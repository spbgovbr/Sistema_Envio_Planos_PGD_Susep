package br.gov.economia.apipgdsusep.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Pacto;

@Component
@SessionScope
public class TarefaValidacaoEnvioDados {
	
	private boolean podeExibirBotaoEnvioDados;
	private List<TarefaValidacaoMensagem> listaTarefaValidacaoMensagem = null;
	
	public boolean iniciarTarefasValidacao(Iterator<Pacto> listaPlanosSelecionados) {
		//this.setListaTarefaValidacaoMensagem(new ArrayList<TarefaValidacaoMensagem>());
		//tarefaValidarTodosPlanosAvaliados(listaPlanosSelecionados);
		return true;
	}
	
	public void tarefaValidarTodosPlanosAvaliados(Iterator<Pacto> listaPlanosSelecionados) {
		TarefaValidacaoMensagem tarefa = null;
		Pacto pacto = null;
		while(listaPlanosSelecionados.hasNext()) {
			pacto = (Pacto) listaPlanosSelecionados.next();
			if(!pacto.getDescricaoSituacaoPacto().equals("Avaliado")) {
				tarefa = new TarefaValidacaoMensagem(false, "tarefaValidarTodosPlanosAvaliados", 
						"Existem Planos de Trabalho que ainda não foram avaliados. Deseja enviá-los mesmo assim?");
				this.getListaTarefaValidacaoMensagem().add(tarefa);
				return;
			}
		}
		tarefa = new TarefaValidacaoMensagem(true, "tarefaValidarTodosPlanosAvaliados", "Todos os Planos de Trabalho foram avaliados.");
		this.getListaTarefaValidacaoMensagem().add(tarefa);
	}
	
	
	public boolean getPodeExibirBotaoEnvioDados() {
		podeExibirBotaoEnvioDados = true;
		if(listaTarefaValidacaoMensagem != null) {
			listaTarefaValidacaoMensagem.stream().forEach(tf -> {
				if(!tf.getResultado()) {
					podeExibirBotaoEnvioDados = false;
				}
			});
		}
		return podeExibirBotaoEnvioDados;
	}

	public List<TarefaValidacaoMensagem> getListaTarefaValidacaoMensagem() {
		if(listaTarefaValidacaoMensagem == null) {
			listaTarefaValidacaoMensagem = new ArrayList<TarefaValidacaoMensagem>();
		}
		return listaTarefaValidacaoMensagem;
	}
	public void setListaTarefaValidacaoMensagem(List<TarefaValidacaoMensagem> listaTarefaValidacaoMensagem) {
		this.listaTarefaValidacaoMensagem = listaTarefaValidacaoMensagem;
	}
	
}