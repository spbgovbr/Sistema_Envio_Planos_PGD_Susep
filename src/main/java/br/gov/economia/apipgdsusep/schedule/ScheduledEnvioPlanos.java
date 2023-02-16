package br.gov.economia.apipgdsusep.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.gov.economia.apipgdsusep.dto.PlanoTrabalhoJson;
import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.entity.Pacto;
import br.gov.economia.apipgdsusep.entity.Parametro;
import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.enums.ETipoLog;
import br.gov.economia.apipgdsusep.form.AgendamentoForm;
import br.gov.economia.apipgdsusep.rest.apipgdme.AccessTokenApiPGDME;
import br.gov.economia.apipgdsusep.rest.apipgdme.ApiPGDMEServiceImpl;
import br.gov.economia.apipgdsusep.rest.apipgdme.ResponseJsonApiPGDME;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.service.PactoService;
import br.gov.economia.apipgdsusep.service.ParametroService;
import br.gov.economia.apipgdsusep.service.UsuarioService;
import br.gov.economia.apipgdsusep.utils.Mail;
import br.gov.economia.apipgdsusep.utils.ThreadEnviarEmail;

@Component
public class ScheduledEnvioPlanos {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");
	
	private static final Gson GSON = new GsonBuilder().create();
	
	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PactoService pactoService;
	
	@Autowired
	private ApiPGDMEServiceImpl apiPGDMEServiceImpl;
	
	@Autowired
	private LogService logService;
	
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    
    private ScheduledFuture<?> future;
    
    @Autowired
	private ThreadEnviarEmail threadEnviarEmail;
    
    @Value("${spring.mail.username}")
	private String emailApis;
    
    @Value("${email.envio.destinatarios.to}")
	private String emailEnvioDestinatariosTO;
    
    @Value("${email.envio.destinatarios.cc}")
	private String emailEnvioDestinatariosCC;
    
    
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
    
    public void startCron(Parametro parametro) {
    	if(parametro != null && parametro.getChave().equals("DS_AGENDAMENTO_ENVIO_PLANOS")) {
    		this.changeCron(parametro);
    	}
    }
    
    public void stopCron() {
        if (future != null) {
            future.cancel(true);
        }
    }
    
    public void changeCron(Parametro parametro) {
        stopCron();
        future = threadPoolTaskScheduler.schedule(this.taskEnviarPlanosTrabalho(), new CronTrigger(parametro.getValor()));
    }
	
	public Runnable taskEnviarPlanosTrabalho() {
		return () -> {
			Parametro parametroAgendamento = parametroService.pesquisarPorChave("ST_AGENDAMENTO_ENVIO_PLANOS");
			if(parametroAgendamento != null && parametroAgendamento.getValor().equals(AgendamentoForm.AGENDAMENTO_INICIADO)) {
				this.enviarPlanosTrabalho(true);
			}
		};
	}
	
