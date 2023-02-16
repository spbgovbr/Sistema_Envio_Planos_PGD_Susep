package br.gov.economia.apipgdsusep.security;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.enums.EPaginasAcesso;
import br.gov.economia.apipgdsusep.enums.ESimNao;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Component
@SessionScope
public class RestricoesControleAcesso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UsuarioLogado usuarioLogado;
	

	public boolean isUsuarioTemAcessoNaPagina() {
		boolean usuarioTemAcessoNaPagina = false;
		String paginaRequisitada = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		try {
			EPaginasAcesso paginaAcessoRequisitada = EPaginasAcesso.fromPagina(paginaRequisitada);
			if(usuarioLogado.isUsuarioAutenticado()) {
				usuarioTemAcessoNaPagina = usuarioLogado.getUsuario().
						getPaginasAcessoUsuario().contains(paginaAcessoRequisitada);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			usuarioTemAcessoNaPagina = false;
		}
		return usuarioTemAcessoNaPagina;
	}
	
	public ESituacaoRegistro getSituacaoRegistro(String situacaoRegistro) {
		return ESituacaoRegistro.valueOf(situacaoRegistro);
	}
	
	public ESimNao getSituacaoPermissao(String situacaoPermissao) {
		return ESimNao.valueOf(situacaoPermissao);
	}
	
	public EPaginasAcesso getPaginaAcesso(String paginaAcesso) {
		return EPaginasAcesso.valueOf(paginaAcesso);
	}
	
	public String redirecionarPara(EPaginasAcesso pagina) {
		return pagina.getUrl() + "?faces-redirect=true";
	}
	
}