package br.gov.economia.apipgdsusep.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.repository.PerfilJpaRepository;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagemExcecao;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;

@Service
@Transactional(transactionManager = "transactionManager")
public class PerfilService {
	
	@Autowired
	private PerfilJpaRepository perfilJpaRepository;

	@Autowired
	private AjudanteMensagens ajudanteMensagens;


	public Perfil pesquisarPorId(Long id) {
		try {
			return perfilJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public Perfil getRole(String role) {
		Optional<Perfil> perfil = perfilJpaRepository.findByDescricao(role);
		return perfil.get();
	}
	
	public List<Perfil> pesquisarTodos() {
		return perfilJpaRepository.findAll();
	}

	public List<Perfil> pesquisarPorFiltros(Perfil perfil) {
		Specification<Perfil> condition = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (perfil.getDescricao() != null 
					&& StringUtils.isNotBlank(perfil.getDescricao())) {
				predicates.add(builder.like(root.get("descricao"), "%" + perfil.getDescricao() + "%"));
			}
			if (perfil.getDetalhe() != null 
					&& StringUtils.isNotBlank(perfil.getDetalhe())) {
				predicates.add(builder.like(root.get("detalhe"), "%" + perfil.getDetalhe() + "%"));
			}
			if (perfil.getSituacaoRegistro() != null) {
				predicates.add(builder.equal(root.get("situacaoRegistro"), perfil.getSituacaoRegistro()));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		return perfilJpaRepository.findAll(condition);
	}

	public boolean ativarDesativar(Perfil perfilSelecionado) {
		Perfil perfilPesquisado = null;
		try {
			perfilPesquisado = this.pesquisarPorId(perfilSelecionado.getId());
			perfilPesquisado.setSituacaoRegistro(perfilSelecionado.
					getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO) ? ESituacaoRegistro.INATIVO : ESituacaoRegistro.ATIVO);
			perfilPesquisado = perfilJpaRepository.save(perfilPesquisado);
			return true;
		} catch (Exception e)  {
			e.printStackTrace();
			return false;
		}
	}

	public Perfil salvar(Perfil perfil) {
		if(isDadosValidos(perfil)) {
			return perfilJpaRepository.save(perfil);
		}
		return null;
	}

	public boolean isDadosValidos(Perfil perfil) {
		if(!isCamposObrigatorioPreenchidos(perfil))
			return false;
		if(isPerfilCadastrado(perfil))
			return false;
		return true;
	}

	public boolean isCamposObrigatorioPreenchidos(Perfil perfil) {
		boolean isCamposPreenchidos = perfil.isCamposObrigatoriosPreenchidos();
		if(!isCamposPreenchidos) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Os campos obrigatório não foram preenchidos.");
		}
		return isCamposPreenchidos;
	}

	public boolean isPerfilCadastrado(Perfil perfil) {
		if (this.isPerfilJaCadastrado(perfil)) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Já existe um Perfil cadastrado com os dados informados.");
			return true;
		}
		return false;
	}

	public boolean isPerfilJaCadastrado(Perfil perfil) {
		boolean isPerfilJaCadastrado = false;
		Perfil perfilCadastrado = null;
		try {
			perfilCadastrado = this.getRole(perfil.getDescricao());
			if(perfil.getId() == null) {
				isPerfilJaCadastrado = perfilCadastrado != null ? true : false; 
			} else {
				if(perfil.getId().equals(perfilCadastrado.getId())) {
					isPerfilJaCadastrado = false;
				}
				if(!perfil.getId().equals(perfilCadastrado.getId()) && 
						perfilCadastrado.getDescricao().equals(perfil.getDescricao())) {
					isPerfilJaCadastrado = true;
				} 
			}
		} catch (Exception e) {
			if(AjudanteMensagemExcecao.isResultadoRetornaMaisDeUmElemento(e.toString())) {
				isPerfilJaCadastrado = true;
			}
		}
		return isPerfilJaCadastrado;
	}
	
	public boolean deletar(Perfil perfilSelecionado) {
		try {
			Perfil perfilPesq = this.pesquisarPorId(perfilSelecionado.getId());
			perfilJpaRepository.delete(perfilPesq);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}