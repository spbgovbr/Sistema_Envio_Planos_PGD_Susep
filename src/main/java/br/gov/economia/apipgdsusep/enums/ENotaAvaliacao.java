package br.gov.economia.apipgdsusep.enums;

public enum ENotaAvaliacao {

	NA1(1), NA2(2), NA3(3), NA4(4), NA5(5),
	NA6(6), NA7(7), NA8(8), NA9(9), NA10(10);

	private Integer nota;

	private ENotaAvaliacao(Integer nota) {
		this.nota = nota;
	}

	public Integer getNota() {
		return nota;
	}
	
}