package br.gov.economia.apipgdsusep.enums;

public enum ESituacaoRegistro {

	ATIVO("Ativo", "Ativado", true),
	INATIVO("Inativo", "Inativado", false);

	private ESituacaoRegistro(String descricao, String detalhe, boolean situacao) {
		this.descricao = descricao;
		this.detalhe = detalhe;
		this.situacao = situacao;
	}

	private String descricao;
	private String detalhe;
	private boolean situacao;

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
	
	public boolean isSituacao() {
		return situacao;
	}
	public boolean getSituacao() {
		return situacao;
	}
	
}