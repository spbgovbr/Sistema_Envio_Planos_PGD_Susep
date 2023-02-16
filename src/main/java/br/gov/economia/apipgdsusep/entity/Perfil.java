package br.gov.economia.apipgdsusep.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Entity
@Table(name = "tb_perfil", schema = "dbo")
public class Perfil {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil")
	private Long id;
	
	@Column(name = "ds_perfil")
	private String descricao;
	
	@Column(name = "ds_detalhe")
	private String detalhe;
	
	@ManyToMany(mappedBy = "perfis")
    private List <Usuario> usuarios;
	
	@Enumerated(EnumType.STRING)
	@Column(name="st_registro")
	private ESituacaoRegistro situacaoRegistro;
	
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy="perfil", orphanRemoval = true,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
	private List<PerfilFuncao> funcoesDoPerfilLista;
	
	
	public Perfil() { }
	
	public Perfil(ESituacaoRegistro situacaoRegistro) {
		super();
		this.situacaoRegistro = situacaoRegistro;
	}
	

	@Override
	public String toString() {
		return this.getId().toString();
	}
	
	
	@Transient
	public boolean isCamposObrigatoriosPreenchidos() {
		return (this.descricao != null && StringUtils.isNotBlank(this.descricao) && this.situacaoRegistro != null);
	}
	
	@Transient
	public String getDadosParaBuscaTextual() {
		return (descricao != null ? descricao : "") + "_" + (detalhe != null ? detalhe : "") + "_" + 
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

	public ESituacaoRegistro getSituacaoRegistro() {
		return situacaoRegistro;
	}
	public void setSituacaoRegistro(ESituacaoRegistro situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}
	
	public List<PerfilFuncao> getFuncoesDoPerfilLista() {
		if(funcoesDoPerfilLista == null) {
			funcoesDoPerfilLista = new ArrayList<PerfilFuncao>();
		}
		return funcoesDoPerfilLista;
	}
	public void setFuncoesDoPerfilLista(List<PerfilFuncao> funcoesDoPerfilLista) {
		this.funcoesDoPerfilLista = funcoesDoPerfilLista;
	}
	
	public String getFuncoesDoPerfilListaDetalhada(List<PerfilFuncao> funcoesDoPerfilLista) {
		StringBuilder sb = new StringBuilder("Funções do Perfil: ");
		if(funcoesDoPerfilLista != null && funcoesDoPerfilLista.size() > 0) {
			for(PerfilFuncao pf : funcoesDoPerfilLista) {
				sb.append(pf.getFuncao().getDescricao() + ", ");
			}
		} else {
			sb.append("Nenhuma função adicionada");
		}
		return sb.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
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
		Perfil other = (Perfil) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}

}