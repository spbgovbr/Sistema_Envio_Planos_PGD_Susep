package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Pessoa;

@Repository
public interface PessoaJpaRepository extends JpaRepository<Pessoa, Long> {

	List<Pessoa> findAll(Specification<Pessoa> spec);
	
	@Query("SELECT p FROM Pessoa p WHERE p.numeroCPF = :CPF")
	Pessoa findByCPF(@Param("CPF") String cpf);
	
}