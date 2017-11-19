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
	
	public Usuario getUsuarioByLogin(String login) {
		return repoUsuario.getUsuarioByLogin(login);
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
	
	public String salvarCompartilhamento(String nomeArq, Usuario usuarioPropArq, List<Usuario> usuariosCompartilhamento) {
		
		Compartilhamento compartilhamento;
		StringBuilder errors = new StringBuilder();
		boolean salvar = false;
		for (Usuario usuario: usuariosCompartilhamento) {
			if (compartilhamentoValido(errors, usuario, usuarioPropArq, nomeArq)) {
				compartilhamento = new Compartilhamento();
				compartilhamento.setUsuarioCompartilhamento(usuario);
				compartilhamento.setUsuarioDono(usuarioPropArq);
				compartilhamento.setNomeArquivo(nomeArq);
				usuarioPropArq.getArquivosCompartilhados().add(compartilhamento);
				salvar = true;
			}
		}
		
		if (salvar) {
			this.salvarUsuario(usuarioPropArq);
		}
		
		return errors.toString();
	}
	
	public List<Compartilhamento> consultarCompartilhamentos(Usuario usuario) {
		return repoUsuario.consultarCompartilhamentoPorUsuarioRecebido(usuario);
	}
	
	private boolean compartilhamentoValido (StringBuilder errors, Usuario UsuarioCompartilhamento, Usuario UsuarioLogado, String nomeArquivo) {
		if (controladorCompartilhamento.existeCompartilhamento(UsuarioLogado.getChavePrimaria(), UsuarioCompartilhamento.getChavePrimaria(), nomeArquivo)) {
			errors = errors.length() == 0 ? errors.append(UsuarioCompartilhamento.getLogin()) : errors.append(", ").append(UsuarioCompartilhamento.getLogin());
			return false;
		}
		return true;
	}
}
