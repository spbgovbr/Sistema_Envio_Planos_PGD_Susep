package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Funcao;
import br.gov.economia.apipgdsusep.enums.EPaginasAcesso;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;

@Component
@SessionScope
public class FuncaoForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Funcao funcaoSalva;
	private Funcao funcaoSelecionada;
	
	private List<Funcao> listaFuncao;
	private List<Funcao> listaFiltradaFuncao;
	
	
	public Funcao getFuncaoSalva() {
		return funcaoSalva;
	}
	public void setFuncaoSalva(Funcao funcaoSalva) {
		this.funcaoSalva = funcaoSalva;
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
	
	public List<Funcao> getListaFiltradaFuncao() {
		return listaFiltradaFuncao;
	}
	public void setListaFiltradaFuncao(List<Funcao> listaFiltradaFuncao) {
		this.listaFiltradaFuncao = listaFiltradaFuncao;
	}
	
	public List<ESituacaoRegistro> getSituacoesRegistro() {
		return Arrays.asList(ESituacaoRegistro.values());
	}
	
	public List<EPaginasAcesso> getPaginasAcesso() {
		return Arrays.asList(EPaginasAcesso.values());
	}
	
	public void novaFuncao() {
		funcaoSalva = new Funcao(ESituacaoRegistro.ATIVO);
	}

}