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

import br.gov.economia.apipgdsusep.entity.Funcao;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.repository.FuncaoJpaRepository;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagemExcecao;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;

@Service
@Transactional(transactionManager = "transactionManager")
public class FuncaoService {
	
	@Autowired
	private FuncaoJpaRepository funcaoJpaRepository;

	@Autowired
	private AjudanteMensagens ajudanteMensagens;


	public Funcao pesquisarPorId(Long id) {
		try {
			return funcaoJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Funcao pesquisarPorDescricao(String descricao) {
		try {
			Optional<Funcao> funcao = funcaoJpaRepository.findByDescricao(descricao);
			return funcao.get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Funcao> pesquisarTodas() {
		return funcaoJpaRepository.findAll();
	}

	public List<Funcao> pesquisarPorFiltros(Funcao funcao) {
		Specification<Funcao> condition = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (funcao.getDescricao() != null 
					&& StringUtils.isNotBlank(funcao.getDescricao())) {
				predicates.add(builder.like(root.get("descricao"), "%" + funcao.getDescricao() + "%"));
			}
			if (funcao.getDetalhe() != null 
					&& StringUtils.isNotBlank(funcao.getDetalhe())) {
				predicates.add(builder.like(root.get("detalhe"), "%" + funcao.getDetalhe() + "%"));
			}
			if (funcao.getPaginaAcesso() != null) {
				predicates.add(builder.equal(root.get("paginaAcesso"), funcao.getPaginaAcesso()));
			}
			if (funcao.getSituacaoRegistro() != null) {
				predicates.add(builder.equal(root.get("situacaoRegistro"), funcao.getSituacaoRegistro()));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		return funcaoJpaRepository.findAll(condition);
	}

	public boolean ativarDesativar(Funcao funcaoSelecionada) {
		Funcao funcaoPesquisada = null;
		try {
			funcaoPesquisada = this.pesquisarPorId(funcaoSelecionada.getId());
			funcaoPesquisada.setSituacaoRegistro(funcaoSelecionada.
					getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO) ? ESituacaoRegistro.INATIVO : ESituacaoRegistro.ATIVO);
			funcaoPesquisada = funcaoJpaRepository.save(funcaoPesquisada);
			return true;
		} catch (Exception e)  {
			e.printStackTrace();
			return false;
		}
	}
	
	public Funcao salvar(Funcao funcao) {
		if(isDadosValidos(funcao)) {
			return funcaoJpaRepository.save(funcao);
		}
		return null;
	}

	public boolean isDadosValidos(Funcao funcao) {
		if(!isCamposObrigatorioPreenchidos(funcao))
			return false;
		if(isFuncaoCadastrado(funcao))
			return false;
		return true;
	}

	public boolean isCamposObrigatorioPreenchidos(Funcao funcao) {
		boolean isCamposPreenchidos = funcao.isCamposObrigatoriosPreenchidos();
		if(!isCamposPreenchidos) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Os campos obrigatório não foram preenchidos.");
		}
		return isCamposPreenchidos;
	}

	public boolean isFuncaoCadastrado(Funcao funcao) {
		if (this.isFuncaoJaCadastrado(funcao)) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Já existe um Função cadastrada com os dados informados.");
			return true;
		}
		return false;
	}

	public boolean isFuncaoJaCadastrado(Funcao funcao) {
		boolean isFuncaoJaCadastrada = false;
		Funcao funcaoCadastrada = null;
		try {
			funcaoCadastrada = this.pesquisarPorDescricao(funcao.getDescricao());
			if(funcao.getId() == null) {
				isFuncaoJaCadastrada = funcaoCadastrada != null ? true : false; 
			} else {
				if(funcao.getId().equals(funcaoCadastrada.getId())) {
					isFuncaoJaCadastrada = false;
				}
				if(!funcao.getId().equals(funcaoCadastrada.getId()) && 
						funcaoCadastrada.getDescricao().equals(funcao.getDescricao())) {
					isFuncaoJaCadastrada = true;
				} 
			}
		} catch (Exception e) {
			if(AjudanteMensagemExcecao.isResultadoRetornaMaisDeUmElemento(e.toString())) {
				isFuncaoJaCadastrada = true;
			}
		}
		return isFuncaoJaCadastrada;
	}
	
	public boolean deletar(Funcao funcaoSelecionada) {
		try {
			Funcao funcaoPesq = this.pesquisarPorId(funcaoSelecionada.getId());
			funcaoJpaRepository.delete(funcaoPesq);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}