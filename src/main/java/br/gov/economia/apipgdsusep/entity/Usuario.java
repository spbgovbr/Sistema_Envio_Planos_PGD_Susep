package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import br.gov.economia.apipgdsusep.enums.EPaginasAcesso;
import br.gov.economia.apipgdsusep.enums.ESimNao;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Entity
@Table(name = "tb_usuario", schema = "dbo")
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="id_pessoa")
	private Pessoa pessoa;
	
	@Column(name="ds_email")
	private String email;
	
	@Column(name="co_senha")
	private String codigoSenha;
	
	@Enumerated(EnumType.STRING)
	@Column(name="st_registro")
	private ESituacaoRegistro situacaoRegistro;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="rl_usuario_perfil", schema="dbo",
	joinColumns = @JoinColumn(name="id_usuario", referencedColumnName = "id_usuario"),
	inverseJoinColumns = @JoinColumn(name="id_perfil", referencedColumnName = "id_perfil"))
	private List<Perfil> perfis;
	
	@Transient
	private String senha;
	
	@Transient
	private List<EPaginasAcesso> paginasAcessoUsuario;

	
	@Override
	public String toString() {
		return this.getId().toString();
	}
	
	
	public Usuario() { }
	
	public Usuario(ESituacaoRegistro situacaoRegistro) {
		super();
		this.situacaoRegistro = situacaoRegistro;
		this.pessoa = new Pessoa(ESituacaoRegistro.ATIVO);
	}
	
	@Transient
	public boolean isCamposObrigatoriosPreenchidos() {
		return ((this.pessoa.getNome() != null && StringUtils.isNotBlank(this.pessoa.getNome())) &&
				(this.pessoa.getNumeroCPF() != null && StringUtils.isNotBlank(this.pessoa.getNumeroCPF())) &&
				(this.email != null && StringUtils.isNotBlank(this.email)) &&
				(this.situacaoRegistro != null));
	}
	
	@Transient
	public String getDadosParaBuscaTextual() {
		return (pessoa != null ? pessoa.getNome() : "") + "_" + (email != null ? email : "") + "_" +
				(situacaoRegistro != null ? "\"" + situacaoRegistro.getDescricao() + "\"" : "") + "_" +
				this.getPerfisUsuario();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodigoSenha() {
		return codigoSenha;
	}
	public void setCodigoSenha(String codigoSenha) {
		this.codigoSenha = codigoSenha;
	}

	public ESituacaoRegistro getSituacaoRegistro() {
		return situacaoRegistro;
	}
	public void setSituacaoRegistro(ESituacaoRegistro situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}

	public List<Perfil> getPerfis() {
		if(perfis == null) {
			perfis = new ArrayList<Perfil>();
		}
		return perfis;
	}
	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public List<EPaginasAcesso> getPaginasAcessoUsuario() {
		if(paginasAcessoUsuario == null) {
			paginasAcessoUsuario = new ArrayList<EPaginasAcesso>();
			this.popularPaginaAcessoUsuario();
		}
		return paginasAcessoUsuario;
	}
	public void setPaginasAcessoUsuario(List<EPaginasAcesso> paginasAcessoUsuario) {
		this.paginasAcessoUsuario = paginasAcessoUsuario;
	}
	

	public boolean temPerfil(String... perfis) {
		return getPerfis()
				.stream()
				.filter(p -> Arrays
						.stream(perfis)
						.anyMatch(v -> v.equals(p.getDescricao())))
				.findAny()
				.orElse(null) != null;
	}
	
	public String getPerfisUsuario() {
		StringBuilder sb = new StringBuilder("");
		if(this.getPerfis() != null && this.getPerfis().size() > 0) {
			this.getPerfis().stream().forEach(p -> sb.append(p.getDescricao()+", "));
		}
		return (StringUtils.isNotBlank(sb.toString()) ? sb.toString().substring(0, sb.toString().length()-2) : "");
	}
	
	public void popularPaginaAcessoUsuario() {
		for(Perfil perfil : this.getPerfis()) {
			if(perfil.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO) 
					&& this.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)) {
				for(PerfilFuncao perfilFuncao : perfil.getFuncoesDoPerfilLista()) {
					if(perfilFuncao.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO) 
							&& perfilFuncao.getPerfil().getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO) 
							&& perfilFuncao.getFuncao().getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)
							&& perfilFuncao.getSituacaoVisualizar().equals(ESimNao.SIM)
							&& !this.getPaginasAcessoUsuario().contains(perfilFuncao.getFuncao().getPaginaAcesso())) {
						this.getPaginasAcessoUsuario().add(perfilFuncao.getFuncao().getPaginaAcesso());
					}
				}
			}
		}
		Collections.sort(this.getPaginasAcessoUsuario());
	}
	
	@Transient
	public PerfilFuncao getFuncaoPorPaginaAcesso(EPaginasAcesso paginaAcesso){
		if(this.getPerfis() != null && this.getPerfis().size() > 0) {
			for(Perfil perfil : this.getPerfis()) {
				for(PerfilFuncao perfilFuncao : perfil.getFuncoesDoPerfilLista()) {
					if(perfilFuncao.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)
							&& perfilFuncao.getPerfil().getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO) 
							&& perfilFuncao.getFuncao().getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)
							&& perfilFuncao.getFuncao().getPaginaAcesso().equals(paginaAcesso)) {
						return perfilFuncao;
					}
				}
			}
		}
		return null;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
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
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		return true;
	}
	
}