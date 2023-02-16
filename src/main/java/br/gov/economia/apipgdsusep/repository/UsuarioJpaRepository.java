package br.gov.economia.apipgdsusep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Usuario;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {
	
	List<Usuario> findAll(Specification<Usuario> spec);
	
	Optional<Usuario> findByEmail(String email);
	
	@Query("SELECT u FROM Usuario u WHERE (u.email = :EMAIL OR u.pessoa.numeroCPF = :CPF)")
	Usuario findByEmailOrNumeroCPF(@Param("EMAIL") String email, @Param("CPF") String numeroCPF);
	
}