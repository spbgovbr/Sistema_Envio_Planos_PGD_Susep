package br.gov.economia.apipgdsusep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Funcao;

@Repository
public interface FuncaoJpaRepository extends JpaRepository<Funcao, Long> {
	
	List<Funcao> findAll(Specification<Funcao> spec);
	
	Optional<Funcao> findByDescricao(String descricao);
	
}