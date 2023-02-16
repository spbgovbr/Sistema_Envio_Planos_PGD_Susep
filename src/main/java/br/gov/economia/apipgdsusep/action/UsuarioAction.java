package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.form.UsuarioForm;
import br.gov.economia.apipgdsusep.service.UsuarioService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class UsuarioAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public final UsuarioForm usuarioForm;
	public final UsuarioService usuarioService;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	
	public UsuarioAction(@Autowired UsuarioForm usuarioForm,
			@Autowired UsuarioService usuarioService) {
		this.usuarioForm = usuarioForm;
		this.usuarioService = usuarioService;
	}
	
	public void pesquisar() {
		try {
			usuarioForm.setListaUsuario(usuarioService.pesquisarTodos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvar() {
		try {
			Usuario usuarioSalvo = usuarioService.salvar(usuarioForm.getUsuarioSalvo());
			if(usuarioSalvo != null) {
				usuarioForm.novoUsuario();
				this.pesquisar();
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
						"Informação!", "Os dados foram salvos com sucesso.");
			} else {
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
						"Alerta!", "Os dados não foram salvos corretamente.");
			}
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public void atualizar(Usuario usuarioSelecionado) {
		try {
			Usuario usuarioSalvo = usuarioService.atualizar(usuarioSelecionado);
			if(usuarioSalvo != null) {
				usuarioForm.novoUsuario();
				this.pesquisar();
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
						"Informação!", "Os dados foram salvos com sucesso.");
			} else {
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
						"Alerta!", "Os dados não foram salvos corretamente.");
			}
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public void alterar(Usuario usuarioSelecionado) {
		try {
			usuarioForm.setUsuarioSalvo(usuarioService.pesquisarPorId(usuarioSelecionado.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selecionar(Usuario usuarioSelecionado) {
		try {
			usuarioForm.setUsuarioSelecionado(usuarioService.pesquisarPorId(usuarioSelecionado.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ativarDesativar(Usuario usuarioSelecionado) {
		boolean dadosSalvosComSucesso = usuarioService.ativarDesativar(usuarioSelecionado);
		if(dadosSalvosComSucesso) {
			this.pesquisar();
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
					"Informação!", "Os dados foram salvos com sucesso.");
		} else {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
		}
	}
	
	public void deletar(boolean isExcluir) {
		if(isExcluir) {
			boolean isSucessoUsuario = usuarioService.deletar(usuarioForm.getUsuarioSelecionado());
			if(isSucessoUsuario) {
				this.pesquisar();
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
						"Informação!", "Os dados foram deletados com sucesso.");
			} else {
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
						"Erro!", "Não foi possível realizar a operação solicitada.");
			}
		}
	}
	
	public String init() {
		if(!ajudanteProjeto.isNovaRequisicaoAjax()) {
			this.limparPesquisa();
			this.pesquisar();
			usuarioForm.novoUsuario();
			return "/admin/manter_usuario/painel_usuario?faces-redirect=true";
		}
		return "";
	}
	
	public void limparPesquisa() {
		usuarioForm.setListaUsuario(null);
		usuarioForm.setListaFiltradaUsuario(null);
	}

}