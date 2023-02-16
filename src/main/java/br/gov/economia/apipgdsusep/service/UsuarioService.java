package br.gov.economia.apipgdsusep.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.repository.UsuarioJpaRepository;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagemExcecao;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.validator.ValidadorCPFCNPJ;

@Service
@Transactional(transactionManager = "transactionManager")
public class UsuarioService {
	
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	@Value("${spring.ldap.domain}")
    private String ldapDomainName;
	
	@Autowired
	private UsuarioJpaRepository usuarioJpaRepository;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	
	public Usuario pesquisarPorId(Long id) {
		try {
			return usuarioJpaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Usuario getUsuarioLogado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = this.pesquisarPorEmail(authentication.getName());
		return usuario;
	}
	
	public Usuario pesquisarPorEmail(String email) {
		try {
			/*if(!email.contains("@"+ldapDomainName)) {
				email += "@"+ldapDomainName;
			}*/
			return usuarioJpaRepository.findByEmail(email).get();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Usuario pesquisar(Usuario usuario) {
		try {
			return usuarioJpaRepository.findByEmailOrNumeroCPF(usuario.getEmail(), usuario.getPessoa().getNumeroCPF());
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Usuario> pesquisarTodos() {
		return usuarioJpaRepository.findAll();
	}
	
	public List<Usuario> pesquisarPorFiltros(Usuario usuario) {
		Specification<Usuario> condition = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (usuario.getPessoa() != null) {
				predicates.add(builder.equal(root.get("pessoa"), usuario.getPessoa()));
			}
			if (usuario.getEmail() != null 
					&& StringUtils.isNotBlank(usuario.getEmail())) {
				predicates.add(builder.like(root.get("email"), "%" + usuario.getEmail() + "%"));
			}
			if (usuario.getSituacaoRegistro() != null) {
				predicates.add(builder.equal(root.get("situacaoRegistro"), usuario.getSituacaoRegistro()));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		return usuarioJpaRepository.findAll(condition);
	}
	
	public boolean ativarDesativar(Usuario usuarioSelecionado) {
		Usuario usuarioPesquisado = null;
		try {
			usuarioPesquisado = this.pesquisarPorId(usuarioSelecionado.getId());
			usuarioPesquisado.setSituacaoRegistro(usuarioSelecionado.getSituacaoRegistro().
					getSituacao() ? ESituacaoRegistro.INATIVO : ESituacaoRegistro.ATIVO);
			usuarioPesquisado = usuarioJpaRepository.save(usuarioPesquisado);
			return true;
		} catch (Exception e)  {
			e.printStackTrace();
			return false;
		}
	}
	
	public Usuario salvar(Usuario usuario) {
		if(isDadosValidos(usuario)) {
			usuario = this.atualizarSenha(usuario);
			usuario = usuarioJpaRepository.save(usuario);
			return usuario;
		}
		return null;
	}
	
	public Usuario atualizar(Usuario usuario) {
		usuario = usuarioJpaRepository.save(usuario);
		return usuario;
	}
	
	public boolean isDadosValidos(Usuario usuario) {
		if(!isCamposObrigatorioPreenchidos(usuario))
			return false;
		if(!isCPFOuCNPJValido(usuario))
			return false;
		if(!isEmailValido(usuario))
			return false;
		if(isUsuarioCadastrado(usuario))
			return false;
		return true;
	}
	
	public Usuario atualizarSenha(Usuario usuario) {
		if(usuario.getId() == null && usuario.getSenha() != null) {
			usuario.setCodigoSenha(PASSWORD_ENCODER.encode(usuario.getSenha()));
		}
		return usuario;
	}
	
	public boolean isCamposObrigatorioPreenchidos(Usuario usuario) {
		boolean isCamposPreenchidos = usuario.isCamposObrigatoriosPreenchidos();
		if(!isCamposPreenchidos) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Os campos obrigatório não foram preenchidos.");
		}
		return isCamposPreenchidos;
	}
	
	public boolean isCPFOuCNPJValido(Usuario usuario) {
		if (!ValidadorCPFCNPJ.isCPFOuCNPJValido(usuario.getPessoa().getNumeroCPF())) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "O  CPF informado não é valido.");
			return false;
		}
		return true;
	}
	
	public boolean isEmailValido(Usuario usuario) {
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)");
		Matcher matcher = pattern.matcher(usuario.getEmail());
		if (!matcher.find() && (usuario.getEmail() != null && StringUtils.isNotBlank(usuario.getEmail()))) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, "Alerta!", "O E-mail digitado não é válido.");
			return false;
		}
		return true;
	}

	public boolean isUsuarioCadastrado(Usuario usuario) {
		if (this.isUsuarioJaCadastrado(usuario)) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Já existe um Usuário cadastrado com os dados informados.");
			return true;
		}
		return false;
	}

	public boolean isUsuarioJaCadastrado(Usuario usuario) {
		boolean isUsuarioJaCadastrado = false;
		Usuario usuarioCadastrado = null;
		try {
			if(usuario.getPessoa().getNumeroCPF() != null && usuario.getEmail() != null) {
				usuarioCadastrado = this.pesquisar(usuario);
				if(usuario.getId() == null) {
					isUsuarioJaCadastrado = usuarioCadastrado != null ? true : false; 
				} else {
					if(usuario.getId().equals(usuarioCadastrado.getId()) && 
							usuarioCadastrado.getPessoa().getNumeroCPF().equals(usuario.getPessoa().getNumeroCPF())) {
						isUsuarioJaCadastrado = false;
					}
					if(!usuario.getId().equals(usuarioCadastrado.getId()) && 
							(usuarioCadastrado.getPessoa().getNumeroCPF().equals(usuario.getPessoa().getNumeroCPF()) ||
									usuarioCadastrado.getEmail().equals(usuario.getEmail()))) {
						isUsuarioJaCadastrado = true;
					} 
				}
			}
		} catch (Exception e) {
			if(AjudanteMensagemExcecao.isResultadoRetornaMaisDeUmElemento(e.toString())) {
				isUsuarioJaCadastrado = true;
			}
		}
		return isUsuarioJaCadastrado;
	}
	
	public boolean deletar(Usuario usuarioSelecionado) {
		try {
			Usuario usuarioPesq = this.pesquisarPorId(usuarioSelecionado.getId());
			usuarioJpaRepository.delete(usuarioPesq);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}