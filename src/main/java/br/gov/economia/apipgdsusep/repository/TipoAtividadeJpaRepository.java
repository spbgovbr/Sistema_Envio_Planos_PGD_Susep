package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.TipoAtividade;

@Repository
public interface TipoAtividadeJpaRepository extends JpaRepository<TipoAtividade, Integer> {
	
	List<TipoAtividade> findAll(Specification<TipoAtividade> spec);
	
}