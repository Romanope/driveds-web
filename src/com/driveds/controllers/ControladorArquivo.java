package com.driveds.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Service;

import com.driveds.apresentacao.Arquivo;
import com.driveds.util.Constantes;

@Service
public class ControladorArquivo {

	public void salvarArquivo (HttpServletRequest request, String login) throws Exception {
		if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String name = new File(item.getName()).getName();
                        System.out.println("name "+name);
                        item.write(new File(Constantes.DEFAULT_DIRECTORY + login + File.separator + name));
                    }
                }
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
	}
	
	public void criarDiretorioUsuario(String login) {
		String diretorio = (Constantes.DEFAULT_DIRECTORY +  login);
		File file = new File(diretorio);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public List<Arquivo> obterArquivosPorUsusario(String login, String filtro) {
		
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
		
		File diretorio = new File(Constantes.DEFAULT_DIRECTORY.concat(login));
		if (diretorio.exists()) {
			for (File file: diretorio.listFiles()) {
				if (filtro != null) {
					if (file.getName().contains(filtro)) {
						arquivos.add(new Arquivo(file.getName(), new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(file.lastModified())), file.getUsableSpace(), login));
					}	
				} else {
					arquivos.add(new Arquivo(file.getName(), new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(file.lastModified())), file.getUsableSpace(), login));
				}
			}
		}
		return arquivos;
	}
	
	public void apagarArquivo(String login, String nomeArquivo) {
		
		String path = Constantes.DEFAULT_DIRECTORY + login + File.separator + nomeArquivo;
		
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public void downloadFile(String login, String nomeArquivo, ServletOutputStream out) throws IOException {
	
		String path = Constantes.DEFAULT_DIRECTORY + login + File.separator + nomeArquivo;
	
		FileInputStream fileIn = new FileInputStream(path);
	
		byte[] outputByte = new byte[4096];
		while (fileIn.read(outputByte, 0, 4096) != -1) {
			out.write(outputByte, 0, 4096);
		}
		fileIn.close();
		out.flush();
		out.close();
	}
}
