package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Funcao;
import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.entity.PerfilFuncao;
import br.gov.economia.apipgdsusep.enums.ESimNao;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.service.FuncaoService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;

@Component
@SessionScope
public class PerfilForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private FuncaoService funcaoService;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	private Perfil perfilSalvo;
	private Perfil perfilSelecionado;
	
	private List<Perfil> listaPerfil;
	private List<Perfil> listaFiltradaPerfil;
	
	private Funcao funcaoSelecionada;
	private List<Funcao> listaFuncao;
	
	
	public Perfil getPerfilSalvo() {
		return perfilSalvo;
	}
	public void setPerfilSalvo(Perfil perfilSalvo) {
		this.perfilSalvo = perfilSalvo;
	}
	
	public Perfil getPerfilSelecionado() {
		return perfilSelecionado;
	}
	public void setPerfilSelecionado(Perfil perfilSelecionado) {
		this.perfilSelecionado = perfilSelecionado;
	}
	
	public List<Perfil> getListaPerfil() {
		return listaPerfil;
	}
	public void setListaPerfil(List<Perfil> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}
	
	public List<Perfil> getListaFiltradaPerfil() {
		return listaFiltradaPerfil;
	}
	public void setListaFiltradaPerfil(List<Perfil> listaFiltradaPerfil) {
		this.listaFiltradaPerfil = listaFiltradaPerfil;
	}
	
	public Funcao getFuncaoSelecionada() {
		return funcaoSelecionada;
	}
	public void setFuncaoSelecionada(Funcao funcaoSelecionada) {
		this.funcaoSelecionada = funcaoSelecionada;
	}
	
	public List<Funcao> getListaFuncao() {
		return listaFuncao;
	}
	public void setListaFuncao(List<Funcao> listaFuncao) {
		this.listaFuncao = listaFuncao;
	}
	
	public List<ESituacaoRegistro> getSituacoesRegistro() {
		return Arrays.asList(ESituacaoRegistro.values());
	}
	
	public List<ESimNao> getSituacoesPermissao() {
		return Arrays.asList(ESimNao.values());
	}
	
	public void novoPerfil() {
		if(perfilSalvo == null) {
			perfilSalvo = new Perfil(ESituacaoRegistro.ATIVO);
		}
	}
	
	public void init() {
		if(this.getListaFuncao() == null || this.getListaFuncao().size() == 0) {
			this.setListaFuncao(funcaoService.pesquisarPorFiltros(new Funcao(ESituacaoRegistro.ATIVO)));
		}
	}
	
	public List<Funcao> autoCompleteFuncao(String query) {
		List<Funcao> listaFuncoesFiltrada = new ArrayList<Funcao>();
		if(this.getListaFuncao() != null && this.getListaFuncao().size() > 0) {
			for (int i = 0; i < this.getListaFuncao().size(); i++) {
				Funcao funcao = this.getListaFuncao().get(i);
				if(funcao.getDescricao().toLowerCase().trim().contains(query.toLowerCase().trim())) {
					listaFuncoesFiltrada.add(funcao);
				}
			}
		}
		return listaFuncoesFiltrada;
	}
	
	public void selecionarFuncao(SelectEvent event) {
		Funcao funcao = (Funcao) event.getObject();
		this.setFuncaoSelecionada(funcao);
	}
	
	public void adicionarPerfilFuncao() {
		try {
			if(this.getFuncaoSelecionada() != null) {
				PerfilFuncao perfilFuncao = new PerfilFuncao(this.getPerfilSalvo(), this.getFuncaoSelecionada(), 
						ESimNao.NAO, ESimNao.NAO, ESimNao.NAO, ESimNao.NAO, ESituacaoRegistro.ATIVO);
				if(!this.getPerfilSalvo().getFuncoesDoPerfilLista().contains(perfilFuncao)) {
					this.getPerfilSalvo().getFuncoesDoPerfilLista().add(perfilFuncao);
					ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, 
							"Informação!", "A Função foi adicionada.");
				} else {
					ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
							"Alerta!", "A Função já foi adicionada!");
				}
			} else {
				ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_WARN, 
						"Alerta!", "Selecione uma Função!");
			}
			this.removerFuncao();
			Collections.sort(this.getPerfilSalvo().getFuncoesDoPerfilLista(), PerfilFuncao.DescricaoFuncaoComparator);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void removerFuncao() {
		this.setFuncaoSelecionada(null);
	}
	
	public void excluirPerfilFuncao(PerfilFuncao perfilFuncaoSelecionado) {
		try {
			Iterator<PerfilFuncao> funcoesDoPerfilIterator = 
					perfilFuncaoSelecionado.getPerfil().getFuncoesDoPerfilLista().iterator();
			while(funcoesDoPerfilIterator.hasNext()) {
				PerfilFuncao perfilFuncao = funcoesDoPerfilIterator.next();
				if(perfilFuncao.getFuncao().equals(perfilFuncaoSelecionado.getFuncao())
						&& perfilFuncao.getPerfil().equals(perfilFuncaoSelecionado.getPerfil())) {
					funcoesDoPerfilIterator.remove();
				}
			}
			Collections.sort(perfilFuncaoSelecionado.getPerfil().getFuncoesDoPerfilLista(), PerfilFuncao.DescricaoFuncaoComparator);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}