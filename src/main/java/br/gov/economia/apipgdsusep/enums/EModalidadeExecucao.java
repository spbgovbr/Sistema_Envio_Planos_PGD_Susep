package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EModalidadeExecucao {

	ME1("Presencial", 1), ME2("Semipresencial", 2), ME3("Teletrabalho", 3);
	
	private static final Map<Integer, EModalidadeExecucao> modalidadeToEnum = new HashMap<Integer, EModalidadeExecucao>();

	private String texto;
	private Integer modalidade;

	private EModalidadeExecucao(String texto, Integer modalidade) {
		this.texto = texto;
		this.modalidade = modalidade;
	}

	public String getTexto() {
		return texto;
	}
	
	public Integer getModalidade() {
		return modalidade;
	}
	
	static {
		for (EModalidadeExecucao modalidadeExecucao : values())
			modalidadeToEnum.put(modalidadeExecucao.getModalidade(), modalidadeExecucao);
	}
	
	public static EModalidadeExecucao fromModalidadeExecucao(Integer modalidade) {
		return modalidadeToEnum.get(modalidade);
	}
	
}