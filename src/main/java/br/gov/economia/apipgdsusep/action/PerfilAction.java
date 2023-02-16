package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.form.PerfilForm;
import br.gov.economia.apipgdsusep.service.PerfilService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class PerfilAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public final PerfilForm perfilForm;
	public final PerfilService perfilService;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	
	public PerfilAction(@Autowired PerfilForm perfilForm,
			@Autowired PerfilService perfilService) {
		this.perfilForm = perfilForm;
		this.perfilService = perfilService;
	}
	
	public void pesquisar() {
		try {
			perfilForm.setListaPerfil(perfilService.pesquisarTodos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvar() {
		try {
			Perfil usuarioSalvo = perfilService.salvar(perfilForm.getPerfilSalvo());
			if(usuarioSalvo != null) {
				perfilForm.novoPerfil();
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
	
	public void alterar(Perfil perfilSelecionado) {
		try {
			perfilForm.setPerfilSalvo(perfilService.pesquisarPorId(perfilSelecionado.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selecionar(Perfil perfilSelecionado) {
		try {
			perfilForm.setPerfilSelecionado(perfilService.pesquisarPorId(perfilSelecionado.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ativarDesativar(Perfil usuarioSelecionado) {
		boolean dadosSalvosComSucesso = perfilService.ativarDesativar(usuarioSelecionado);
		if(dadosSalvosComSucesso) {
			this.limparPesquisa();
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
			boolean isSucessoUsuario = perfilService.deletar(perfilForm.getPerfilSelecionado());
			if(isSucessoUsuario) {
				this.limparPesquisa();
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
			perfilForm.novoPerfil();
			perfilForm.init();
			return "/admin/manter_perfil/painel_perfil?faces-redirect=true";
		}
		return "";
	}
	
	public void limparPesquisa() {
		perfilForm.setListaPerfil(null);
		perfilForm.setListaFiltradaPerfil(null);
	}

}