package br.gov.economia.apipgdsusep.utils;

import java.util.Map;

public class Mail {

	private String from;
	private String recipientsTO;
	private String recipientsCC;
	private String subject;
	private String template;
	private Map<String, Object> modelo;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getRecipientsTO() {
		return recipientsTO;
	}
	public void setRecipientsTO(String recipientsTO) {
		this.recipientsTO = recipientsTO;
	}
	
	public String getRecipientsCC() {
		return recipientsCC;
	}
	public void setRecipientsCC(String recipientsCC) {
		this.recipientsCC = recipientsCC;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public Map<String, Object> getModelo() {
		return modelo;
	}
	public void setModelo(Map<String, Object> modelo) {
		this.modelo = modelo;
	}
	
}