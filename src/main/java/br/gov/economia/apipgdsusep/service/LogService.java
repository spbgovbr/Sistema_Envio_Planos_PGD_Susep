package br.gov.economia.apipgdsusep.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.repository.LogJpaRepository;

@Service
@Transactional(transactionManager = "transactionManager")
public class LogService {
	
	@Autowired
	private LogJpaRepository logJpaRepository;


	public Log pesquisarPorId(Long id) {
		try {
			return logJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Log> pesquisarTodos() {
		return logJpaRepository.findAll();
	}

	public List<Log> pesquisarPorFiltros(Log log) {
		Specification<Log> condition = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (log.getUsuario() != null) {
				predicates.add(builder.equal(root.get("usuario"), log.getUsuario()));
			}
			if (log.getDescricaoAcao() != null 
					&& StringUtils.isNotBlank(log.getDescricaoAcao())) {
				predicates.add(builder.like(root.get("descricaoAcao"), "%" + log.getDescricaoAcao() + "%"));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		return logJpaRepository.findAll(condition);
	}
	
	public List<Log> buscarUltimosRegistrosEnviados(ENivelLog nivelAcao, int qtdRegistros) {
		return logJpaRepository.findUltimosRegistrosEnviados(nivelAcao.getDescricao(), qtdRegistros);
	}
	
	public List<Log> buscarRegistrosEnviados(ENivelLog nivelAcao) {
		return logJpaRepository.findRegistrosEnviados(nivelAcao);
	}
	
	public List<Log> buscarUltimosRegistrosEnvioPlanos() {
		return logJpaRepository.findUltimosRegistrosEnvioPlanos();
	}
	
	public List<Log> buscarUltimosRegistrosAgendamento() {
		return logJpaRepository.findUltimosRegistrosAgendamento();
	}
	
	public Log buscarUltimoRegistroCronAgendamento() {
		return logJpaRepository.findUltimoRegistroCronAgendamento();
	}

	public Log salvar(Log log) {
		return logJpaRepository.save(log);
	}

}