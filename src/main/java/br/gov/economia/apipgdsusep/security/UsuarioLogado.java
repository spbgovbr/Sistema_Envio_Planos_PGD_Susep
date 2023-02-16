package br.gov.economia.apipgdsusep.security;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.service.UsuarioService;

@Component
@SessionScope
public class UsuarioLogado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsuarioService usuarioService;
	
	private Usuario usuario;
	
	
	@PostConstruct
	public void init() {
		usuario = usuarioService.getUsuarioLogado();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public boolean isUsuarioAutenticado() {
		return usuario != null && usuario.getId() != null;
	}
	
	@Override
	public String toString() {
		return usuario.getPessoa().getNome();
	}
	
}