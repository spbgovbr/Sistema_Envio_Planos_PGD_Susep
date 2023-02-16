package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_parametro", schema = "dbo")
public class Parametro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ds_chave")
	private String chave;
	
	@Column(name="ds_valor")
	private String valor;


	public Parametro() { }
	
	public Parametro(String chave, String valor) {
		super();
		this.chave = chave;
		this.valor = valor;
	}
	
	@Transient
	public String getDadosParaBuscaTextual() {
		return chave + "_" + valor;
	}
	
	
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
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
		Parametro other = (Parametro) obj;
		if (chave == null) {
			if (other.chave != null)
				return false;
		} else if (!chave.equals(other.chave))
			return false;
		return true;
	}
	
}