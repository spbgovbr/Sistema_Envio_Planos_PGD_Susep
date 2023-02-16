package br.gov.economia.apipgdsusep.security;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RedirectLoginSuscessHandler implements AuthenticationSuccessHandler {
	
	/*@Autowired
	private UsuarioService usuarioService;*/
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if(roles.size() == 0) {
			response.sendRedirect("/login.jsf?error=true");
        } else {
        	response.sendRedirect("/admin/home.jsf");
        }
	}
	
	/*@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		try {
			Usuario usuario = usuarioService.pesquisarPorEmail(authentication.getName());
			CustomLdapUserDetails customLdapUserDetails = new CustomLdapUserDetails();
			customLdapUserDetails.setUserName(usuario.getEmail());
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
			for (Perfil perfil : usuario.getPerfis()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(perfil.getDescricao()));
			}
			customLdapUserDetails.setGrantedAuthorities(grantedAuthorities);
			Set<String> authorities = AuthorityUtils.authorityListToSet(customLdapUserDetails.getAuthorities());
			if(authorities.size() == 0) {
	        	//response.sendRedirect("/login?error=true");
				response.sendRedirect("/login.jsf?error=true");
	        } else {
	        	response.sendRedirect("/admin/home.jsf");
	        	//for(String authority : authorities) {
	        	//	if (authority.contains("ADMIN") || authority.contains("USUARIO")) {
	    				response.sendRedirect("/admin/home.jsf");
	    	    //    } 
	        	//}
	        }
		} catch (Exception e) {
			System.out.println("O Usuário " + authentication.getName() + " não tem permissão de acesso!");
			//response.sendRedirect("/login?error=true");
			response.sendRedirect("/login.jsf?error=true");
		}
	}*/

}