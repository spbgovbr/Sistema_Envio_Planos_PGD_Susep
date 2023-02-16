package br.gov.economia.apipgdsusep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Perfil;

@Repository
public interface PerfilJpaRepository extends JpaRepository<Perfil, Long> {
	
	List<Perfil> findAll(Specification<Perfil> spec);
	
	Optional<Perfil> findByDescricao(String rolePerfil);
	
}