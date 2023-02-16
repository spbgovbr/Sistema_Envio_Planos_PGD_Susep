package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Atividade;

@Repository
public interface AtividadeJpaRepository extends JpaRepository<Atividade, Integer> {
	
	List<Atividade> findAll(Specification<Atividade> spec);
	
}