package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.gov.economia.apipgdsusep.dto.PlanoTrabalhoJson;
import br.gov.economia.apipgdsusep.entity.Atividade;
import br.gov.economia.apipgdsusep.entity.Log;
import br.gov.economia.apipgdsusep.entity.Pacto;
import br.gov.economia.apipgdsusep.entity.Produto;
import br.gov.economia.apipgdsusep.entity.SituacaoPacto;
import br.gov.economia.apipgdsusep.entity.TipoAtividade;
import br.gov.economia.apipgdsusep.entity.Unidade;
import br.gov.economia.apipgdsusep.enums.ENivelLog;
import br.gov.economia.apipgdsusep.enums.ENotaAvaliacao;
import br.gov.economia.apipgdsusep.enums.EQuantidadeEntrega;
import br.gov.economia.apipgdsusep.service.AtividadeService;
import br.gov.economia.apipgdsusep.service.LogService;
import br.gov.economia.apipgdsusep.service.SituacaoPactoService;
import br.gov.economia.apipgdsusep.service.TipoAtividadeService;
import br.gov.economia.apipgdsusep.service.UnidadeService;

@Component
@SessionScope
public class PainelApiPactosEnviadosForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Gson GSON = new GsonBuilder().create();
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private SituacaoPactoService situacaoPactoService;
	
	@Autowired
	private UnidadeService unidadeService;
	
	@Autowired
	private AtividadeService atividadeService;
	
	@Autowired
	private TipoAtividadeService tipoAtividadeService;
	
	private Pacto pactoPesquisa;
	private Pacto pactoSelecionado;
	
	private Produto produtoSelecionado;
	
	private List<String> listaIdRegistrosEnviados;
	private List<Pacto> listaPactosEnviados;
	
	private Unidade unidadeSelecionada;
	private Atividade atividadeSelecionada;
	private TipoAtividade tipoAtividadeSelecionado;
	
	private List<Unidade> listaUnidades;
	private List<Atividade> listaAtividades;
	private List<TipoAtividade> listaTiposAtividades;
	
	
	public Pacto getPactoPesquisa() {
		if(pactoPesquisa == null) {
			pactoPesquisa = new Pacto();
		}
		return pactoPesquisa;
	}
	public void setPactoPesquisa(Pacto pactoPesquisa) {
		this.pactoPesquisa = pactoPesquisa;
	}
	
	public Pacto getPactoSelecionado() {
		return pactoSelecionado;
	}
	public void setPactoSelecionado(Pacto pactoSelecionado) {
		this.pactoSelecionado = pactoSelecionado;
	}
	
	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}
	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
	public List<String> getListaIdRegistrosEnviados() {
		if(listaIdRegistrosEnviados == null) {
			listaIdRegistrosEnviados = new ArrayList<String>();
		}
		return listaIdRegistrosEnviados;
	}
	public void setListaIdRegistrosEnviados(List<String> listaIdRegistrosEnviados) {
		this.listaIdRegistrosEnviados = listaIdRegistrosEnviados;
	}
	
	public List<Pacto> getListaPactosEnviados() {
		return listaPactosEnviados;
	}
	public void setListaPactosEnviados(List<Pacto> listaPactosEnviados) {
		this.listaPactosEnviados = listaPactosEnviados;
	}
	
	public Unidade getUnidadeSelecionada() {
		return unidadeSelecionada;
	}
	public void setUnidadeSelecionada(Unidade unidadeSelecionada) {
		this.unidadeSelecionada = unidadeSelecionada;
	}
	
	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}
	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}
	
	public TipoAtividade getTipoAtividadeSelecionado() {
		return tipoAtividadeSelecionado;
	}
	public void setTipoAtividadeSelecionado(TipoAtividade tipoAtividadeSelecionado) {
		this.tipoAtividadeSelecionado = tipoAtividadeSelecionado;
	}
	
	public List<Unidade> getListaUnidades() {
		if(listaUnidades == null) {
			listaUnidades = unidadeService.pesquisarTodos();
		}
		return listaUnidades;
	}
	public void setListaUnidades(List<Unidade> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}
	
	public List<Atividade> getListaAtividades() {
		if(listaAtividades == null) {
			listaAtividades = atividadeService.pesquisarTodos();
		}
		return listaAtividades;
	}
	public void setListaAtividades(List<Atividade> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}
	
	public List<TipoAtividade> getListaTiposAtividades() {
		if(listaTiposAtividades == null) {
			listaTiposAtividades = tipoAtividadeService.pesquisarTodos();
		}
		return listaTiposAtividades;
	}
	public void setListaTiposAtividades(List<TipoAtividade> listaTiposAtividades) {
		this.listaTiposAtividades = listaTiposAtividades;
	}
	
	public List<ENotaAvaliacao> getNotasAvaliacao() {
		return Arrays.asList(ENotaAvaliacao.values());
	}
	
	public List<EQuantidadeEntrega> getQuantidadeEntregas() {
		return Arrays.asList(EQuantidadeEntrega.values());
	}
	
	public List<SituacaoPacto> getSituacoesPacto() {
		return situacaoPactoService.pesquisarTodos();
	}
	
	
	public void init() {
		this.setPactoPesquisa(null);
		this.setListaIdRegistrosEnviados(null);
		this.setListaPactosEnviados(null);
		this.setUnidadeSelecionada(null);
		this.setAtividadeSelecionada(null);
		this.setTipoAtividadeSelecionado(null);
		this.buscarRegistrosEnviados();
	}
	
	public void buscarRegistrosEnviados() {
		List<Log> logsUltimosRegistrosEnviados = logService.
				buscarRegistrosEnviados(ENivelLog.NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO);
		logsUltimosRegistrosEnviados.stream().forEach(log -> {
			PlanoTrabalhoJson plano = GSON.fromJson(log.getDescricaoAcao(), PlanoTrabalhoJson.class);
			plano.setUsuarioEnvio(log.getUsuario());
			plano.setDataEnvio(log.getDataAcao());
			this.getListaIdRegistrosEnviados().add(plano.getCod_plano());
		});
	}
	
	public List<Unidade> autoCompleteUnidade(String query) {
		List<Unidade> listaUnidades = this.getListaUnidades();
		List<Unidade> listaUnidadesFiltrada = new ArrayList<Unidade>();
		if(listaUnidades != null && listaUnidades.size() > 0) {
			for (int i = 0; i < listaUnidades.size(); i++) {
				Unidade unidade = listaUnidades.get(i);
				if(unidade.getNome().toLowerCase().trim().contains(query.toLowerCase().trim())) {
					listaUnidadesFiltrada.add(unidade);
				}
			}
		}
		return listaUnidadesFiltrada;
	}
	
	public void selecionarUnidade(SelectEvent event) {
		Unidade unidade = (Unidade) event.getObject();
		this.setUnidadeSelecionada(unidade);
		this.getPactoPesquisa().setSiglaUnidadeExercicio(unidade.getSigla());
	}
	
	public List<Atividade> autoCompleteAtividade(String query) {
		List<Atividade> listaAtividades = this.getListaAtividades();
		List<Atividade> listaAtividadesFiltrada = new ArrayList<Atividade>();
		if(listaAtividades != null && listaAtividades.size() > 0) {
			for (int i = 0; i < listaAtividades.size(); i++) {
				Atividade unidade = listaAtividades.get(i);
				if(unidade.getNome().toLowerCase().trim().contains(query.toLowerCase().trim())) {
					listaAtividadesFiltrada.add(unidade);
				}
			}
		}
		return listaAtividadesFiltrada;
	}
	
	public void selecionarAtividade(SelectEvent event) {
		Atividade atividade = (Atividade) event.getObject();
		this.setAtividadeSelecionada(atividade);
		this.getPactoPesquisa().setAtividade(atividade);
	}
	
	public List<TipoAtividade> autoCompleteTipoAtividade(String query) {
		List<TipoAtividade> listaTiposAtividade = this.getListaTiposAtividades();
		List<TipoAtividade> listaTiposAtividadeFiltrada = new ArrayList<TipoAtividade>();
		if(listaTiposAtividade != null && listaTiposAtividade.size() > 0) {
			for (int i = 0; i < listaTiposAtividade.size(); i++) {
				TipoAtividade unidade = listaTiposAtividade.get(i);
				if(unidade.getFaixaComplexidade().toLowerCase().trim().contains(query.toLowerCase().trim())) {
					listaTiposAtividadeFiltrada.add(unidade);
				}
			}
		}
		return listaTiposAtividadeFiltrada;
	}
	
	public void selecionarTipoAtividade(SelectEvent event) {
		TipoAtividade tipoAtividade = (TipoAtividade) event.getObject();
		this.setTipoAtividadeSelecionado(tipoAtividade);
		this.getPactoPesquisa().setTipoAtividade(tipoAtividade);
	}
	
}