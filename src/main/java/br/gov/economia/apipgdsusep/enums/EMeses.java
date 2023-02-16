package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EMeses {

	MTODOS("Todo mês", "*"), M1("Janeiro", "1"), M2("Fevereiro", "2"), M3("Março", "3"), M4("Abril", "4"), M5("Maio", "5"), M6("Junho", "6"), 
	M7("Julho", "7"), M8("Agosto", "8"), M9("Setembro", "9"), M10("Outubro", "10"), M11("Novembro", "11"), M12("Dezembro", "12");
	
	private static final Map<String, EMeses> mesesToEnum = new HashMap<String, EMeses>();

	private String descricao;
	private String valor;

	private EMeses(String descricao, String valor) {
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
		for (EMeses mes : values())
			mesesToEnum.put(mes.getValor(), mes);
	}
	
	public static EMeses fromMes(String mes) {
		return mesesToEnum.get(mes);
	}
	
}