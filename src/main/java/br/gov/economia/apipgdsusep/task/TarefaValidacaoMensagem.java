package br.gov.economia.apipgdsusep.task;

public class TarefaValidacaoMensagem {

	private boolean resultado;
	private String metodo;
	private String mensagem;
	

	public TarefaValidacaoMensagem() { }
	
	public TarefaValidacaoMensagem(boolean resultado, String metodo, String mensagem) {
		super();
		this.resultado = resultado;
		this.metodo = metodo;
		this.mensagem = mensagem;
	}
	
	
	public boolean isResultado() {
		return resultado;
	}
	public boolean getResultado() {
		return resultado;
	}
	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}
	
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}