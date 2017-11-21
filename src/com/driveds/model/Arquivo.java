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
@Table(name = "ARQUIVO")
public class Arquivo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ARQ_ID")
	private long chavePrimaria;
	
	@Column(name = "ARQ_NM", nullable = false)
	private String nome;
	
	@Column(name = "ARQ_REMOVIDO", nullable = false)
	private boolean removido;
	
	@Column(name = "ARQ_SYN", nullable = false)
	private boolean sincronizado;
	
	@ManyToOne
	@JoinColumn(name="USU_ID")
	private Usuario usuario;

	public long getChavePrimaria() {
		return chavePrimaria;
	}

	public void setChavePrimaria(long chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isRemovido() {
		return removido;
	}

	public void setRemovido(boolean removido) {
		this.removido = removido;
	}

	public boolean isSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(boolean sincronizado) {
		this.sincronizado = sincronizado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
