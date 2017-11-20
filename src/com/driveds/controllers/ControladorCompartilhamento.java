package com.driveds.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driveds.apresentacao.ArquivoVO;
import com.driveds.dao.CompartilhamentoDAO;
import com.driveds.model.Compartilhamento;

@Service
public class ControladorCompartilhamento {

	@Autowired
	private ControladorArquivo controladorArquivo;
	
	@Autowired
	private CompartilhamentoDAO compartilhamentoDAO;
	
	public List<ArquivoVO> adicionarArqCompartilhamentos (List<Compartilhamento> compartilhamentos) {
		
		List<ArquivoVO> arqs = new ArrayList<ArquivoVO>();

		for (Compartilhamento com: compartilhamentos) {
			ArquivoVO arq = controladorArquivo.obterArquivosPorUsusario(com.getUsuarioDono().getLogin(), com.getNomeArquivo()).get(0);
			arq.setTitle(com.getUsuarioDono().getLogin().concat(" compartilhou com voc�"));
			arq.setDeTerceiro(true);
			arqs.add(arq);
		}
		
		return arqs;
	}
	
	public boolean existeCompartilhamento (Long usuarioLogado, Long usuarioCompartilhamento, String nomeArquivo) {
		
		Compartilhamento compartilhamento = compartilhamentoDAO.consultarCompartilhamento(usuarioLogado, usuarioCompartilhamento, nomeArquivo);
		
		return compartilhamento != null;
	}
	
	public List<Compartilhamento> consultarCompartilhamentoArquivo (Long usuarioLogado, String nomeArquivo) {
		
		return compartilhamentoDAO.consultarCompartilhamentosArquivo(usuarioLogado, nomeArquivo);
	}
	
	public void salvarCompartilhamento (Compartilhamento compartilhamento) {
		
		compartilhamentoDAO.save(compartilhamento);
	}
}
