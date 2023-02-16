package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.gov.economia.apipgdsusep.converter.PactoDataModel;
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
import br.gov.economia.apipgdsusep.service.PactoService;
import br.gov.economia.apipgdsusep.service.SituacaoPactoService;
import br.gov.economia.apipgdsusep.service.TipoAtividadeService;
import br.gov.economia.apipgdsusep.service.UnidadeService;

@Component
@SessionScope
public class PainelApiPgCadeForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Gson GSON = new GsonBuilder().create();
	
	@Autowired
	private PactoService pactoService;
	
	@Autowired
	private SituacaoPactoService situacaoPactoService;
	
	@Autowired
	private UnidadeService unidadeService;
	
	@Autowired
	private AtividadeService atividadeService;
	
	@Autowired
	private TipoAtividadeService tipoAtividadeService;
	
	@Autowired
	private LogService logService;
	
	
	private Pacto pactoPesquisa;
	private Pacto pactoSelecionado;
	
	private Produto produtoSelecionado;
	
	private List<Pacto> listaPacto;
	private List<Pacto> listaFiltradaPacto;
	
	private PactoDataModel pactoDataModel;
	private List<Pacto> listaPactoSelected;
	private Pacto pactoSelected;
	private boolean selectAll;
	
	private Unidade unidadeSelecionada;
	private Atividade atividadeSelecionada;
	private TipoAtividade tipoAtividadeSelecionado;
	
	private List<Unidade> listaUnidades;
	private List<Atividade> listaAtividades;
	private List<TipoAtividade> listaTiposAtividades;
	
	private Integer qtdPactosSelecionados;
	
	private List<PlanoTrabalhoJson> listaUltimosRegistrosEnviados;
	
	private List<String> listaIdRegistrosEnviados;
	
	private List<Log> listaUltimosRegistrosEnvioPlanos;
	
	
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
	
	public List<Pacto> getListaPacto() {
		return listaPacto;
	}
	public void setListaPacto(List<Pacto> listaPacto) {
		this.listaPacto = listaPacto;
	}
	
	public List<Pacto> getListaFiltradaPacto() {
		return listaFiltradaPacto;
	}
	public void setListaFiltradaPacto(List<Pacto> listaFiltradaPacto) {
		this.listaFiltradaPacto = listaFiltradaPacto;
	}
	
	public PactoDataModel getPactoDataModel() {
		return pactoDataModel;
	}
	public void setPactoDataModel(PactoDataModel pactoDataModel) {
		this.pactoDataModel = pactoDataModel;
	}
	
	public List<Pacto> getListaPactoSelected() {
		if(listaPactoSelected == null) {
			listaPactoSelected = new ArrayList<Pacto>();
		}
		return listaPactoSelected;
	}
	public void setListaPactoSelected(List<Pacto> listaPactoSelected) {
		this.listaPactoSelected = listaPactoSelected;
	}
	
	public Pacto getPactoSelected() {
		return pactoSelected;
	}
	public void setPactoSelected(Pacto pactoSelected) {
		this.pactoSelected = pactoSelected;
	}
	
	public boolean isSelectAll() {
		return selectAll;
	}
	public boolean getSelectAll() {
		return selectAll;
	}
	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
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
	
	public Integer getQtdPactosSelecionados() {
		return qtdPactosSelecionados;
	}
	public void setQtdPactosSelecionados(Integer qtdPactosSelecionados) {
		this.qtdPactosSelecionados = qtdPactosSelecionados;
	}
	
	public List<PlanoTrabalhoJson> getListaUltimosRegistrosEnviados() {
		if(listaUltimosRegistrosEnviados == null) {
			listaUltimosRegistrosEnviados = new ArrayList<PlanoTrabalhoJson>();
		}
		return listaUltimosRegistrosEnviados;
	}
	public void setListaUltimosRegistrosEnviados(List<PlanoTrabalhoJson> listaUltimosRegistrosEnviados) {
		this.listaUltimosRegistrosEnviados = listaUltimosRegistrosEnviados;
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
	
	public List<Log> getListaUltimosRegistrosEnvioPlanos() {
		if(listaUltimosRegistrosEnvioPlanos == null) {
			listaUltimosRegistrosEnvioPlanos = logService.buscarUltimosRegistrosEnvioPlanos();
		}
		return listaUltimosRegistrosEnvioPlanos;
	}
	public void setListaUltimosRegistrosEnvioPlanos(List<Log> listaUltimosRegistrosEnvioPlanos) {
		this.listaUltimosRegistrosEnvioPlanos = listaUltimosRegistrosEnvioPlanos;
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
		this.setPactoPesquisa(new Pacto());
		this.setPactoSelecionado(null);
		this.setListaPacto(null);
		this.setListaFiltradaPacto(null);
		this.setPactoDataModel(null);
		this.setListaPactoSelected(null);
		this.setPactoSelected(null);
		this.setSelectAll(false);
		this.setUnidadeSelecionada(null);
		this.setAtividadeSelecionada(null);
		this.setTipoAtividadeSelecionado(null);
		this.setQtdPactosSelecionados(null);
		this.setListaUltimosRegistrosEnviados(null);
		this.setListaIdRegistrosEnviados(null);
		this.setListaUltimosRegistrosEnvioPlanos(null);
		this.getListaUltimosRegistrosEnvioPlanos();
		this.buscarRegistrosEnviados();
		PrimeFaces.current().executeScript("PF('dataTableListaPactoWidgetVar').unselectAllRows()");
	}
	
	public void buscarUltimosRegistrosEnviados() {
		List<Log> logsUltimosRegistrosEnviados = logService.
				buscarUltimosRegistrosEnviados(ENivelLog.NIVEL_JSON_API_PG_CADE_PGD_ME_SUCESSO, 5);
		logsUltimosRegistrosEnviados.stream().forEach(log -> {
			PlanoTrabalhoJson plano = GSON.fromJson(log.getDescricaoAcao(), PlanoTrabalhoJson.class);
			plano.setUsuarioEnvio(log.getUsuario());
			plano.setDataEnvio(log.getDataAcao());
			this.getListaUltimosRegistrosEnviados().add(plano);
		});
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
	
	public int getQtdVezesRegistroFoiEnviado(Pacto pacto) {
		int count = 0;
		for(String id : this.getListaIdRegistrosEnviados()) {
			if(id.equals(pacto.getIdPacto())) {
				count++;
			}
		}
		return count;
	}
	
	public int getQtdPlanos() {
		return (this.getPactoDataModel() != null ? this.getPactoDataModel().getRowCount() : 0);
	}
	
	public int getQtdAtividades() {
		int countAtividades = 0;
		Iterator<Pacto> pactoIterator = this.getPactoDataModel().iterator();
		Pacto pacto = null;
		while(pactoIterator.hasNext()) {
			pacto = pactoIterator.next();
			for (int i = 0; i < pacto.getProdutos().size(); i++) {
				countAtividades++;
			}
		}
		return countAtividades;
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
	
	public void selectAllOrUnselectAllPactoDataModal(ToggleSelectEvent event) {
		this.setSelectAll(this.isSelectAll() ? false : true);
		if(this.getPactoDataModel() != null) {
			this.getPactoDataModel().forEach(p -> {
				if(this.isSelectAll()) {
					p.setSelecionado(true);
					PrimeFaces.current().executeScript("PF('dataTableListaPactoWidgetVar').selectAllRows()");
				} else {
					p.setSelecionado(false);
					PrimeFaces.current().executeScript("PF('dataTableListaPactoWidgetVar').unselectAllRows()");
				}
			});
		}
		this.countElements();
	}
	
	public void selectPactoDataModal(SelectEvent event) {
		Pacto pactoSelecionado = (Pacto) event.getObject();
		if(this.getPactoDataModel() != null && pactoSelecionado != null) {
			this.getPactoDataModel().forEach(p -> {
				if(p.getIdPacto().equals(pactoSelecionado.getIdPacto())) {
					p.setSelecionado(true);
				}
			});
		}
		this.countElements();
	}
	
	public void unselectPactoDataModal(UnselectEvent event) {
		Pacto pactoSelecionado = (Pacto) event.getObject();
		if(this.getPactoDataModel() != null && pactoSelecionado != null) {
			this.getPactoDataModel().forEach(p -> {
				if(p.getIdPacto().equals(pactoSelecionado.getIdPacto())) {
					p.setSelecionado(false);
				}
			});
		}
		this.countElements();
	}
	
	public List<Pacto> getListaPactoRenovada() {
		List<Pacto> listaPacto = new ArrayList<Pacto>();
		for(Pacto pacto : this.getPactoDataModel()) {
			listaPacto.add(pactoService.pesquisarPorId(pacto.getIdPacto()));
		}
		return listaPacto;
	}
	
	@SuppressWarnings("rawtypes")
	public void countElements() {
		int qtdTrue = 0, qtdFalse = 0;
		Iterator it = this.getPactoDataModel().iterator();
		while(it.hasNext()) {
			Pacto pacto = (Pacto) it.next();
			if(pacto.getSelecionado()) {
				qtdTrue++;
			} else {
				qtdFalse++;
			}
		}
		this.setQtdPactosSelecionados(new Integer(qtdTrue));
		System.out.println("Qtd. Elementos True: " + qtdTrue);
		System.out.println("Qtd. Elementos False: " + qtdFalse);
	}
	
}