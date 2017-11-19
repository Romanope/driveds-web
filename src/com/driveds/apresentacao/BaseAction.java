package com.driveds.apresentacao;

import org.springframework.ui.ModelMap;

public class BaseAction {

	protected static final int TP_MSG_ERRO = 1;
	protected static final int TP_MSG_SUCESSO = 2;
	
	protected void setMsg (ModelMap map, String mensagemError, String msgSuccess) {
		
		StringBuilder mensagem = new StringBuilder();
		if (mensagemError != null && mensagemError.length() > 0) {
			mensagem.append(gerarMensagemErro(mensagemError));
		}
		if (msgSuccess != null && msgSuccess.length() > 0) {
			if (mensagem.length() > 0) {
				mensagem.append("<br />");
			}
			mensagem.append(gerarMensagemSucesso(msgSuccess));
		}
		if (mensagem.length() > 0) {
			map.addAttribute("mensagem", mensagem);
		}
	}
	
	private String gerarMensagemErro(String msg) {
		return "<div class=\"ui-alert-message\" style=\"text-align: center;\"> " + msg + " </div>";
	}
	
	private String gerarMensagemSucesso(String msg) {
		return "<div class=\"ui-success-message\" style=\"text-align: center;\"> " + msg + " </div>";
	}
}
