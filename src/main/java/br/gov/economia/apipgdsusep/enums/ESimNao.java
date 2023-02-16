package br.gov.economia.apipgdsusep.enums;

public enum ESimNao {

	SIM("S", "Sim", true, 1),
	NAO("N", "Não", false, 0);

	private String descricao;
	private String detalhe;
	private boolean opcao;
	private int valor;

	private ESimNao(String descricao, String detalhe, 
			boolean opcao, int valor) {
		this.descricao = descricao;
		this.detalhe = detalhe;
		this.opcao = opcao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}

	public boolean isOpcao() {
		return opcao;
	}
	public void setOpcao(boolean opcao) {
		this.opcao = opcao;
	}
	
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	
}