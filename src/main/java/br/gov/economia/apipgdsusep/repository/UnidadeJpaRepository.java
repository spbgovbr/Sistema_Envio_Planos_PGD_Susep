package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Unidade;

@Repository
public interface UnidadeJpaRepository extends JpaRepository<Unidade, Integer> {
	
	List<Unidade> findAll(Specification<Unidade> spec);
	
}