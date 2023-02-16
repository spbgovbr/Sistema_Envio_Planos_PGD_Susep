package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vw_tipo_atividade")
public class TipoAtividade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_tipo_atividade")
	private Integer id;
	
	@Column(name="faixa_complexidade")
	private String faixaComplexidade;
	
	@Column(name="parametro_complexidade")
	private String parametro_complexidade;
	
	
	public TipoAtividade() { }


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getFaixaComplexidade() {
		return faixaComplexidade;
	}
	public void setFaixaComplexidade(String faixaComplexidade) {
		this.faixaComplexidade = faixaComplexidade;
	}

	public String getParametro_complexidade() {
		return parametro_complexidade;
	}
	public void setParametro_complexidade(String parametro_complexidade) {
		this.parametro_complexidade = parametro_complexidade;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TipoAtividade other = (TipoAtividade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}