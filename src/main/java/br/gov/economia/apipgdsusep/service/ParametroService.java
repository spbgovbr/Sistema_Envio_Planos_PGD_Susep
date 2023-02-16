package br.gov.economia.apipgdsusep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Parametro;
import br.gov.economia.apipgdsusep.repository.ParametroJpaRepository;

@Service
@Transactional(transactionManager = "transactionManager")
public class ParametroService {
	
	@Autowired
	private ParametroJpaRepository parametroJpaRepository;


	public Parametro pesquisarPorChave(String chave) {
		try {
			return parametroJpaRepository.findByChave(chave);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Parametro> pesquisarTodos() {
		return parametroJpaRepository.findAll();
	}

	public Parametro salvar(Parametro parametro) {
		return parametroJpaRepository.save(parametro);
	}
	
	public void deletar(Parametro parametro) {
		parametro = this.pesquisarPorChave(parametro.getChave());
		parametroJpaRepository.delete(parametro);
	}

}