	public void enviarPlanosTrabalho(boolean isEnviarTodosPlanosTrabalho) {
		boolean error = false;
		int count = 0;
		List<Pacto> listaPacto = null;
		Usuario usuarioSistema = null;
		try {
			usuarioSistema = usuarioService.pesquisarPorEmail("system@gov.br");
			this.gravarLogInicioExecucao(usuarioSistema);
			if(isEnviarTodosPlanosTrabalho) {
				listaPacto = pactoService.pesquisarPorFiltros(new Pacto());
			} else {
				listaPacto = this.getListaPactos(this.buscarRegistrosEnviados(), this.buscarRegistrosAlterados());
			}
			AccessTokenApiPGDME accessToken = GSON.fromJson(apiPGDMEServiceImpl.obterAccessTokenApiPGDME(), AccessTokenApiPGDME.class);
			for(Pacto pacto : listaPacto) {
				StringBuilder jsonSB = new StringBuilder(GSON.toJson(new PlanoTrabalhoJson(pacto)));
				String response = apiPGDMEServiceImpl.criarOuAtualizarPlanoTrabalho(accessToken, pacto.getIdPacto().toString(), jsonSB.toString());
				ResponseJsonApiPGDME responseJsonApiPGDME = ResponseJsonApiPGDME.gerarResponseJsonApiPGDME(response);
				if(responseJsonApiPGDME.getDetail() != null) {
					this.gravarLogExecucao(usuarioSistema, ENivelLog.NIVEL_JSON_API_PG_CADE_PGD_ME_ERRO, responseJsonApiPGDME.toString());
					error = true;
				} else {
					this.gravarLogExecucao(usuarioSistema, ENivelLog.NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO, response);
					error = false;
				}
				count++;
			}
			if(error) {
				throw new Exception("Ocorreu um erro ao enviar os dados. Favor, verificar os logs!");
			} else {
				System.out.println("\n\n==========> QTD. PLANOS: " + count);
				this.gravarLogFimExecucao(usuarioSistema, listaPacto);
				threadEnviarEmail.setMail(this.getEmailSucessoEnvioPlanosTrabalho(listaPacto));
				new Thread(threadEnviarEmail).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.gravarLogExecucao(usuarioSistema, ENivelLog.NIVEL_SISTEMA, e.getMessage());
			threadEnviarEmail.setMail(this.getEmailErroEnvioPlanosTrabalho(e.getMessage()));
			new Thread(threadEnviarEmail).start();
		}
	}
	
	public List<Pacto> getListaPactos(List<String> listaIdPactosEnviados, List<String> listaIdPactosAlterados) {
		List<Pacto> listaPacto = new ArrayList<Pacto>();
		if(listaIdPactosEnviados == null || listaIdPactosEnviados.size() == 0) {
			listaPacto = pactoService.pesquisarPorFiltros(new Pacto());
		} else {
			List<Pacto> listaNovosPactos = pactoService.pesquisarPorFiltrosInMemory(new Pacto(), listaIdPactosEnviados, PactoService.NOT_IN);
			List<Pacto> listaPactosAlterados = pactoService.pesquisarPorFiltrosInMemory(new Pacto(), listaIdPactosAlterados, PactoService.IN);
			if(listaNovosPactos != null && listaNovosPactos.size() > 0) {
				for(Pacto pacto : listaNovosPactos) {
					if(!listaPacto.contains(pacto)) {
						listaPacto.add(pacto);
					}
				}
			}
			if(listaPactosAlterados != null && listaPactosAlterados.size() > 0) {
				for(Pacto pacto : listaPactosAlterados) {
					if(!listaPacto.contains(pacto)) {
						listaPacto.add(pacto);
					}
				}
			}
		}
		return listaPacto;
	}
	
	public List<String> buscarRegistrosEnviados() {
		List<String> listaIdPactosEnviados = new ArrayList<String>();
		List<Log> logsUltimosRegistrosEnviados = logService.
				buscarRegistrosEnviados(ENivelLog.NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO);
		try {
			logsUltimosRegistrosEnviados.stream().forEach(log -> {
				PlanoTrabalhoJson plano = GSON.fromJson(log.getDescricaoAcao(), PlanoTrabalhoJson.class);
				plano.setUsuarioEnvio(log.getUsuario());
				plano.setDataEnvio(log.getDataAcao());
				listaIdPactosEnviados.add(plano.getCod_plano());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaIdPactosEnviados;
	}
	
	public List<String> buscarRegistrosAlterados() {
		List<String> listaIdPactosAlterados = new ArrayList<String>();
		try {
			String valorCronUltimoAgendamento = logService.buscarUltimoRegistroCronAgendamento().getDescricaoAcao();
			//String valorCronUltimoAgendamento = "0 0 0 * * *";
			Calendar dataUltimoAgendamento = Calendar.getInstance();
			/*
			 * Agendamento Diário
			 */
			if(this.getDiaAgendamento(valorCronUltimoAgendamento).equals("*") &&
					this.getMesAgendamento(valorCronUltimoAgendamento).equals("*")) {
				dataUltimoAgendamento.add(Calendar.DATE, -1);
			}
			/*
			 * Agendamento Mensal
			 */
			if(!this.getDiaAgendamento(valorCronUltimoAgendamento).equals("*") &&
					this.getMesAgendamento(valorCronUltimoAgendamento).equals("*")) {
				dataUltimoAgendamento.set(Calendar.DATE, 
						Integer.parseInt(this.getDiaAgendamento(valorCronUltimoAgendamento)));
				dataUltimoAgendamento.add(Calendar.MONTH, - 1);
			}
			/*
			 * Agendamento Anual
			 */
			if(this.getDiaAgendamento(valorCronUltimoAgendamento).equals("*") &&
					!this.getMesAgendamento(valorCronUltimoAgendamento).equals("*")) {
				dataUltimoAgendamento.set(Calendar.MONTH, 
						(Integer.parseInt(this.getMesAgendamento(valorCronUltimoAgendamento)) - 1));
				dataUltimoAgendamento.add(Calendar.YEAR, -1);
			}
			/*
			 * Agendamento Anual
			 */
			if(!this.getDiaAgendamento(valorCronUltimoAgendamento).equals("*") &&
					!this.getMesAgendamento(valorCronUltimoAgendamento).equals("*")) {
				dataUltimoAgendamento.set(Calendar.DATE, 
						Integer.parseInt(this.getDiaAgendamento(valorCronUltimoAgendamento)));
				dataUltimoAgendamento.set(Calendar.MONTH, 
						(Integer.parseInt(this.getMesAgendamento(valorCronUltimoAgendamento)) - 1));
				dataUltimoAgendamento.add(Calendar.YEAR, - 1);
			}
			String valorCronAgendamentoAtual = parametroService.pesquisarPorChave("DS_AGENDAMENTO_ENVIO_PLANOS").getValor();
			//String valorCronAgendamentoAtual = "0 0 0 * * *";
			Calendar dataAgendamentoAtual = Calendar.getInstance();
			/*
			 * Agendamento Diário
			 */
			if(this.getDiaAgendamento(valorCronAgendamentoAtual).equals("*") &&
					this.getMesAgendamento(valorCronAgendamentoAtual).equals("*")) {
				dataAgendamentoAtual = Calendar.getInstance();
			}
			/*
			 * Agendamento Mensal
			 */
			if(!this.getDiaAgendamento(valorCronAgendamentoAtual).equals("*") &&
					this.getMesAgendamento(valorCronAgendamentoAtual).equals("*")) {
				dataAgendamentoAtual.set(Calendar.DATE, 
						Integer.parseInt(this.getDiaAgendamento(valorCronAgendamentoAtual)));
			}
			/*
			 * Agendamento Anual
			 */
			if(this.getDiaAgendamento(valorCronAgendamentoAtual).equals("*") &&
					!this.getMesAgendamento(valorCronAgendamentoAtual).equals("*")) {
				dataAgendamentoAtual.set(Calendar.MONTH, 
						(Integer.parseInt(this.getMesAgendamento(valorCronAgendamentoAtual)) - 1));
			}
			/*
			 * Agendamento Anual
			 */
			if(!this.getDiaAgendamento(valorCronAgendamentoAtual).equals("*") &&
					!this.getMesAgendamento(valorCronAgendamentoAtual).equals("*")) {
				dataAgendamentoAtual.set(Calendar.DATE, 
						Integer.parseInt(this.getDiaAgendamento(valorCronAgendamentoAtual)));
				dataAgendamentoAtual.set(Calendar.MONTH, 
						(Integer.parseInt(this.getMesAgendamento(valorCronAgendamentoAtual)) - 1));
			}
			listaIdPactosAlterados = pactoService.pesquisarIdPactosAlterados(
					dataUltimoAgendamento.getTime(), dataAgendamentoAtual.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaIdPactosAlterados;
	}
	
	public String getDiaAgendamento(String valorCronAgendamento) {
		try {
			String[] cronAgendamento = valorCronAgendamento.split(" ");
			return cronAgendamento[3];
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getMesAgendamento(String valorCronAgendamento) {
		try {
			String[] cronAgendamento = valorCronAgendamento.split(" ");
			return cronAgendamento[4];
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getQtdPlanos(List<Pacto> listaPacto) {
		return (listaPacto != null ? listaPacto.size() : 0);
	}
	
	public int getQtdAtividades(List<Pacto> listaPacto) {
		int countAtividades = 0;
		for(Pacto pact : listaPacto) {
			for(int i = 0; i < pact.getProdutos().size(); i++) {
				countAtividades++;
			}
		}
		return countAtividades;
	}
	
	public void gravarLogInicioExecucao(Usuario usuarioSistema) {
		String log = "O agendamento do envio de planos foi iniciado em " + LocalDateTime.now().format(FORMATTER) + ".";
		logService.salvar(new Log(usuarioSistema, log, new Date(), ETipoLog.AGENDAMENTO, ENivelLog.NIVEL_SISTEMA));
	}
	
	public void gravarLogExecucao(Usuario usuarioSistema, ENivelLog nivelLog, String response) {
		logService.salvar(new Log(usuarioSistema, response, 
				new Date(), ETipoLog.ENVIO_PLANO, nivelLog));
	}
	
	public void gravarLogFimExecucao(Usuario usuarioSistema, List<Pacto> listaPacto) {
		String log = "";
		if(this.getQtdPlanos(listaPacto) == 0) {
			log = "A tarefa foi concluída em " + LocalDateTime.now().format(FORMATTER) + ", mas nenhum plano foi enviado.";
		} else {
			log = "A tarefa foi concluída em " + LocalDateTime.now().format(FORMATTER) + 
					" e foram enviados " + this.getQtdPlanos(listaPacto) + " planos e " + this.getQtdAtividades(listaPacto) + " atividades.";
		}
		logService.salvar(new Log(usuarioSistema, log, new Date(), ETipoLog.AGENDAMENTO, ENivelLog.NIVEL_SISTEMA));
	}
	
	public Mail getEmailSucessoEnvioPlanosTrabalho(List<Pacto> listaPacto) {
		Mail mail = new Mail();
		mail.setFrom(emailApis);
		mail.setRecipientsTO(emailEnvioDestinatariosTO);
		mail.setRecipientsCC(emailEnvioDestinatariosCC);
		mail.setSubject("Sucesso - Envio dos Planos de Trabalho");
		String sucesso = "";
		if(this.getQtdPlanos(listaPacto) == 0) {
			sucesso = "A tarefa foi concluída em " + LocalDateTime.now().format(FORMATTER) + ", mas nenhum plano foi enviado.";
		} else {
			sucesso = "A tarefa foi concluída em " + LocalDateTime.now().format(FORMATTER) + 
					" e foram enviados " + this.getQtdPlanos(listaPacto) + " planos e " + this.getQtdAtividades(listaPacto) + " atividades.";
		}
		Map<String, Object> modelo = new HashMap<>();
		modelo.put("mensagem", sucesso);
		mail.setModelo(modelo);
		mail.setTemplate("/email/email-sucesso-envio-planos-trabalho");
		return mail;
	}
	
	public Mail getEmailErroEnvioPlanosTrabalho(String erro) {
		Mail mail = new Mail();
		mail.setFrom(emailApis);
		mail.setRecipientsTO(emailEnvioDestinatariosTO);
		mail.setRecipientsCC(emailEnvioDestinatariosCC);
		mail.setSubject("Erro - Envio dos Planos de Trabalho");
		Map<String, Object> modelo = new HashMap<>();
		modelo.put("mensagem", erro);
		mail.setModelo(modelo);
		mail.setTemplate("/email/email-erro-envio-planos-trabalho");
		return mail;
	}
	
}