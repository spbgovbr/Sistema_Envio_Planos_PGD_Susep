package br.gov.economia.apipgdsusep.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class AjudanteProjeto {

    public boolean isNovaRequisicao() {
        final boolean getMethod = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getMethod().equals("GET");
        final boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
        final boolean validationFailed = FacesContext.getCurrentInstance().isValidationFailed();
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return getMethod && !ajaxRequest && !validationFailed && req.getHeader("X-Requested-With") == null;
    }
    
    public boolean isNovaRequisicaoAjax() {
        final boolean getMethod = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getMethod().equals("GET");
        final boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
        final boolean validationFailed = FacesContext.getCurrentInstance().isValidationFailed();
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return !getMethod && ajaxRequest && !validationFailed && req.getHeader("X-Requested-With").equals("XMLHttpRequest");
    }
    
    public String resumirString(String string, int limite, boolean reticenciaAoFinal){
		StringBuffer stringBuffer = new StringBuffer();
		if(string != null) {
			if(string.length() > limite) {
				stringBuffer.append(string.substring(0, limite));
			} else{
				stringBuffer.append(string);
			}
			if(string.length() > limite && reticenciaAoFinal){
				stringBuffer.append("...");
			}
		}
		return stringBuffer.toString();
	}

}