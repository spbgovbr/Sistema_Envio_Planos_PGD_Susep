package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class ApiPgCadeForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int qtdPlanos;
	private int qtdAtividades;
	
	
	public int getQtdPlanos() {
		return qtdPlanos;
	}
	public void setQtdPlanos(int qtdPlanos) {
		this.qtdPlanos = qtdPlanos;
	}
	
	public int getQtdAtividades() {
		return qtdAtividades;
	}
	public void setQtdAtividades(int qtdAtividades) {
		this.qtdAtividades = qtdAtividades;
	}
	
}