package br.gov.economia.apipgdsusep.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Pacto;

@Repository
public interface PactoJpaRepository extends JpaRepository<Pacto, String> {
	
	List<Pacto> findAll(Specification<Pacto> spec);
	
	@Query(nativeQuery = true, value = "SELECT IdTabela FROM dbo.GerarLogAlteracaoPacto(:DT_INICIO, :DT_FIM) go")
	List<String> findIdPactosAlterados(@Param("DT_INICIO") Date dataInicio, @Param("DT_FIM") Date dataFim);
	
}