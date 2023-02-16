package br.gov.economia.apipgdsusep.action;

import java.io.Serializable;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.gov.economia.apipgdsusep.entity.Funcao;
import br.gov.economia.apipgdsusep.form.FuncaoForm;
import br.gov.economia.apipgdsusep.service.FuncaoService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;
import br.gov.economia.apipgdsusep.utils.AjudanteProjeto;

@Component
@RequestScope
public class FuncaoAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public final FuncaoForm funcaoForm;
	public final FuncaoService funcaoService;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	@Autowired
	private AjudanteProjeto ajudanteProjeto;
	
	
	public FuncaoAction(@Autowired FuncaoForm funcaoForm,
			@Autowired FuncaoService funcaoService) {
		this.funcaoForm = funcaoForm;
		this.funcaoService = funcaoService;
	}
	
	public void pesquisar() {
		try {
			funcaoForm.setListaFuncao(funcaoService.pesquisarTodas());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvar() {
		try {
			Funcao funcaoSalvo = funcaoService.salvar(funcaoForm.getFuncaoSalva());
			if(funcaoSalvo != null) {
				funcaoForm.novaFuncao();
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
	
	public void alterar(Funcao funcaoSelecionada) {
		try {
			funcaoForm.setFuncaoSalva(funcaoService.pesquisarPorId(funcaoSelecionada.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selecionar(Funcao funcaoSelecionada) {
		try {
			funcaoForm.setFuncaoSelecionada(funcaoService.pesquisarPorId(funcaoSelecionada.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ativarDesativar(Funcao funcaoSelecionado) {
		boolean dadosSalvosComSucesso = funcaoService.ativarDesativar(funcaoSelecionado);
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
			boolean isSucessoFuncao = funcaoService.deletar(funcaoForm.getFuncaoSelecionada());
			if(isSucessoFuncao) {
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
			funcaoForm.novaFuncao();
			return "/admin/manter_funcao/painel_funcao?faces-redirect=true";
		}
		return "";
	}
	
	public void limparPesquisa() {
		funcaoForm.setListaFuncao(null);
		funcaoForm.setListaFiltradaFuncao(null);
	}

}