package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.form.LogForm;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class LogAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public final LogForm logForm;
	public final LogService logService;
	public final AjudanteProjeto ajudanteProjeto;
	

	public LogAction(@Autowired LogForm logForm,
			@Autowired LogService logService,
			@Autowired AjudanteProjeto ajudanteProjeto) {
		this.logForm = logForm;
		this.logService = logService;
		this.ajudanteProjeto = ajudanteProjeto;
	}

	public void pesquisar() {
		try {
			logForm.setListaLog(logService.pesquisarTodos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selecionar(Log logSelecionado) {
		try {
			logForm.setLogSelecionado(
					logService.pesquisarPorId(logSelecionado.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String init() {
		if(!ajudanteProjeto.isNovaRequisicaoAjax()) {
			this.limparPesquisa();
			this.pesquisar();
			return "/paginas/painel_log/painel_log?faces-redirect=true";
		}
		return "";
	}

	public void limparPesquisa() {
		logForm.setListaLog(null);
		logForm.setListaFiltradaLog(null);
	}

}