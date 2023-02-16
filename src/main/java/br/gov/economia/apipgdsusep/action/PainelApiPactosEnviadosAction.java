package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.entity.Pacto;
import br.gov.economia.apipgdsusep.entity.Produto;
import br.gov.economia.apipgdsusep.form.ApiPgCadeForm;
import br.gov.economia.apipgdsusep.form.PainelApiPactosEnviadosForm;
import br.gov.economia.apipgdsusep.service.PactoService;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class PainelApiPactosEnviadosAction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final ApiPgCadeForm apiPgCadeForm;
	public final PainelApiPactosEnviadosForm painelApiPlanosEnviadosForm;
	
	public final PactoService pactoService;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	
	public PainelApiPactosEnviadosAction(
			@Autowired PainelApiPactosEnviadosForm painelApiPlanosEnviadosForm,
			@Autowired ApiPgCadeForm apiPgCadeForm,
			@Autowired PactoService pactoService) {
		this.apiPgCadeForm = apiPgCadeForm;
		this.painelApiPlanosEnviadosForm = painelApiPlanosEnviadosForm;
		this.pactoService = pactoService;
	}
	
	
	public void pesquisarPorFiltro() {
		try {
			painelApiPlanosEnviadosForm.setListaPactosEnviados(pactoService.pesquisarPorFiltrosInMemory(
					painelApiPlanosEnviadosForm.getPactoPesquisa(), 
					painelApiPlanosEnviadosForm.getListaIdRegistrosEnviados(), 
					PactoService.IN));
			int countAtividades = 0;
			for(Pacto pacto : painelApiPlanosEnviadosForm.getListaPactosEnviados()) {
				for(int i = 0; i < pacto.getProdutos().size(); i++) {
					countAtividades++;
				}
			}
			apiPgCadeForm.setQtdPlanos(painelApiPlanosEnviadosForm.getListaPactosEnviados().size());
			apiPgCadeForm.setQtdAtividades(countAtividades);
			PrimeFaces.current().ajax().update("modalDetalheAPIPGCade");
			PrimeFaces.current().executeScript("PF('modalDetalheAPIPGCadeWidgetVar').show()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selecionarPacto(Pacto pactoSelecionado) {
		painelApiPlanosEnviadosForm.setPactoSelecionado(pactoSelecionado);
	}
	
	public void selecionarProduto(Produto produtoSelecionado) {
		painelApiPlanosEnviadosForm.setProdutoSelecionado(produtoSelecionado);
	}
	
	public String init() {
		if(ajudanteProjeto.isNovaRequisicao()) {
			painelApiPlanosEnviadosForm.init();
			return "/admin/painel_api_planos_enviados/painel_api_planos_enviados.jsf?faces-redirect=true";
		}
		return "";
	}
	
}