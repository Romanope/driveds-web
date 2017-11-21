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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driveds.adapters.AmazonS3Adapter;
import com.driveds.apresentacao.ArquivoVO;
import com.driveds.dao.ArquivoDAO;
import com.driveds.model.Arquivo;
import com.driveds.model.Compartilhamento;
import com.driveds.model.Usuario;
import com.driveds.util.Constantes;

@Service
public class ControladorArquivo {

	@Autowired
	private ControladorUsuario controladorUsuario;
	
	@Autowired 
	private ControladorCompartilhamento controladorCompartilhamento;

	@Autowired
	private ArquivoDAO arquivoDAO;
	
	public void salvarArquivo (HttpServletRequest request, String login) throws Exception {
		if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                String name = "";
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        name = new File(item.getName()).getName();
                        System.out.println("name "+name);
                        item.write(new File(Constantes.DEFAULT_DIRECTORY + login + File.separator + name));
                    }
                }
                
                File file = new File(Constantes.DEFAULT_DIRECTORY + login + File.separator + name);
                AmazonS3Adapter s3 = AmazonS3Adapter.get();
                if (s3.uploadFile(login, file)) {
                	Usuario usuario = controladorUsuario.getUsuarioByLogin(login, true);
                	salvarAtualizarArquivo(usuario, name);
                	atualizarFlagCompartilhamentos(usuario.getChavePrimaria(), name);
                	file.delete();
                }
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
	}
	
	public void criarDiretorioUsuario (String login) {
		String diretorio = (Constantes.DEFAULT_DIRECTORY +  login);
		File file = new File(diretorio);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public List<ArquivoVO> obterArquivosPorUsusario (Usuario usuario, String filtro) {
		
		List<ArquivoVO> arquivos = new ArrayList<ArquivoVO>();
		List<Arquivo> arquivosUsuario = consultarArquivos(usuario.getChavePrimaria(), filtro);
		for (Arquivo arquivo: arquivosUsuario) {
			ArquivoVO arquivoVO = new ArquivoVO(arquivo.getNome(), new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()), 321321, usuario.getLogin());
			arquivoVO.setChavePrimariaArquivo(arquivo.getChavePrimaria());
			arquivos.add(arquivoVO);
		}
		
		return arquivos;
	}
	/**
	 * Remove arquivo do diretório do usuário no S3 da amazon
	 * @param login
	 * @param nomeArquivo
	 * @return true, case success on delete
	 */
	public boolean apagarArquivo (String login, String nomeArquivo) {
		
		AmazonS3Adapter s3 = AmazonS3Adapter.get();
		
		return s3.deleteFile(login, nomeArquivo);
	}
	
	public void downloadFile (String login, String nomeArquivo, ServletOutputStream out) throws IOException {
	
		File file = AmazonS3Adapter.get().downloadFile(login, nomeArquivo);
		FileInputStream fileIn = new FileInputStream(file);
	
		byte[] outputByte = new byte[4096];
		while (fileIn.read(outputByte, 0, 4096) != -1) {
			out.write(outputByte, 0, 4096);
		}
		
		fileIn.close();
		out.flush();
		out.close();
		file.delete();
	}
	
	private void atualizarFlagCompartilhamentos (Long usuarioDono, String fileName) {
		Arquivo arquivo = consultarArquivo(usuarioDono, fileName);
		List<Compartilhamento> compartilhamentos = controladorCompartilhamento.consultarCompartilhamentoArquivo(usuarioDono, arquivo.getChavePrimaria());
		for (Compartilhamento compartilhamento: compartilhamentos) {
			compartilhamento.setSincronizado(false);
			controladorCompartilhamento.salvarCompartilhamento(compartilhamento);
		}
	}
	
	public void salvarAtualizarArquivo (Usuario usuario, String nomeArquivo) {
		
		Arquivo arquivo = consultarArquivo(usuario.getChavePrimaria(), nomeArquivo);
		if (arquivo == null) {
			arquivo = new Arquivo();
			arquivo.setNome(nomeArquivo);
			arquivo.setUsuario(usuario);
		}
		arquivo.setSincronizado(false);
		salvarArquivo(arquivo);
	}
	
	public Arquivo consultarArquivo (Long chavePrimaria, String nomeArquivo) {
		
		return arquivoDAO.consultarArquivo(chavePrimaria, nomeArquivo);
	}
	
	public void salvarArquivo (Arquivo arquivo) {
		
		arquivoDAO.save(arquivo);
	}
	
	public Arquivo removerArquivo (Usuario usuario, String nomeArquivo) {
		
		Arquivo arquivo = consultarArquivo(usuario.getChavePrimaria(), nomeArquivo);
		if (arquivo != null) {
			arquivo.setRemovido(true);
			arquivo.setSincronizado(false);
			arquivoDAO.save(arquivo);
		}
		
		return arquivo;
	}
	
	public List<Arquivo> consultarArquivos (Long chavePrimariaUsuario, String nomeArquivo) {
		
		return arquivoDAO.consultarArquivos(chavePrimariaUsuario, nomeArquivo);
	}
	
	public Arquivo obterArquivo (Long chavePrimaria) {
		
		return arquivoDAO.get(chavePrimaria);
	}
}
