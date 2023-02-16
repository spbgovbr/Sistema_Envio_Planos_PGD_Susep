package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.SituacaoPacto;

@Repository
public interface SituacaoPactoJpaRepository extends JpaRepository<SituacaoPacto, Integer> {
	
	List<SituacaoPacto> findAll(Specification<SituacaoPacto> spec);
	
}