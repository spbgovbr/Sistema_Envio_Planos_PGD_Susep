package br.gov.economia.apipgdsusep.utils;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.rest.apipgdme.ResponseJsonApiPGDME;

@Component
@SessionScope
public class AjudanteMensagens {
	
	private ResponseJsonApiPGDME responseJsonApiPGDME;

	public void exibirMensagem(Severity tipoMensagem, String tituloMensagem, String mensagem) {
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage(tipoMensagem, tituloMensagem, mensagem));
	}
	
	public void exibirErrorResponse(ResponseJsonApiPGDME errorResponseJsonApiPGDME) {
		responseJsonApiPGDME = errorResponseJsonApiPGDME;
		PrimeFaces.current().executeScript("PF('modalErrorResponseWidgetVar').show();");
	}
	
	
	public ResponseJsonApiPGDME getResponseJsonApiPGDME() {
		return responseJsonApiPGDME;
	}
	public void setResponseJsonApiPGDME(ResponseJsonApiPGDME responseJsonApiPGDME) {
		this.responseJsonApiPGDME = responseJsonApiPGDME;
	}

}