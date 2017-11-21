package com.driveds.adapters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.driveds.util.Util;

public class AmazonS3Adapter {

	private static final String BUCKET_NAME     = "driveds";
	private static final String REGION			= "us-east-2";
	
	private static AmazonS3Adapter instance;
	
	public static AmazonS3Adapter get () {
		if (instance == null) {
			instance = new AmazonS3Adapter();
		}
		return instance;
	}
	
	public AWSCredentials getCredentials () {
		
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        
        return credentials;
	}
	
	public AmazonS3 getAmazons3 (AWSCredentials credentials) {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
        		.withRegion(REGION)
        		.build();
	}
	
	public boolean uploadFile (String usuario, File file) {

		try {
			String fileName = usuario;
			if (usuario != null) {
				fileName += "/";
			}
			fileName += file.getName();
			System.out.println("upload file " + fileName);
			AmazonS3 s3 = getAmazons3(getCredentials());
			s3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public File downloadFile (String login, String objectName) throws IOException {
		
        
		objectName = login + "/" + objectName;
		System.out.println("Downloading object: " + objectName);
		AmazonS3 s3 = getAmazons3(getCredentials());
		S3Object object = s3.getObject(BUCKET_NAME, objectName);
        System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
        File file = new File(login);
        if (!file.exists()) {
        	file.mkdirs();
        }
        return Util.createFile(objectName, object.getObjectContent());
	}

	
	public boolean deleteFile (String login, String fileName) {
		
		fileName = login + "/" + fileName;
		System.out.println("deleting file " + fileName);
		try {
			AmazonS3 s3 = getAmazons3(getCredentials());
			s3.deleteObject(BUCKET_NAME, fileName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<String> getNameFiles (String usuario) {
		
		List<String> objects = new ArrayList<String>();
		AmazonS3 s3 = getAmazons3(getCredentials());
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(BUCKET_NAME));
        System.out.println("-------------- object keys ---------------------");
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	System.out.println(objectSummary.getKey());
        	objects.add(objectSummary.getKey());
        }
		System.out.println("-------------- object keys END---------------------");
		
		return objects;
	}
	
	public static void main(String[] args) {
		AmazonS3Adapter aws = AmazonS3Adapter.get();
		try {
			
			
//			aws.getNameFiles(null);
//			aws.uploadFile("romano", new File("C:\\Users\\Romano\\Downloads\\teste.txt"));
			aws.downloadFile("romano", "teste.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
