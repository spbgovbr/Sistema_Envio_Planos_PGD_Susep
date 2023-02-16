package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EDiasSemana {

	MTODOS("Qualquer dia da semana", "*"), DS0("Domingo", "0"), DS1("Segunda-Feira", "1"), DS2("Terça-Feira", "2"), 
	DS3("Quarta-feira", "3"), DS4("Quinta-Feira", "4"), DS5("Sexta-Feira", "5"), DS6("Sábado", "6");
	
	private static final Map<String, EDiasSemana> diasSemanaToEnum = new HashMap<String, EDiasSemana>();

	private String descricao;
	private String valor;

	private EDiasSemana(String descricao, String valor) {
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	static {
		for (EDiasSemana diaSemana : values())
			diasSemanaToEnum.put(diaSemana.getValor(), diaSemana);
	}
	
	public static EDiasSemana fromDiaSemana(String diaSemana) {
		return diasSemanaToEnum.get(diaSemana);
	}
	
}