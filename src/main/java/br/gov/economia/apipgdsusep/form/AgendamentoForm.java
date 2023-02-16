package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.entity.Parametro;
import br.gov.economia.apipgdsusep.enums.EDias;
import br.gov.economia.apipgdsusep.enums.EDiasSemana;
import br.gov.economia.apipgdsusep.enums.EHoras;
import br.gov.economia.apipgdsusep.enums.EMeses;
import br.gov.economia.apipgdsusep.enums.EMinutos;
import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.enums.ETipoLog;
import br.gov.economia.apipgdsusep.schedule.ScheduledEnvioPlanos;
import br.gov.economia.apipgdsusep.security.UsuarioLogado;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.service.ParametroService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;

@Component
@SessionScope
public class AgendamentoForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String AGENDAMENTO_PARADO = "0";
	public static final String AGENDAMENTO_INICIADO = "1";
	
	public static final int MINUTOS_AGENDAMENTO = 1;
	public static final int HORAS_AGENDAMENTO = 2;
	public static final int DIA_AGENDAMENTO = 3;
	public static final int MES_AGENDAMENTO = 4;
	public static final int DIA_SEMANA_AGENDAMENTO = 5;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private ScheduledEnvioPlanos scheduledEnvioPlanos;
	
	@Autowired
	private UsuarioLogado usuarioLogado;
	
	
	private Parametro parametroAgendamento;
	private Parametro parametroStatusAgendamento;
	private String agendamento;
	private EMinutos minutosAgendamento;
	private EHoras horasAgendamento;
	private EDias diaAgendamento;
	private EMeses mesAgendamento;
	private EDiasSemana diaSemanaAgendamento;
	
	private List<Log> listaUltimosRegistrosAgendamento;
	
	
	public Parametro getParametroAgendamento() {
		if(parametroAgendamento == null) {
			parametroAgendamento = parametroService.pesquisarPorChave("DS_AGENDAMENTO_ENVIO_PLANOS");
		}
		return parametroAgendamento;
	}
	public void setParametroAgendamento(Parametro parametroAgendamento) {
		this.parametroAgendamento = parametroAgendamento;
	}
	
	public Parametro getParametroStatusAgendamento() {
		if(parametroAgendamento == null) {
			parametroAgendamento = parametroService.pesquisarPorChave("ST_AGENDAMENTO_ENVIO_PLANOS");
		}
		return parametroStatusAgendamento;
	}
	public void setParametroStatusAgendamento(Parametro parametroStatusAgendamento) {
		this.parametroStatusAgendamento = parametroStatusAgendamento;
	}
	
	public String getAgendamento() {
		if(agendamento == null) {
			agendamento = parametroAgendamento.getValor();
		}
		return agendamento;
	}
	public void setAgendamento(String agendamento) {
		this.agendamento = agendamento;
	}
	
	public EMinutos getMinutosAgendamento() {
		return minutosAgendamento;
	}
	public void setMinutosAgendamento(EMinutos minutosAgendamento) {
		this.minutosAgendamento = minutosAgendamento;
	}
	
	public EHoras getHorasAgendamento() {
		return horasAgendamento;
	}
	public void setHorasAgendamento(EHoras horasAgendamento) {
		this.horasAgendamento = horasAgendamento;
	}
	
	public EDias getDiaAgendamento() {
		return diaAgendamento;
	}
	public void setDiaAgendamento(EDias diaAgendamento) {
		this.diaAgendamento = diaAgendamento;
	}
	
	public EMeses getMesAgendamento() {
		return mesAgendamento;
	}
	public void setMesAgendamento(EMeses mesAgendamento) {
		this.mesAgendamento = mesAgendamento;
	}
	
	public EDiasSemana getDiaSemanaAgendamento() {
		return diaSemanaAgendamento;
	}
	public void setDiaSemanaAgendamento(EDiasSemana diaSemanaAgendamento) {
		this.diaSemanaAgendamento = diaSemanaAgendamento;
	}
	
	public List<Log> getListaUltimosRegistrosAgendamento() {
		return listaUltimosRegistrosAgendamento;
	}
	public void setListaUltimosRegistrosAgendamento(List<Log> listaUltimosRegistrosAgendamento) {
		this.listaUltimosRegistrosAgendamento = listaUltimosRegistrosAgendamento;
	}
	
	public List<EMinutos> getEnumMinutosAgendamento() {
		return Arrays.asList(EMinutos.values());
	}
	
	public List<EHoras> getEnumHorasAgendamento() {
		return Arrays.asList(EHoras.values());
	}
	
	public List<EDias> getEnumDiasAgendamento() {
		return Arrays.asList(EDias.values());
	}
	
	public List<EMeses> getEnumMesesAgendamento() {
		return Arrays.asList(EMeses.values());
	}
	
	public List<EDiasSemana> getEnumDiasSemanaAgendamento() {
		return Arrays.asList(EDiasSemana.values());
	}
	
	
	public void init() {
		this.setMinutosAgendamento(EMinutos.fromMinuto(this.getParamAgendamento(MINUTOS_AGENDAMENTO)));
		this.setHorasAgendamento(EHoras.fromHora(this.getParamAgendamento(HORAS_AGENDAMENTO)));
		this.setDiaAgendamento(EDias.fromDia(this.getParamAgendamento(DIA_AGENDAMENTO)));
		this.setMesAgendamento(EMeses.fromMes(this.getParamAgendamento(MES_AGENDAMENTO)));
		this.setDiaSemanaAgendamento(EDiasSemana.fromDiaSemana(this.getParamAgendamento(DIA_SEMANA_AGENDAMENTO)));
	}
	
	public String atualizarAgendamento() {
		String agendamento = "";
		try {
			agendamento += "0 ";
			agendamento += this.getMinutosAgendamento().getValor() + " ";
			agendamento += this.getHorasAgendamento().getValor() + " ";
			agendamento += this.getDiaAgendamento().getValor() + " ";
			agendamento += this.getMesAgendamento().getValor() + " ";
			agendamento += (this.getDiaSemanaAgendamento() != null ? this.getDiaSemanaAgendamento().getValor() : "*");
		} catch (Exception e) {
			agendamento = "0 0 0 15 * *";
		}
		return agendamento;
	}
	
	public String getParamAgendamento(int parametro) {
		String param = "";
		try {
			String[] params = this.getParametroAgendamento().getValor().split(" ");
			param = params[parametro];
		} catch (Exception e) {
			param = "";
		}
		return param;
	}
	
	public void pararAgendamento() {
		try {
			this.getParametroStatusAgendamento().setValor("0");
			parametroService.salvar(this.getParametroStatusAgendamento());
			scheduledEnvioPlanos.stopCron();
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
					"Informação!", "Os dados foram salvos com sucesso.");
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public void iniciarAgendamento() {
		try {
			this.getParametroStatusAgendamento().setValor(AGENDAMENTO_INICIADO);
			parametroService.salvar(this.getParametroStatusAgendamento());
			this.getParametroAgendamento().setValor(this.atualizarAgendamento());
			parametroService.salvar(this.getParametroAgendamento());
			logService.salvar(new Log(usuarioLogado.getUsuario(), this.getParametroAgendamento().getValor(), 
					new Date(), ETipoLog.LOG_CRON_AGENDAMENTO, ENivelLog.NIVEL_USUARIO));
			scheduledEnvioPlanos.startCron(this.getParametroAgendamento());
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
					"Informação!", "Os dados foram salvos com sucesso.");
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public String enviarTodosPlanosTrabalho() {
		try {
			scheduledEnvioPlanos.enviarPlanosTrabalho(true);
			return "/admin/painel_agendamento/painel_agendamento?faces-redirect=true";
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
			return "";
		}
		
	}

}