package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EPaginasAcesso {

	/* Icons from fontawesome.com/icon - file: fontawesome.css */
	
	PAINEL_AGENDAMENTO("Agendamento", "fa-calendar-alt", "/admin/painel_agendamento/painel_agendamento.jsf"),
	PAINEL_API_PG_CADE("API PG.Cade - Enviar Planos", "fa-file-import", "/admin/painel_api_pg_cade/painel_api_pg_cade.jsf"),
	PAINEL_API_PLANOS_ENVIADOS("API PG.Cade - Planos Enviados", "fa-exchange-alt", "/admin/painel_api_pactos_enviados/painel_api_pactos_enviados.jsf"),
	PAINEL_PARAMETROS("Parâmetros", "fa-wrench", "/admin/painel_parametro/painel_parametro.jsf"),
	MANTER_FUNCOES("Funções", "fa-cogs", "/admin/manter_funcao/painel_funcao.jsf"),
	MANTER_PERFIS("Perfis", "fa-lock", "/admin/manter_perfil/painel_perfil.jsf"),
	MANTER_USUARIOS("Usuários", "fa-user-edit", "/admin/manter_usuario/painel_usuario.jsf"),
	MANTER_LOGS("Logs", "fa-database", "/admin/painel_log/painel_log.jsf"),
	PAINEL_FUNCIONALIDADES("Menu", "fa-th", "home.jsf");

	private static final Map<String, EPaginasAcesso> urlStringToEnum = new HashMap<String, EPaginasAcesso>();
	private static final Map<String, EPaginasAcesso> paginasStringToEnum = new HashMap<String, EPaginasAcesso>();

	private String nome = "";
	private String icone = "";
	private String url = "";

	EPaginasAcesso(String nome, String icone, String url) {
		this.nome = nome;
		this.icone = icone;
		this.url = url;
	}

	public String getNome() {
		return nome;
	}

	public String getIcone() {
		return icone;
	}

	public String getUrl() {
		return url;
	}
	

	static {
		for (EPaginasAcesso pagina: values())
			urlStringToEnum.put(pagina.getUrl(), pagina);
	}

	public static EPaginasAcesso fromUri(String url) {
		return urlStringToEnum.get(url);
	}

	static {
		for (EPaginasAcesso paginas: values())
			paginasStringToEnum.put(paginas.name(), paginas);
	}

	public static EPaginasAcesso fromPagina(String pagina) {
		return paginasStringToEnum.get(pagina);
	}

	public static EPaginasAcesso fromOrdinal(int id) {
		if (id < values().length)
			return values()[id];
		return null;
	}

}