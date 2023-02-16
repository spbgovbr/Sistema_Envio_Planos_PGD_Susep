package br.gov.economia.apipgdsusep.enums;

public enum EQuantidadeEntrega {

	QE1("1 Entrega", 1), QE2("2 Entregas", 2), QE3("3 Entregas", 3), QE4("4 Entregas", 4), QE5("5 Entregas", 5),
	QE6("6 Entregas", 6), QE7("7 Entregas", 7), QE8("8 Entregas", 8), QE9("9 Entregas", 9), QE10("10 Entregas", 10),
	QE11("Mais de 10 Entregas", 11);

	private Integer quantidade;
	private String texto;

	private EQuantidadeEntrega(String texto, Integer quantidade) {
		this.texto = texto;
		this.quantidade = quantidade;
	}
	
	public String getTexto() {
		return texto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}
	
}