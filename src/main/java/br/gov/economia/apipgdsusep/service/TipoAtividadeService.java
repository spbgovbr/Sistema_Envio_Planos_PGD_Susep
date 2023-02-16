package br.gov.economia.apipgdsusep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.TipoAtividade;
import br.gov.economia.apipgdsusep.repository.TipoAtividadeJpaRepository;

@Service
@Transactional(transactionManager = "pgdTransactionManager")
public class TipoAtividadeService {
	
	@Autowired
	private TipoAtividadeJpaRepository tipoAtividadeJpaRepository;

	public TipoAtividade pesquisarPorId(Integer id) {
		try {
			return tipoAtividadeJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<TipoAtividade> pesquisarTodos() {
		return tipoAtividadeJpaRepository.findAll();
	}

}