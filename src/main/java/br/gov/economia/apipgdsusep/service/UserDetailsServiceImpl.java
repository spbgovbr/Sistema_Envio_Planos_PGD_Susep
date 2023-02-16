package br.gov.economia.apipgdsusep.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.gov.economia.apipgdsusep.dto.CustomUserDetails;
import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.repository.UsuarioJpaRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Value("${spring.ldap.domain}")
    private String ldapDomainName;

	@Autowired
	private UsuarioJpaRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(!username.contains("@"+ldapDomainName)) {
			username += "@"+ldapDomainName;
		}
		Usuario usuario = usuarioRepository.findByEmail(username).get();
		if (usuario != null) {
			CustomUserDetails customUserDetails = new CustomUserDetails();
			customUserDetails.setUserName(usuario.getEmail());
			customUserDetails.setPassword(usuario.getCodigoSenha());
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			for (Perfil perfil : usuario.getPerfis()) {
				authorities.add(new SimpleGrantedAuthority(perfil.getDescricao()));
			}
			customUserDetails.setGrantedAuthorities(authorities);
			return customUserDetails;
		}
		throw new UsernameNotFoundException(username);
	}

}