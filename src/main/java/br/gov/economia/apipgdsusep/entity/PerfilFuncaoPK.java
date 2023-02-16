package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PerfilFuncaoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="id_perfil")
	private Long idPerfil;

	@Column(name="id_funcao")
	private Long idFuncao;

	
	public Long getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}
	
	public Long getIdFuncao() {
		return idFuncao;
	}
	public void setIdFuncao(Long idFuncao) {
		this.idFuncao = idFuncao;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idFuncao == null) ? 0 : idFuncao.hashCode());
		result = prime * result + ((idPerfil == null) ? 0 : idPerfil.hashCode());
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
		PerfilFuncaoPK other = (PerfilFuncaoPK) obj;
		if (idFuncao == null) {
			if (other.idFuncao != null)
				return false;
		} else if (!idFuncao.equals(other.idFuncao))
			return false;
		if (idPerfil == null) {
			if (other.idPerfil != null)
				return false;
		} else if (!idPerfil.equals(other.idPerfil))
			return false;
		return true;
	}

}