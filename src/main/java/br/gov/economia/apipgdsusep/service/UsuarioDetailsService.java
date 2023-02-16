package br.gov.economia.apipgdsusep.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.repository.UsuarioJpaRepository;

@Service
@Transactional
public class UsuarioDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioJpaRepository usuarioJpaRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Usuario user = usuarioJpaRepository.findByEmail(userName)
				.orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " Não Encontrado"));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getCodigoSenha(),
				getAuthorities(user));

	}

	private static Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
		String[] userRoles = usuario.getPerfis().stream().map((perfil) -> perfil.getDescricao()).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}
	
	public static void main(String[] args) {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		System.out.println(bc.encode("@t3st3#"));
	}

}