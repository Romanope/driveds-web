package com.driveds.apresentacao;

import java.io.Serializable;

public class ArquivoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;
	
	private String dataModificacao;
	
	private String diretorioCompleto;

	private long tamanho;
	
	private String usuario;
	
	private String title;
	
	private boolean deTerceiro;
	
	private long chavePrimariaArquivo;
	
	public ArquivoVO(String nome, String dataModificacao, String diretorioCompleto) {
		super();
		this.nome = nome;
		this.dataModificacao = dataModificacao;
		this.diretorioCompleto = diretorioCompleto;
	}

	public ArquivoVO(String nome, String dataModificacao, long tamanho, String login) {
		super();
		this.nome = nome;
		this.dataModificacao = dataModificacao;
		this.tamanho = tamanho;
		this.usuario = login;
	}

	public ArquivoVO() {
		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(String dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	public String getDiretorioCompleto() {
		return diretorioCompleto;
	}

	public void setDiretorioCompleto(String diretorioCompleto) {
		this.diretorioCompleto = diretorioCompleto;
	}

	public long getTamanho() {
		return tamanho;
	}

	public void setTamanho(long tamanho) {
		this.tamanho = tamanho;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDeTerceiro() {
		return deTerceiro;
	}

	public void setDeTerceiro(boolean deTerceiro) {
		this.deTerceiro = deTerceiro;
	}

	public long getChavePrimariaArquivo() {
		return chavePrimariaArquivo;
	}

	public void setChavePrimariaArquivo(long chavePrimariaArquivo) {
		this.chavePrimariaArquivo = chavePrimariaArquivo;
	}
}
