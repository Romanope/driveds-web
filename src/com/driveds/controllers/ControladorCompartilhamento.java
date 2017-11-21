package com.driveds.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driveds.apresentacao.ArquivoVO;
import com.driveds.dao.CompartilhamentoDAO;
import com.driveds.model.Arquivo;
import com.driveds.model.Compartilhamento;
import com.driveds.model.Usuario;

@Service
public class ControladorCompartilhamento {

	@Autowired
	private CompartilhamentoDAO compartilhamentoDAO;
	
	@Autowired
	private ControladorArquivo controladorArquivo;
	
	public List<ArquivoVO> adicionarArqCompartilhamentos (List<Compartilhamento> compartilhamentos) {
		
		List<ArquivoVO> arqs = new ArrayList<ArquivoVO>();

		for (Compartilhamento com: compartilhamentos) {
			Arquivo arqCompartilhamento = com.getArquivo();
			ArquivoVO arq = new ArquivoVO(arqCompartilhamento.getNome(), new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()), 321321, com.getUsuarioDono().getLogin());
			arq.setTitle(com.getUsuarioDono().getLogin().concat(" compartilhou com voce"));
			arq.setDeTerceiro(true);
			arq.setChavePrimariaArquivo(arqCompartilhamento.getChavePrimaria());
			arqs.add(arq);
		}
		
		return arqs;
	}
	
	public boolean existeCompartilhamento (Long usuarioLogado, Long usuarioCompartilhamento, Long chaveArquivo) {
		
		Compartilhamento compartilhamento = compartilhamentoDAO.consultarCompartilhamento(usuarioLogado, usuarioCompartilhamento, chaveArquivo);
		
		return compartilhamento != null;
	}
	
	public List<Compartilhamento> consultarCompartilhamentoArquivo (Long usuarioLogado, Long chavePrimariaArquivo) {
		
		return compartilhamentoDAO.consultarCompartilhamentosArquivo(usuarioLogado, chavePrimariaArquivo);
	}
	
	public void salvarCompartilhamento (Compartilhamento compartilhamento) {
		
		compartilhamentoDAO.save(compartilhamento);
	}
	
	public List<Compartilhamento> consultarCompartilhamentosRecebidos (Long usuarioCompartilhamento, String filtro) {
		
		return compartilhamentoDAO.consultarCompartilhamentosRecebidos(usuarioCompartilhamento, filtro);
	}
	
	public String salvarCompartilhamento(Long chaveArquivo, Usuario usuarioPropArq, List<Usuario> usuariosCompartilhamento) {
		
		Compartilhamento compartilhamento;
		StringBuilder errors = new StringBuilder();
		for (Usuario usuario: usuariosCompartilhamento) {
			if (compartilhamentoValido(errors, usuario, usuarioPropArq, chaveArquivo)) {
				compartilhamento = new Compartilhamento();
				compartilhamento.setUsuarioCompartilhamento(usuario);
				compartilhamento.setUsuarioDono(usuarioPropArq);
				compartilhamento.setArquivo(controladorArquivo.obterArquivo(chaveArquivo));
				compartilhamento.setAtivo(true);
				salvarCompartilhamento(compartilhamento);
			}
		}
		
		return errors.toString();
	}
	
	private boolean compartilhamentoValido (StringBuilder errors, Usuario UsuarioCompartilhamento, Usuario UsuarioLogado, Long chaveArquivo) {
		if (existeCompartilhamento(UsuarioLogado.getChavePrimaria(), UsuarioCompartilhamento.getChavePrimaria(), chaveArquivo)) {
			errors = errors.length() == 0 ? errors.append(UsuarioCompartilhamento.getLogin()) : errors.append(", ").append(UsuarioCompartilhamento.getLogin());
			return false;
		}
		return true;
	}
	
	public void invativarCompartilhamentos (Long chaveArquivo) {
		
		List<Compartilhamento> compartilhamentos = compartilhamentoDAO.consultarCompartilhamentosPorArquivo(chaveArquivo);
		for (Compartilhamento compartilhamento: compartilhamentos) {
			compartilhamento.setSincronizado(false);
			compartilhamento.setAtivo(false);
			compartilhamentoDAO.save(compartilhamento);
		}
	}
}
