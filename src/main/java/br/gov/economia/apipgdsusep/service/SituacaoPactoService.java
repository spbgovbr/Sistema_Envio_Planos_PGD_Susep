package br.gov.economia.apipgdsusep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.SituacaoPacto;
import br.gov.economia.apipgdsusep.repository.SituacaoPactoJpaRepository;

@Service
@Transactional(transactionManager = "pgdTransactionManager")
public class SituacaoPactoService {
	
	@Autowired
	private SituacaoPactoJpaRepository situacaoPactoJpaRepository;

	public SituacaoPacto pesquisarPorId(Integer id) {
		try {
			return situacaoPactoJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<SituacaoPacto> pesquisarTodos() {
		return situacaoPactoJpaRepository.findAll();
	}

}