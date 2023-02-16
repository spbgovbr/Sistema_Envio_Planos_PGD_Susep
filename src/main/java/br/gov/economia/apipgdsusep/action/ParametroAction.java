package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.entity.Parametro;
import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.enums.ETipoLog;
import br.gov.economia.apipgdsusep.form.ParametroForm;
import br.gov.economia.apipgdsusep.schedule.ScheduledEnvioPlanos;
import br.gov.economia.apipgdsusep.security.UsuarioLogado;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.service.ParametroService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class ParametroAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public final ParametroForm parametroForm;
	public final ParametroService parametroService;
	public final LogService logService;
	
	@Autowired
	private ScheduledEnvioPlanos scheduledEnvioPlanos;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	@Autowired
	private UsuarioLogado usuarioLogado;
	

	public ParametroAction(@Autowired ParametroForm parametroForm,
			@Autowired ParametroService parametroService,
			@Autowired LogService logService) {
		this.parametroForm = parametroForm;
		this.parametroService = parametroService;
		this.logService = logService;
	}

	public void pesquisar() {
		try {
			parametroForm.setListaParametro(parametroService.pesquisarTodos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvar(RowEditEvent event) {
		try {
			Parametro parametro = (Parametro) event.getObject();
			parametro = parametroService.salvar(parametro);
			logService.salvar(new Log(usuarioLogado.getUsuario(), parametro.getValor(), 
					new Date(), ETipoLog.LOG_CRON_AGENDAMENTO, ENivelLog.NIVEL_USUARIO));
			scheduledEnvioPlanos.startCron(parametro);
			//scheduledEnvioPlanos.agendarEnvioPlanosTrabalho(parametro);
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
					"Informação!", "Os dados foram salvos com sucesso.");
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public void selecionar(Parametro parametroSelecionado) {
		try {
			parametroForm.setParametroSelecionado(
					parametroService.pesquisarPorChave(parametroSelecionado.getChave()));
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public String init() {
		if(!ajudanteProjeto.isNovaRequisicaoAjax()) {
			this.limparPesquisa();
			this.pesquisar();
			return "/paginas/painel_parametro/painel_parametro?faces-redirect=true";
		}
		return "";
	}

	public void limparPesquisa() {
		parametroForm.setListaParametro(null);
		parametroForm.setListaFiltradaParametro(null);
	}

}