package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import br.gov.economia.apipgdsusep.enums.EPaginasAcesso;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Entity
@Table(name = "tb_funcao", schema = "dbo")
public class Funcao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_funcao")
	private Long id;
	
	@Column(name="ds_funcao")
	private String descricao; 

	@Column(name="ds_detalhe")
	private String detalhe;
	
	@Column(name="ds_pagina_acesso")
	@Enumerated(EnumType.STRING)
	private EPaginasAcesso paginaAcesso;

	@Enumerated(EnumType.STRING)
	@Column(name="st_registro")
	private ESituacaoRegistro situacaoRegistro;
	
	
	public Funcao() { }
	
	public Funcao(ESituacaoRegistro situacaoRegistro) { 
		this.situacaoRegistro = situacaoRegistro;
	}
	

	@Override
	public String toString() {
		return this.getId().toString();
	}
	
	public boolean isCamposObrigatoriosPreenchidos() {
		return (this.getDescricao() != null && StringUtils.isNotBlank(this.getDescricao()) && 
				this.getPaginaAcesso() != null && this.getSituacaoRegistro() != null);
	}
	
	public String getDadosParaBuscaTextual() {
		return (descricao != null ? descricao : "") + "_" + (detalhe != null ? detalhe : "") + "_" + 
				paginaAcesso + "_" + (paginaAcesso != null ? paginaAcesso.getNome() : "") + "_" +
				(situacaoRegistro != null ? "\"" + situacaoRegistro.getDescricao() + "\"" : "");
	}
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}

	public EPaginasAcesso getPaginaAcesso() {
		return paginaAcesso;
	}
	public void setPaginaAcesso(EPaginasAcesso paginaAcesso) {
		this.paginaAcesso = paginaAcesso;
	}

	public ESituacaoRegistro getSituacaoRegistro() {
		return situacaoRegistro;
	}
	public void setSituacaoRegistro(ESituacaoRegistro situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((paginaAcesso == null) ? 0 : paginaAcesso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcao other = (Funcao) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (paginaAcesso != other.paginaAcesso)
			return false;
		return true;
	}

}