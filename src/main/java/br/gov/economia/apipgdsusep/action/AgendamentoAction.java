package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.form.AgendamentoForm;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.service.ParametroService;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class AgendamentoAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public final AgendamentoForm agendamentoForm;
	public final ParametroService parametroService;
	public final LogService logService;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	
	public AgendamentoAction(@Autowired AgendamentoForm agendamentoForm,
			@Autowired ParametroService parametroService,
			@Autowired LogService logService) {
		this.agendamentoForm = agendamentoForm;
		this.parametroService = parametroService;
		this.logService = logService;
	}
	
	public void pesquisar() {
		try {
			agendamentoForm.setParametroAgendamento(parametroService.
					pesquisarPorChave("DS_AGENDAMENTO_ENVIO_PLANOS"));
			agendamentoForm.setParametroStatusAgendamento(parametroService.
					pesquisarPorChave("ST_AGENDAMENTO_ENVIO_PLANOS"));
			agendamentoForm.setListaUltimosRegistrosAgendamento(
					logService.buscarUltimosRegistrosAgendamento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String init() {
		if(!ajudanteProjeto.isNovaRequisicaoAjax()) {
			this.pesquisar();
			agendamentoForm.init();
			return "/admin/painel_agendamento/painel_agendamento?faces-redirect=true";
		}
		return "";
	}
	
}