package com.driveds.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

	public static File createFile (String fileName, InputStream input) {
		
		File targetFile = null;
	    try {
			targetFile = new File(fileName);
			write(input, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return targetFile;
	}
	
	
	public static byte[] getBytes (InputStream input) throws IOException {
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = input.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();

		return buffer.toByteArray();
	}
	
	public static void write (InputStream input, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		out.write(getBytes(input));
		out.close();
	}
}
