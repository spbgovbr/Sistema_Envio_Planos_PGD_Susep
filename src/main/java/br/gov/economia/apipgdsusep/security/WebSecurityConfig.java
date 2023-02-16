package br.gov.economia.apipgdsusep.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.gov.economia.apipgdsusep.service.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${spring.ldap.domain}")
    private String ldapDomainName;
	
	@Value("${spring.ldap.url}")
    private String ldapUrl;
	
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;
	
	@Autowired
	public WebSecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}
	
	@Bean
	public PasswordEncoder paswEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(usuarioDetailsService).passwordEncoder(paswEncoder());
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccesHandler() {
		return new RedirectLoginSuscessHandler();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
			.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/gfx/**", "/javax.faces.resource/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
        	.antMatchers(HttpMethod.GET, "/resources/**", "/static/**", "/css/**", "/js/**", "/gfx/**", "/javax.faces.resource/**").permitAll()
        	.antMatchers(HttpMethod.POST, "/javax.faces.resource/**").permitAll()
			.anyRequest().fullyAuthenticated()
		.and()
			.formLogin()
				.loginPage("/login.jsf")
				.successHandler(authenticationSuccessHandler)
				.failureUrl("/login.jsf?error=true")
				.permitAll()
		.and()
	        .logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login.jsf")
				.deleteCookies("JSESSIONID")
				.permitAll()
		.and()
			.httpBasic();
	}
	
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
        	.antMatchers(HttpMethod.GET, "/resources/**", "/static/**", "/css/**", "/js/**", "/gfx/**", "/javax.faces.resource/**").permitAll()
        	.antMatchers(HttpMethod.POST, "/javax.faces.resource/**").permitAll()
			.anyRequest().fullyAuthenticated()
		.and()
		.formLogin()
			.loginPage("/login.jsf")
			.loginProcessingUrl("/login")
			.successHandler(authenticationSuccessHandler)
			.usernameParameter("username")
	        .passwordParameter("password")
			//.failureUrl("/login?error=true")
			.failureUrl("/login.jsf?error=true")
			.permitAll()
        .and()
        .logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			//.logoutSuccessUrl("/login")
			.logoutSuccessUrl("/login.jsf")
			.deleteCookies("JSESSIONID")
			.permitAll();
	}*/

	/*@Bean
	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider = new
				ActiveDirectoryLdapAuthenticationProvider(ldapDomainName, ldapUrl);
				//ActiveDirectoryLdapAuthenticationProvider("cade.gov.br", "ldap://cade.gov.br:389/");
		return activeDirectoryLdapAuthenticationProvider;
	}*/

}