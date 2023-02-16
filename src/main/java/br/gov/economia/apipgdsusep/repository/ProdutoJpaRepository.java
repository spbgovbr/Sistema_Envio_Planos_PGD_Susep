package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Produto;

@Repository
public interface ProdutoJpaRepository extends JpaRepository<Produto, String> {
	
	List<Produto> findAll(Specification<Produto> spec);
	
}