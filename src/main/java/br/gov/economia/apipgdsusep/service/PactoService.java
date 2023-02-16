package br.gov.economia.apipgdsusep.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Pacto;
import br.gov.economia.apipgdsusep.entity.Produto;
import br.gov.economia.apipgdsusep.repository.PactoJpaRepository;

@Service
@Transactional(transactionManager = "pgdTransactionManager")
public class PactoService {
	
	public static final int NOT_IN = 0;
	public static final int IN = 1;

	@Autowired
	private PactoJpaRepository pactoJpaRepository;

	public Pacto pesquisarPorId(String id) {
		Pacto pacto = null;
		try {
			pacto = pactoJpaRepository.findById(id).get();
			pacto.getProdutos().size();
			return pacto;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Pacto> pesquisarTodos() {
		return pactoJpaRepository.findAll();
	}
	
	public List<String> pesquisarIdPactosAlterados(Date dataInicio, Date dataFim) {
		return pactoJpaRepository.findIdPactosAlterados(dataInicio, dataFim);
	}
	
	public List<Pacto> pesquisarPorFiltros(Pacto pacto) {
		Specification<Pacto> condition = (root, query, builder) -> {
			List<Predicate> predicates = this.getPredicates(pacto, root, query, builder);
			Predicate[] ps = new Predicate[predicates.size()];
			query.distinct(true);
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		List<Pacto> listaPacto = pactoJpaRepository.findAll(condition);
		for(Pacto p : listaPacto) {
			p.getProdutos().size();
		}
		return listaPacto;
	}
	
	public List<Pacto> pesquisarPorFiltrosInMemory(Pacto pacto, List<String> listaIdPactos, int condInNotIn) {
		Specification<Pacto> condition = (root, query, builder) -> {
			List<Predicate> predicates = this.getPredicates(pacto, root, query, builder);
			Predicate[] ps = new Predicate[predicates.size()];
			query.distinct(true);
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		List<Pacto> listaPacto = pactoJpaRepository.findAll(condition);
		Collections.sort(listaPacto, Pacto.IdComparator);
		Collections.sort(listaIdPactos);
		List<Pacto> novaListaPacto = new ArrayList<Pacto>();
		listPct : for(Pacto p : listaPacto) {
			listEnv : for(@SuppressWarnings("unused") String id : listaIdPactos) {
				if(condInNotIn == IN && listaIdPactos.contains(p.getIdPacto())) {
					p.getProdutos().size();
					novaListaPacto.add(p);
					break listEnv;
				}
				if(condInNotIn == NOT_IN && !listaIdPactos.contains(p.getIdPacto())) {
					p.getProdutos().size();
					novaListaPacto.add(p);
					break listEnv;
				}
				continue listPct;
			}
		}
		return novaListaPacto;
	}
	
	public List<Pacto> pesquisarPorFiltros(Pacto pacto, List<Integer> listaIdRegistrosEnviados, int condInNotIn) {
		Specification<Pacto> condition = (root, query, builder) -> {
			List<Predicate> predicates = this.getPredicates(pacto, root, query, builder);
			if(listaIdRegistrosEnviados != null && listaIdRegistrosEnviados.size() > 0) {
				if(condInNotIn == NOT_IN) {
					predicates.add(builder.in(root.get("idPacto")).value(listaIdRegistrosEnviados).not());
				}
				if(condInNotIn == IN) {
					predicates.add(builder.in(root.get("idPacto")).value(listaIdRegistrosEnviados));
				}
			} else {
				predicates.add(builder.equal(root.get("idPacto"), "-1"));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			query.distinct(true);
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		List<Pacto> listaPacto = pactoJpaRepository.findAll(condition);
		for(Pacto p : listaPacto) {
			p.getProdutos().size();
		}
		return listaPacto;
	}
	
	public List<Predicate> getPredicates(Pacto pacto, 
			Root<Pacto> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<>();
		Join<Pacto, Produto> joinPactoProduto  = root.join("produtos");
		if (pacto.getCargaHorariaSemanal() != null) {
			predicates.add(builder.equal(root.get("cargaHorariaSemanal"), pacto.getCargaHorariaSemanal()));
		}
		if (pacto.getCargaHorariaTotal() != null) {
			predicates.add(builder.equal(root.get("cargaHorariaTotal"), pacto.getCargaHorariaTotal()));
		}
		if (pacto.getCodigoUnidadeExercicio() != null) {
			predicates.add(builder.equal(root.get("codigoUnidadeExercicio"), pacto.getCodigoUnidadeExercicio()));
		}
		if (pacto.getCpf() != null && StringUtils.isNotBlank(pacto.getCpf())) {
			predicates.add(builder.equal(root.get("cpf"), pacto.getCpf()));
		}
		if (pacto.getDataInicialPeriodoInicio() != null && pacto.getDataFinalPeriodoInicio() != null) {
			predicates.add(builder.between(root.get("dataInicio"), pacto.getDataInicialPeriodoInicio(), pacto.getDataFinalPeriodoInicio()));
		}
		if (pacto.getDataInicio() != null) {
			predicates.add(builder.equal(root.get("dataInicio"), pacto.getDataInicio()));
		}
		if (pacto.getDataFim() != null) {
			predicates.add(builder.equal(root.get("dataFim"), pacto.getDataFim()));
		}
		if (pacto.getDataInterrupcao() != null) {
			predicates.add(builder.equal(root.get("dataInterrupcao"), pacto.getDataInterrupcao()));
		}
		if (pacto.getEntregueNoPrazo() != null) {
			predicates.add(builder.equal(root.get("entregueNoPrazo"), pacto.getEntregueNoPrazo()));
		}
		if (pacto.getHorasHomologadas() != null) {
			predicates.add(builder.equal(root.get("horasHomologadas"), pacto.getHorasHomologadas()));
		}
		if (pacto.getMatriculaSiape() != null && StringUtils.isNotBlank(pacto.getMatriculaSiape())) {
			predicates.add(builder.equal(root.get("matriculaSiape"), pacto.getMatriculaSiape()));
		}
		if (pacto.getModalidadeExecucao() != null) {
			predicates.add(builder.equal(root.get("modalidadeExecucao"), pacto.getModalidadeExecucao()));
		}
		if (pacto.getNomeParticipante() != null && StringUtils.isNotBlank(pacto.getNomeParticipante())) {
			predicates.add(builder.like(root.get("nomeParticipante"), "%" + pacto.getNomeParticipante() + "%"));
		}
		if (pacto.getNomeUnidadeExercicio() != null) {
			predicates.add(builder.equal(root.get("nomeUnidadeExercicio"), pacto.getNomeUnidadeExercicio()));
		}
		if (pacto.getSiglaUnidadeExercicio() != null) {
			predicates.add(builder.equal(root.get("siglaUnidadeExercicio"), pacto.getSiglaUnidadeExercicio()));
		}
		if (pacto.getSituacaoPacto() != null) {
			predicates.add(builder.equal(root.get("descricaoSituacaoPacto"), pacto.getSituacaoPacto().getDescricao()));
		}
		if (pacto.getAtividade() != null) {
			predicates.add(builder.equal(joinPactoProduto.get("nomeAtividade"), pacto.getAtividade().getNome()));
		}
		if (pacto.getTipoAtividade() != null) {
			predicates.add(builder.equal(joinPactoProduto.get("faixaComplexidade"), pacto.getTipoAtividade().getFaixaComplexidade()));
		}
		if (pacto.getNotaAvaliacao() != null) {
			predicates.add(builder.equal(joinPactoProduto.get("avaliacao"), pacto.getNotaAvaliacao().getNota()));
		}
		if (pacto.getDataAvaliacao() != null) {
			predicates.add(builder.equal(joinPactoProduto.get("dataAvaliacao"), pacto.getDataAvaliacao()));
		}
		if (pacto.getQuantidadeEntregas() != null) {
			if(pacto.getQuantidadeEntregas().getQuantidade().intValue() <= 10) {
				predicates.add(builder.equal(joinPactoProduto.get("quantidadeEntregas"), 
						pacto.getQuantidadeEntregas().getQuantidade().intValue()));
			} else {
				predicates.add(builder.greaterThanOrEqualTo(joinPactoProduto.get("quantidadeEntregas"), 
						pacto.getQuantidadeEntregas().getQuantidade().intValue()));
			}
		}
		if (pacto.getQuantidadeEntregasEfetivas() != null) {
			if(pacto.getQuantidadeEntregasEfetivas().getQuantidade().intValue() <= 10) {
				predicates.add(builder.equal(joinPactoProduto.get("quantidadeEntregasEfetivas"), 
						pacto.getQuantidadeEntregasEfetivas().getQuantidade().intValue()));
			} else {
				predicates.add(builder.greaterThanOrEqualTo(joinPactoProduto.get("quantidadeEntregasEfetivas"), 
						pacto.getQuantidadeEntregasEfetivas().getQuantidade().intValue()));
			}
		}
		return predicates;
	}

}