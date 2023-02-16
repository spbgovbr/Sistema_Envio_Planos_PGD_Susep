package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Parametro;

@Repository
public interface ParametroJpaRepository extends JpaRepository<Parametro, String> {
	
	List<Parametro> findAll(Specification<Parametro> spec);
	
	@Query(value = "SELECT p FROM Parametro p WHERE p.chave = :CHAVE")
	Parametro findByChave(@Param("CHAVE") String chave);
	
}