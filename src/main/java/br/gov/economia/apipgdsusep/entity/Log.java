package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.enums.ETipoLog;

@Entity
@Table(name = "tb_log", schema = "dbo")
public class Log implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_log")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@Column(name="ds_acao", columnDefinition="LONGTEXT")
	private String descricaoAcao;
	
	@Column(name="dt_acao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAcao;
	
	@Column(name="ds_tipo_acao")
	@Enumerated(EnumType.STRING)
	private ETipoLog tipoAcao;
	
	@Column(name="ds_nivel_acao")
	@Enumerated(EnumType.STRING)
	private ENivelLog nivelAcao;
	
	
	public Log() { }
	
	public Log(Usuario usuario, String descricaoAcao, Date dataAcao, ETipoLog tipoAcao, ENivelLog nivelAcao) {
		super();
		this.usuario = usuario;
		this.descricaoAcao = descricaoAcao;
		this.dataAcao = dataAcao;
		this.tipoAcao = tipoAcao;
		this.nivelAcao = nivelAcao;
	}


	@Override
	public String toString() {
		return this.getId().toString();
	}
	
	public String getDadosParaBuscaTextual() {
		return (usuario != null ? usuario.getPessoa().getNome() : "") + "_" + 
				(descricaoAcao != null ? descricaoAcao : "") + "_" + 
				(dataAcao != null ? new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss").format(dataAcao.getTime()) : "") + "_" +
				(nivelAcao != null ? "\"" + nivelAcao.getDetalhe() + "\"" : "") + 
				(tipoAcao != null ? "\"" + tipoAcao.getDetalhe() + "\"" : "");
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDescricaoAcao() {
		return descricaoAcao;
	}
	public void setDescricaoAcao(String descricaoAcao) {
		this.descricaoAcao = descricaoAcao;
	}

	public Date getDataAcao() {
		return dataAcao;
	}
	public void setDataAcao(Date dataAcao) {
		this.dataAcao = dataAcao;
	}
	
	public ETipoLog getTipoAcao() {
		return tipoAcao;
	}
	public void setTipoAcao(ETipoLog tipoAcao) {
		this.tipoAcao = tipoAcao;
	}
	
	public ENivelLog getNivelAcao() {
		return nivelAcao;
	}
	public void setNivelAcao(ENivelLog nivelAcao) {
		this.nivelAcao = nivelAcao;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricaoAcao == null) ? 0 : descricaoAcao.hashCode());
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
		Log other = (Log) obj;
		if (descricaoAcao == null) {
			if (other.descricaoAcao != null)
				return false;
		} else if (!descricaoAcao.equals(other.descricaoAcao))
			return false;
		return true;
	}
	
}