package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.economia.apipgdsusep.enums.ESimNao;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Entity
@Table(name = "rl_perfil_funcao", schema = "dbo")
public class PerfilFuncao implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({ 
		@AttributeOverride(name="idPerfil", column=@Column(name="id_perfil", nullable = false)), 
		@AttributeOverride(name="idFuncao", column=@Column(name="id_funcao", nullable = false))
	})
	private PerfilFuncaoPK perfilFuncaoPK;

	@ManyToOne
	@JoinColumn(name="id_perfil", nullable=false, insertable=false, updatable=false)
	private Perfil perfil;

	@ManyToOne
	@JoinColumn(name="id_funcao", nullable=false, insertable=false, updatable=false)
	private Funcao funcao;
	
	@Column(name="st_alterar")
	@Enumerated(EnumType.STRING)
	private ESimNao situacaoAlterar;
	
	@Column(name="st_cadastrar")
	@Enumerated(EnumType.STRING)
	private ESimNao situacaoCadastrar;

	@Column(name="st_excluir")
	@Enumerated(EnumType.STRING)
	private ESimNao situacaoExcluir;

	@Column(name="st_visualizar")
	@Enumerated(EnumType.STRING)
	private ESimNao situacaoVisualizar;
	
	@Enumerated(EnumType.STRING)
	@Column(name="st_registro")
	private ESituacaoRegistro situacaoRegistro;


	public PerfilFuncao() {
		
	}
	
	public PerfilFuncao(Perfil perfil, Funcao funcao) {
		this.perfil = perfil;
		this.funcao = funcao;
	}
	
	public PerfilFuncao(Perfil perfil, Funcao funcao, 
			ESimNao situacaoAlterar, ESimNao situacaoCadastrar, 
			ESimNao situacaoExcluir, ESimNao situacaoVisualizar,
			ESituacaoRegistro situacaoRegistro) {
		this.perfil = perfil;
		this.funcao = funcao;
		PerfilFuncaoPK pk = new PerfilFuncaoPK();
		pk.setIdPerfil(perfil.getId());
		pk.setIdFuncao(funcao.getId());
		this.setPerfilFuncaoPK(pk);
		this.setSituacaoAlterar(situacaoAlterar);
		this.setSituacaoCadastrar(situacaoCadastrar);
		this.setSituacaoExcluir(situacaoExcluir);
		this.setSituacaoVisualizar(situacaoVisualizar);
		this.setSituacaoRegistro(situacaoRegistro);
	}
	

	@Override
	public String toString() {
		return "idPerfil:" + this.getPerfilFuncaoPK().getIdPerfil().toString() 
				+ "_" + "idFuncao:" + this.getPerfilFuncaoPK().getIdFuncao().toString();
	}
	
	
	public PerfilFuncaoPK getPerfilFuncaoPK() {
		if(perfilFuncaoPK == null) {
			perfilFuncaoPK = new PerfilFuncaoPK();
		}
		return perfilFuncaoPK;
	}
	public void setPerfilFuncaoPK(PerfilFuncaoPK perfilFuncaoPK) {
		this.perfilFuncaoPK = perfilFuncaoPK;
	}

	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Funcao getFuncao() {
		return funcao;
	}
	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public ESimNao getSituacaoAlterar() {
		return situacaoAlterar;
	}
	public void setSituacaoAlterar(ESimNao situacaoAlterar) {
		this.situacaoAlterar = situacaoAlterar;
	}

	public ESimNao getSituacaoCadastrar() {
		return situacaoCadastrar;
	}
	public void setSituacaoCadastrar(ESimNao situacaoCadastrar) {
		this.situacaoCadastrar = situacaoCadastrar;
	}

	public ESimNao getSituacaoExcluir() {
		return situacaoExcluir;
	}
	public void setSituacaoExcluir(ESimNao situacaoExcluir) {
		this.situacaoExcluir = situacaoExcluir;
	}

	public ESimNao getSituacaoVisualizar() {
		return situacaoVisualizar;
	}
	public void setSituacaoVisualizar(ESimNao situacaoVisualizar) {
		this.situacaoVisualizar = situacaoVisualizar;
	}

	public ESituacaoRegistro getSituacaoRegistro() {
		return situacaoRegistro;
	}
	public void setSituacaoRegistro(ESituacaoRegistro situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}


	public static final Comparator<PerfilFuncao> DescricaoFuncaoComparator = new Comparator<PerfilFuncao>() {
		@Override
		public int compare(final PerfilFuncao obj1, final PerfilFuncao obj2) {
			// Ordenação Ascendente
			return obj1.getFuncao().getDescricao().compareTo(obj2.getFuncao().getDescricao());
		}
	};
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((perfilFuncaoPK == null) ? 0 : perfilFuncaoPK.hashCode());
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
		PerfilFuncao other = (PerfilFuncao) obj;
		if (perfilFuncaoPK == null) {
			if (other.perfilFuncaoPK != null)
				return false;
		} else if (!perfilFuncaoPK.equals(other.perfilFuncaoPK))
			return false;
		return true;
	}

}