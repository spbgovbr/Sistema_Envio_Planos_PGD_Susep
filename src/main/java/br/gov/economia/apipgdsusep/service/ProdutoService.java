package br.gov.economia.apipgdsusep.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Produto;
import br.gov.economia.apipgdsusep.repository.ProdutoJpaRepository;

@Service
@Transactional(transactionManager = "pgdTransactionManager")
public class ProdutoService {
	
	@Autowired
	private ProdutoJpaRepository produtoJpaRepository;

	public Produto pesquisarPorId(String id) {
		try {
			return produtoJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Produto> pesquisarTodos() {
		return produtoJpaRepository.findAll();
	}

	public List<Produto> pesquisarPorFiltros(Produto produto) {
		Specification<Produto> condition = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (produto.getAvaliacao() != null) {
				predicates.add(builder.equal(root.get("avaliacao"), produto.getAvaliacao()));
			}
			if (produto.getDataAvaliacao() != null) {
				predicates.add(builder.equal(root.get("dataAvaliacao"), produto.getDataAvaliacao()));
			}
			if (produto.getEntregaEsperada() != null) {
				predicates.add(builder.equal(root.get("entregaEsperada"), produto.getEntregaEsperada()));
			}
			if (produto.getFaixaComplexidade() != null) {
				predicates.add(builder.equal(root.get("faixaComplexidade"), produto.getFaixaComplexidade()));
			}
			if (produto.getJustificativa() != null) {
				predicates.add(builder.like(root.get("justificativa"), "%" + produto.getJustificativa() + "%"));
			}
			if (produto.getNomeAtividade() != null) {
				predicates.add(builder.like(root.get("nomeAtividade"), "%" + produto.getNomeAtividade() + "%"));
			}
			if (produto.getNomeGrupoAtividade() != null) {
				predicates.add(builder.like(root.get("nomeGrupoAtividade"), "%" + produto.getNomeGrupoAtividade() + "%"));
			}
			if (produto.getPacto() != null) {
				predicates.add(builder.equal(root.get("pacto"), produto.getPacto()));
			}
			if (produto.getParametrosComplexidade() != null) {
				predicates.add(builder.equal(root.get("parametrosComplexidade"), produto.getParametrosComplexidade()));
			}
			if (produto.getQuantidadeEntregas() != null) {
				predicates.add(builder.equal(root.get("quantidadeEntregas"), produto.getQuantidadeEntregas()));
			}
			if (produto.getQuantidadeEntregasEfetivas() != null) {
				predicates.add(builder.equal(root.get("quantidadeEntregasEfetivas"), produto.getQuantidadeEntregasEfetivas()));
			}
			if (produto.getTempoPresencialEstimado() != null) {
				predicates.add(builder.equal(root.get("tempoPresencialEstimado"), produto.getTempoPresencialEstimado()));
			}
			if (produto.getTempoPresencialProgramado() != null) {
				predicates.add(builder.equal(root.get("tempoPresencialProgramado"), produto.getTempoPresencialProgramado()));
			}
			if (produto.getTempoPresencialExecutado() != null) {
				predicates.add(builder.equal(root.get("tempoPresencialExecutado"), produto.getTempoPresencialEstimado()));
			}
			if (produto.getTempoTeletrabalhoEstimado() != null) {
				predicates.add(builder.equal(root.get("tempoTeletrabalhoEstimado"), produto.getTempoTeletrabalhoEstimado()));
			}
			if (produto.getTempoTeletrabalhoProgramado() != null) {
				predicates.add(builder.equal(root.get("tempoTeletrabalhoProgramado"), produto.getTempoTeletrabalhoProgramado()));
			}
			if (produto.getTempoTeletrabalhoExecutado() != null) {
				predicates.add(builder.equal(root.get("tempoTeletrabalhoExecutado"), produto.getTempoTeletrabalhoExecutado()));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		return produtoJpaRepository.findAll(condition);
	}

}