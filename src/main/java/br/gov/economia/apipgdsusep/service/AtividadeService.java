package br.gov.economia.apipgdsusep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Atividade;
import br.gov.economia.apipgdsusep.repository.AtividadeJpaRepository;

@Service
@Transactional(transactionManager = "pgdTransactionManager")
public class AtividadeService {
	
	@Autowired
	private AtividadeJpaRepository atividadeJpaRepository;

	public Atividade pesquisarPorId(Integer id) {
		try {
			return atividadeJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Atividade> pesquisarTodos() {
		return atividadeJpaRepository.findAll();
	}

}