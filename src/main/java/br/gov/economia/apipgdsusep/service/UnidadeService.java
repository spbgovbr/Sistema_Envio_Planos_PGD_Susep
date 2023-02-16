package br.gov.economia.apipgdsusep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Unidade;
import br.gov.economia.apipgdsusep.repository.UnidadeJpaRepository;

@Service
@Transactional(transactionManager = "pgdTransactionManager")
public class UnidadeService {
	
	@Autowired
	private UnidadeJpaRepository unidadeJpaRepository;

	public Unidade pesquisarPorId(Integer id) {
		try {
			return unidadeJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Unidade> pesquisarTodos() {
		return unidadeJpaRepository.findAll();
	}

}