package br.gov.economia.apipgdsusep.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.enums.ENivelLog;

@Repository
public interface LogJpaRepository extends JpaRepository<Log, Long> {
	
	List<Log> findAll(Specification<Log> spec);
	
	@Query(nativeQuery = true, value = "SELECT TOP :QTD_REGISTROS * FROM tb_log WHERE ds_nivel_acao = :NIVEL_ACAO ORDER BY dt_acao DESC")
	List<Log> findUltimosRegistrosEnviados(@Param("NIVEL_ACAO") String nivelAcao, @Param("QTD_REGISTROS") int qtdRegistros);
	
	@Query(value = "SELECT l FROM Log l WHERE l.nivelAcao = :NIVEL_ACAO ORDER BY l.dataAcao DESC")
	List<Log> findRegistrosEnviados(@Param("NIVEL_ACAO") ENivelLog nivelAcao);
	
	@Query(nativeQuery = true, value = "SELECT TOP 2 * FROM tb_log WHERE ds_tipo_acao in ('AGENDAMENTO', 'LOG_ENVIO_PLANO') ORDER BY dt_acao DESC")
	List<Log> findUltimosRegistrosEnvioPlanos();
	
	@Query(nativeQuery = true, value = "SELECT TOP 2 * FROM tb_log WHERE ds_tipo_acao = 'AGENDAMENTO' ORDER BY dt_acao DESC")
	List<Log> findUltimosRegistrosAgendamento();
	
	@Query(nativeQuery = true, value = "SELECT TOP 1 * FROM tb_log WHERE ds_tipo_acao = 'LOG_CRON_AGENDAMENTO' ORDER BY dt_acao DESC")
	Log findUltimoRegistroCronAgendamento();
	
}