package com.driveds.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driveds.dao.UsuarioDAO;
import com.driveds.model.Compartilhamento;
import com.driveds.model.Usuario;

@Service
public class ControladorUsuario {

	@Autowired
	private UsuarioDAO repoUsuario;
	
	@Autowired
	private ControladorCompartilhamento controladorCompartilhamento;
	
	public Usuario salvarUsuario(Usuario usuario) {
		return repoUsuario.save(usuario);
	}
	
	public Usuario getUsuarioByLogin(String login, boolean lazy) {
		return repoUsuario.getUsuarioByLogin(login, lazy);
	}
	
	public Usuario criarUsuario(String login, String senha, String email) {
		
		Usuario usuario = new Usuario();
		
		usuario.setLogin(login);
		usuario.setSenha(senha);
		usuario.setEmail(email);
		usuario.setDiretorioArquivos("diretorio");
		usuario.setData(new java.sql.Date(System.currentTimeMillis()));
		
		return usuario;
	}
	
	public List<Usuario> listAll(String login) {
		
		return repoUsuario.listAll(login);
	}

	public List<Usuario> getUserById(Long[] ids) {
		
		return repoUsuario.getUserById(ids);
	}
}
