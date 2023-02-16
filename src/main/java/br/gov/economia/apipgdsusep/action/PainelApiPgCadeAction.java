package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

import javax.faces.application.FacesMessage;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.gov.economia.apipgdsusep.converter.PactoDataModel;
import br.gov.economia.apipgdsusep.dto.PlanoTrabalhoJson;
import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.entity.Pacto;
import br.gov.economia.apipgdsusep.entity.Produto;
import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.enums.ETipoLog;
import br.gov.economia.apipgdsusep.form.ApiPgCadeForm;
import br.gov.economia.apipgdsusep.form.PainelApiPgCadeForm;
import br.gov.economia.apipgdsusep.rest.apipgdme.AccessTokenApiPGDME;
import br.gov.economia.apipgdsusep.rest.apipgdme.ApiPGDMEServiceImpl;
import br.gov.economia.apipgdsusep.rest.apipgdme.ResponseDetailJsonApiPGDME;
import br.gov.economia.apipgdsusep.rest.apipgdme.ResponseJsonApiPGDME;
import br.gov.economia.apipgdsusep.security.UsuarioLogado;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.service.PactoService;
import br.gov.economia.apipgdsusep.service.ProdutoService;
import br.gov.economia.apipgdsusep.task.TarefaValidacaoEnvioDados;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class PainelApiPgCadeAction implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String CRIAR_OU_SUBSTITUIR = "CRIAR_OU_SUBSTITUIR";
	private static final String ATUALIZAR = "ATUALIZAR";
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");
	
	private static final Gson GSON = new GsonBuilder().create();
	
	public final ApiPgCadeForm apiPgCadeForm;
	public final PainelApiPgCadeForm painelApiPgCadeForm;
	public final PactoService pactoService;
	public final ProdutoService produtoService;
	public final LogService logService;
	
	@Autowired
	private UsuarioLogado usuarioLogado;
	
	@Autowired
	private ApiPGDMEServiceImpl apiPGDMEServiceImpl;
	
	@Autowired
	private TarefaValidacaoEnvioDados tarefaValidacaoEnvioDados;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	
	public PainelApiPgCadeAction(@Autowired PainelApiPgCadeForm painelApiPgCadeForm,
			@Autowired ApiPgCadeForm apiPgCadeForm,
			@Autowired PactoService pactoService,
			@Autowired ProdutoService produtoService,
			@Autowired LogService logService) {
		this.apiPgCadeForm = apiPgCadeForm;
		this.painelApiPgCadeForm = painelApiPgCadeForm;
		this.pactoService = pactoService;
		this.produtoService = produtoService;
		this.logService = logService;
	}
	
	public void pesquisarPorFiltro() {
		try {
			painelApiPgCadeForm.setPactoDataModel(new PactoDataModel(
					pactoService.pesquisarPorFiltros(painelApiPgCadeForm.getPactoPesquisa())));
			painelApiPgCadeForm.setSelectAll(false);
			painelApiPgCadeForm.setQtdPactosSelecionados(null);
			apiPgCadeForm.setQtdPlanos(painelApiPgCadeForm.getQtdPlanos());
			apiPgCadeForm.setQtdAtividades(painelApiPgCadeForm.getQtdAtividades());
			PrimeFaces.current().executeScript("PF('dataTableListaPactoWidgetVar').unselectAllRows()");
			PrimeFaces.current().ajax().update("modalDetalheAPIPGCade");
			PrimeFaces.current().executeScript("PF('modalDetalheAPIPGCadeWidgetVar').show()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selecionarPacto(Pacto pactoSelecionado) {
		painelApiPgCadeForm.setPactoSelecionado(pactoSelecionado);
	}
	
	public void selecionarProduto(Produto produtoSelecionado) {
		painelApiPgCadeForm.setProdutoSelecionado(produtoSelecionado);
	}
	
	public void validarEnvioDados() {
		tarefaValidacaoEnvioDados.iniciarTarefasValidacao(painelApiPgCadeForm.getPactoDataModel().iterator());
	}
	
	public void confirmarEnvioDados(boolean isEnviarDados, String metodo) {
		if(isEnviarDados) {
			this.enviarDados(metodo);
		} else {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "O envio dos dados foi cancelado!");
		}
	}
	
	public void enviarDados(String metodo) {
		boolean sucesso = false;
		String response = null;
		try {
			AccessTokenApiPGDME accessToken = GSON.fromJson(apiPGDMEServiceImpl.obterAccessTokenApiPGDME(), AccessTokenApiPGDME.class);
			this.gravarLogInicioExecucao();
			System.out.println("Qtd. Elementos Selecionados: " + painelApiPgCadeForm.getQtdPactosSelecionados());
			Iterator<Pacto> listaPlanosSelecionados = painelApiPgCadeForm.getPactoDataModel().iterator();
			listaPactoSelecionados : while(listaPlanosSelecionados.hasNext()) {
				Pacto pacto = (Pacto) listaPlanosSelecionados.next();
				System.out.println("\n\n=======> CPF: " + pacto.getCpf());
				if(pacto.getSelecionado()) {
					PlanoTrabalhoJson planoTrabalho = new PlanoTrabalhoJson(pacto);
					StringBuilder jsonSB = new StringBuilder(GSON.toJson(planoTrabalho));
					if(metodo.equals(CRIAR_OU_SUBSTITUIR)) {
						response = apiPGDMEServiceImpl.criarOuAtualizarPlanoTrabalho(accessToken, pacto.getIdPacto().toString(), jsonSB.toString());
					}
					if(metodo.equals(ATUALIZAR)) {
						response = apiPGDMEServiceImpl.atualizarPlanoTrabalho(accessToken, pacto.getIdPacto().toString(), jsonSB.toString());
					}
					ResponseJsonApiPGDME responseJsonApiPGDME = ResponseJsonApiPGDME.gerarResponseJsonApiPGDME(response);
					if(responseJsonApiPGDME.getDetail() != null) {
						for(ResponseDetailJsonApiPGDME detail : responseJsonApiPGDME.getDetail()) {
							if(detail.getType().contains("error")) {
								System.out.println("\n\n=======> CPF ERROR: " + pacto.getCpf());
								ajudanteMensagens.exibirErrorResponse(responseJsonApiPGDME);
								sucesso = false;
								break listaPactoSelecionados;
							}
						}
					} else {
						sucesso = true;
						jsonSB = new StringBuilder();
						logService.salvar(new Log(usuarioLogado.getUsuario(), response, 
								new Date(), ETipoLog.ENVIO_PLANO, ENivelLog.NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO));
					}
				}
			}
			if(sucesso) {
				this.gravarLogFimExecucao();
				painelApiPgCadeForm.init();
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
						"Informação!", "Os dados foram enviados com sucesso.");
			} else {
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
						"Alerta!", "Os dados não foram enviados corretamente.");
			}
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public void gravarLogInicioExecucao() {
		String log = "O envio de planos foi iniciado em " + LocalDateTime.now().format(FORMATTER) + ".";
		logService.salvar(new Log(usuarioLogado.getUsuario(), log, new Date(), ETipoLog.LOG_ENVIO_PLANO, ENivelLog.NIVEL_SISTEMA));
	}
	
	public void gravarLogFimExecucao() {
		String log = "";
		if(painelApiPgCadeForm.getQtdPlanos() == 0) {
			log = "A tarefa foi concluída em " + LocalDateTime.now().format(FORMATTER) + ", mas nenhum plano foi enviado.";
		} else {
			log = "A tarefa foi concluída em " + LocalDateTime.now().format(FORMATTER) + 
					" e foram enviados " + painelApiPgCadeForm.getQtdPlanos() + " planos e " + painelApiPgCadeForm.getQtdAtividades() + " atividades.";
		}
		logService.salvar(new Log(usuarioLogado.getUsuario(), log, new Date(), ETipoLog.LOG_ENVIO_PLANO, ENivelLog.NIVEL_SISTEMA));
	}
	
	public String init() {
		if(ajudanteProjeto.isNovaRequisicao()) {
			painelApiPgCadeForm.init();
			return "/admin/painel_api_pg_cade/painel_api_pg_cade.jsf?faces-redirect=true";
		}
		return "";
	}
	
}