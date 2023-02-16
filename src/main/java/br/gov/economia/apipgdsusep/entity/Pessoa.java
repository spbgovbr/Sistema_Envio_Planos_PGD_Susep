package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Entity
@Table(name = "tb_pessoa", schema = "dbo")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_pessoa")
	private Long id;

	@Column(name="ds_nome")
	private String nome;

	@Column(name="nr_cpf")
	private String numeroCPF;

	@Enumerated(EnumType.STRING)
	@Column(name="st_registro")
	private ESituacaoRegistro situacaoRegistro;
	
	@OneToOne(mappedBy="pessoa",
			targetEntity=Usuario.class,
			cascade=CascadeType.ALL)
	private Usuario usuario;


	@Override
	public String toString() {
		return this.getId().toString();
	}

	public Pessoa() { }
	
	public Pessoa(ESituacaoRegistro situacaoRegistro) {
		super();
		this.situacaoRegistro = situacaoRegistro;
	}

	
	@Transient
	public boolean isCamposObrigatoriosPreenchidos() {
		return ((this.nome != null && StringUtils.isNotBlank(this.nome)) &&
				(this.numeroCPF != null && StringUtils.isNotBlank(this.numeroCPF)) &&
				(this.situacaoRegistro != null));
	}

	@Transient
	public String getDadosParaBuscaTextual() {
		return (nome != null ? nome : "") + "_" + 
				(numeroCPF != null ? numeroCPF : "") + "_" + 
				(situacaoRegistro != null ? "\"" + situacaoRegistro.getDescricao() + "\"" : "");
	}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumeroCPF() {
		return numeroCPF;
	}
	public void setNumeroCPF(String numeroCPF) {
		this.numeroCPF = numeroCPF;
	}

	public ESituacaoRegistro getSituacaoRegistro() {
		return situacaoRegistro;
	}
	public void setSituacaoRegistro(ESituacaoRegistro situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroCPF == null) ? 0 : numeroCPF.hashCode());
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
		Pessoa other = (Pessoa) obj;
		if (numeroCPF == null) {
			if (other.numeroCPF != null)
				return false;
		} else if (!numeroCPF.equals(other.numeroCPF))
			return false;
		return true;
	}

}