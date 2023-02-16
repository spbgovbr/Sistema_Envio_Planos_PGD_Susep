package br.gov.economia.apipgdsusep.enums;

public enum ETipoLog {

	AGENDAMENTO("AGENDAMENTO", "Agendamento"),
	ENVIO_PLANO("ENVIO_PLANO", "Envio de Planos"),
	LOG_AGENDAMENTO("LOG_AGENDAMENTO", "Log de Agendamento"),
	LOG_CRON_AGENDAMENTO("LOG_CRON_AGENDAMENTO", "Log do Cron de Agendamento"),
	LOG_ENVIO_PLANO("LOG_ENVIO_PLANO", "Log do Envio de Planos");

	private String descricao;
	private String detalhe;

	private ETipoLog(String descricao, String detalhe) {
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