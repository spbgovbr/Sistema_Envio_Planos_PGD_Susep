package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Log;

@Component
@SessionScope
public class LogForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Log logSelecionado;
	private List<Log> listaLog;
	private List<Log> listaFiltradaLog;
	
	
	public Log getLogSelecionado() {
		return logSelecionado;
	}
	public void setLogSelecionado(Log logSelecionado) {
		this.logSelecionado = logSelecionado;
	}
	
	public List<Log> getListaLog() {
		return listaLog;
	}
	public void setListaLog(List<Log> listaLog) {
		this.listaLog = listaLog;
	}

	public List<Log> getListaFiltradaLog() {
		return listaFiltradaLog;
	}
	public void setListaFiltradaLog(List<Log> listaFiltradaLog) {
		this.listaFiltradaLog = listaFiltradaLog;
	}

}