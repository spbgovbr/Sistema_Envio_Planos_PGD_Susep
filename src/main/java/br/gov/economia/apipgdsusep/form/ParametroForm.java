package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Parametro;

@Component
@SessionScope
public class ParametroForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Parametro parametroSelecionado;
	private List<Parametro> listaParametro;
	private List<Parametro> listaFiltradaParametro;
	
	
	public Parametro getParametroSelecionado() {
		return parametroSelecionado;
	}
	public void setParametroSelecionado(Parametro parametroSelecionado) {
		this.parametroSelecionado = parametroSelecionado;
	}
	
	public List<Parametro> getListaParametro() {
		return listaParametro;
	}
	public void setListaParametro(List<Parametro> listaParametro) {
		this.listaParametro = listaParametro;
	}

	public List<Parametro> getListaFiltradaParametro() {
		return listaFiltradaParametro;
	}
	public void setListaFiltradaParametro(List<Parametro> listaFiltradaParametro) {
		this.listaFiltradaParametro = listaFiltradaParametro;
	}
	
}