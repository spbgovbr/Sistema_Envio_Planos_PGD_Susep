package br.gov.economia.apipgdsusep.utils;

import java.io.Serializable;

public class AjudanteMensagemExcecao implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String RESULTADO_RETORNA_MAIS_DE_UM_ELEMENTO = 
			"org.springframework.dao.IncorrectResultSizeDataAccessException: query did not return a unique result";
	
	
	public static boolean isResultadoRetornaMaisDeUmElemento(String mensagemExcecao) {
		return (mensagemExcecao.contains(RESULTADO_RETORNA_MAIS_DE_UM_ELEMENTO) 
				|| mensagemExcecao.equals(RESULTADO_RETORNA_MAIS_DE_UM_ELEMENTO));
	}
	
}