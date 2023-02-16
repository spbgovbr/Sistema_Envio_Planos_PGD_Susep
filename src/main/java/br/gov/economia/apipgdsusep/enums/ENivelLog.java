package br.gov.economia.apipgdsusep.enums;

public enum ENivelLog {

	NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO("NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO", "Log da API PG.Cade"),
	NIVEL_JSON_API_PG_CADE_PGD_ME_ERRO("NIVEL_JSON_API_PG_CADE_PGD_ME_ERRO", "Log da API PG.Cade"),
	NIVEL_SISTEMA("NIVEL_SISTEMA", "Log de Sistema"),
	NIVEL_USUARIO("NIVEL_USUARIO", "Log de Usuário");

	private String descricao;
	private String detalhe;

	private ENivelLog(String descricao, String detalhe) {
		this.descricao = descricao;
		this.detalhe = detalhe;
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

}