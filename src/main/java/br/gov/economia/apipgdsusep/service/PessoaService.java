package br.gov.economia.apipgdsusep.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.entity.Pessoa;
import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.repository.PessoaJpaRepository;
import br.gov.economia.apipgdsusep.security.Criptografia;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagemExcecao;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.validator.ValidadorCPFCNPJ;

@Service
@Transactional(transactionManager = "transactionManager")
public class PessoaService {

	@Autowired
	private PessoaJpaRepository pessoaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;


	public Pessoa pesquisarPorId(Long id) {
		try {
			return pessoaRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public Pessoa pesquisarPorCPF(String cpf) throws Exception {
		return pessoaRepository.findByCPF(cpf);
	}
	
	public List<Pessoa> pesquisarPorFiltros(Pessoa pessoa) {
		Specification<Pessoa> condition = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (pessoa.getNome() != null 
					&& StringUtils.isNotBlank(pessoa.getNome())) {
				predicates.add(builder.like(root.get("nome"), "%" + pessoa.getNome() + "%"));
			}
			if (pessoa.getNumeroCPF() != null 
					&& StringUtils.isNotBlank(pessoa.getNumeroCPF())) {
				predicates.add(builder.equal(root.get("numeroCPF"), pessoa.getNumeroCPF()));
			}
			if (pessoa.getSituacaoRegistro() != null) {
				predicates.add(builder.equal(root.get("situacaoRegistro"), pessoa.getSituacaoRegistro()));
			}
			Predicate[] ps = new Predicate[predicates.size()];
			return query.where(builder.and(predicates.toArray(ps))).getRestriction();
		};
		return pessoaRepository.findAll(condition);
	}
	
	public boolean ativarDesativar(Pessoa pessoaSelecionada) {
		Pessoa pessoaPesquisada = null;
		try {
			pessoaPesquisada = this.pesquisarPorId(pessoaSelecionada.getId());
			pessoaPesquisada.setSituacaoRegistro(pessoaSelecionada.getSituacaoRegistro().
					equals(ESituacaoRegistro.ATIVO) ? ESituacaoRegistro.INATIVO : ESituacaoRegistro.ATIVO);
			pessoaPesquisada = pessoaRepository.save(pessoaPesquisada);
			return true;
		} catch (Exception e)  {
			e.printStackTrace();
			return false;
		}
	}

	public Pessoa salvar(Pessoa pessoa) {
		Pessoa pessoaSalva = null;
		if(isDadosValidos(pessoa)) {
			try {
				pessoaSalva = pessoaRepository.save(pessoa);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pessoa.getUsuario().setPerfis(new ArrayList<Perfil>());
		}
		return pessoaSalva;
	}
	
	public boolean isDadosValidos(Pessoa pessoa) {
		if(pessoa.getId() == null) {
			String senha = Criptografia.getNumerosAleatorios(6);
			pessoa.getUsuario().setSenha(senha);
			pessoa.getUsuario().setCodigoSenha(passwordEncoder.encode(senha));
			Perfil role = perfilService.getRole("ROLE_USUARIO");
			if(!pessoa.getUsuario().getPerfis().contains(role)) {
				pessoa.getUsuario().getPerfis().add(role);
			}
		}
		if(!isCamposObrigatorioPreenchidos(pessoa))
			return false;
		if(!isCPFOuCNPJValido(pessoa))
			return false;
		if(isPessoaCadastrada(pessoa))
			return false;
		return true;
	}
	
	public boolean isCamposObrigatorioPreenchidos(Pessoa pessoa) {
		boolean isCamposPreenchidos = pessoa.isCamposObrigatoriosPreenchidos();
		if(!isCamposPreenchidos) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Os campos obrigatório não foram preenchidos.");
		}
		return isCamposPreenchidos;
	}

	public boolean isCPFOuCNPJValido(Pessoa pessoa) {
		if (!ValidadorCPFCNPJ.isCPFOuCNPJValido(pessoa.getNumeroCPF())) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "O CPF informado não é valido.");
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

	public boolean isPessoaCadastrada(Pessoa pessoa) {
		if (this.isPessoaJaCadastrada(pessoa)) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
					"Alerta!", "Já existe uma Pessoa cadastrada com o " + 
							(pessoa.getNumeroCPF() != null ? "CPF" : "CNPJ") + " ou E-mail informados.");
			return true;
		}
		return false;
	}

	public boolean isPessoaJaCadastrada(Pessoa pessoa) {
		boolean isPessoaJaCadastrada = false;
		Pessoa pessoaCadastrada = null;
		try {
			if(pessoa.getNumeroCPF() != null) {
				pessoaCadastrada = this.pesquisarPorCPF(pessoa.getNumeroCPF());
				if(pessoa.getId() == null) {
					isPessoaJaCadastrada = pessoaCadastrada != null ? true : false; 
				} else {
					if(pessoa.getId().equals(pessoaCadastrada.getId()) && 
							pessoaCadastrada.getNumeroCPF().equals(pessoa.getNumeroCPF())) {
						isPessoaJaCadastrada = false;
					}
					if(!pessoa.getId().equals(pessoaCadastrada.getId()) && 
							(pessoaCadastrada.getNumeroCPF().equals(pessoa.getNumeroCPF()) ||
									pessoaCadastrada.getUsuario().getEmail().equals(pessoa.getUsuario().getEmail()))) {
						isPessoaJaCadastrada = true;
					} 
				}
			}
		} catch (Exception e) {
			if(AjudanteMensagemExcecao.isResultadoRetornaMaisDeUmElemento(e.toString())) {
				isPessoaJaCadastrada = true;
			}
		}
		return isPessoaJaCadastrada;
	}
	
}