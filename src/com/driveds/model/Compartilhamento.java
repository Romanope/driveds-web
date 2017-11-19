package com.driveds.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COMPARTILHAMENTO")
public class Compartilhamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMP_ID")
	private long chavePrimaria;
	
	@ManyToOne
	@JoinColumn(name="USU_ID_PROP")
	private Usuario usuarioDono;
	
	@Column(name = "FILE_NAME", nullable = false)
	private String nomeArquivo;
	
	@ManyToOne
	@JoinColumn(name="USU_ID_COMP")
	private Usuario usuarioCompartilhamento;

	@Column(name = "COMP_SYN", nullable = false)
	private boolean sincronizado;
	
	public Compartilhamento(Usuario usuarioDono, String nomeArquivo, Usuario usuarioCompartilhamento) {
		this.usuarioDono = usuarioDono;
		this.nomeArquivo = nomeArquivo;
		this.usuarioCompartilhamento = usuarioCompartilhamento;
	}

	public Compartilhamento() {

	}

	public Usuario getUsuarioDono() {
		return usuarioDono;
	}

	public void setUsuarioDono(Usuario usuarioDono) {
		this.usuarioDono = usuarioDono;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Usuario getUsuarioCompartilhamento() {
		return usuarioCompartilhamento;
	}

	public void setUsuarioCompartilhamento(Usuario usuarioCompartilhamento) {
		this.usuarioCompartilhamento = usuarioCompartilhamento;
	}

	public boolean isSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(boolean sincronizado) {
		this.sincronizado = sincronizado;
	}
}
