package com.driveds.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USU_ID")
	private long chavePrimaria;
	
	@Column(name = "USU_LOGIN", nullable = true)
	private String login;
	
	@Column(name = "USU_SENHA", nullable = true)
	private String senha;
	
	@Column(name = "USU_EMAIL", nullable = true)
	private String email;
	
	@Column(name = "USU_DIRE_ARQUIVO", nullable = true)
	private String diretorioArquivos;
	
	@Column(name = "USU_DH_CADASTRO", nullable = true)
	private java.sql.Date data;
	
	@OneToMany(mappedBy = "usuarioDono", targetEntity = Compartilhamento.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Compartilhamento> arquivosCompartilhados;

	@OneToMany(mappedBy = "usuarioCompartilhamento", targetEntity = Compartilhamento.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Compartilhamento> arquivosRecebidos;
	
	public Usuario() {
		arquivosCompartilhados = new ArrayList<Compartilhamento>();
	}

	public long getChavePrimaria() {
		return chavePrimaria;
	}


	public void setChavePrimaria(long chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDiretorioArquivos() {
		return diretorioArquivos;
	}


	public void setDiretorioArquivos(String diretorioArquivos) {
		this.diretorioArquivos = diretorioArquivos;
	}


	public java.sql.Date getData() {
		return data;
	}


	public void setData(java.sql.Date data) {
		this.data = data;
	}

	public List<Compartilhamento> getArquivosCompartilhados() {
		return arquivosCompartilhados;
	}
}
