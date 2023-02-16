package br.gov.economia.apipgdsusep.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;

import br.gov.economia.apipgdsusep.entity.Produto;

public class AtividadeJson {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private Integer id_atividade;
	private String nome_grupo_atividade;
	private String nome_atividade;
	private String faixa_complexidade;
	private String parametros_complexidade;
	private Integer tempo_presencial_estimado;
	private Integer tempo_presencial_programado;
	private Integer tempo_presencial_executado;
	private Integer tempo_teletrabalho_estimado;
	private Double tempo_teletrabalho_programado;
	private Integer tempo_teletrabalho_executado;
	private String entrega_esperada;
	private Integer qtde_entregas;
	private Integer qtde_entregas_efetivas;
	private Integer avaliacao;
	private String data_avaliacao;
	private String justificativa;
	
	
	public AtividadeJson() { }
	
	public AtividadeJson(Produto produto) {
		super();
		this.id_atividade = produto.getId();
		this.nome_grupo_atividade = produto.getNomeGrupoAtividade();
		this.nome_atividade = produto.getNomeAtividade();
		this.faixa_complexidade = produto.getFaixaComplexidade();
		this.parametros_complexidade = produto.getParametrosComplexidade();
		this.tempo_presencial_estimado = produto.getTempoPresencialEstimado();
		this.tempo_presencial_programado = produto.getTempoPresencialProgramado();
		this.tempo_presencial_executado = produto.getTempoPresencialExecutado();
		this.tempo_teletrabalho_estimado = produto.getTempoTeletrabalhoEstimado();
		this.tempo_teletrabalho_programado = produto.getTempoTeletrabalhoProgramado();
		this.tempo_teletrabalho_executado = produto.getTempoTeletrabalhoExecutado();
		this.entrega_esperada = stripHTML(produto.getEntregaEsperada());
		this.qtde_entregas = produto.getQuantidadeEntregas();
		this.qtde_entregas_efetivas = produto.getQuantidadeEntregasEfetivas();
		this.avaliacao = produto.getAvaliacao();
		this.data_avaliacao = (produto.getDataAvaliacao() != null ? DATE_FORMAT.format(produto.getDataAvaliacao()) : null);
		this.justificativa = stripHTML(produto.getJustificativa());
	}
	
	
	public String getRegistroLogNivelSistema() {
		return "Atividade [id_atividade = " + id_atividade + ", nome_grupo_atividade = " + nome_grupo_atividade
				+ ", nome_atividade = " + nome_atividade + ", faixa_complexidade = " + faixa_complexidade
				+ ", parametros_complexidade = " + parametros_complexidade 
				+ ", tempo_presencial_estimado = " + tempo_presencial_estimado + " horas" 
				+ ", tempo_presencial_programado = " + tempo_presencial_programado + " horas"
				+ ", tempo_presencial_executado = " + tempo_presencial_executado + " horas" 
				+ ", tempo_teletrabalho_estimado = " + tempo_teletrabalho_estimado + " horas"
				+ ", tempo_teletrabalho_programado = " + tempo_teletrabalho_programado + " horas"
				+ ", tempo_teletrabalho_executado = " + tempo_teletrabalho_executado + " horas"
				+ ", entrega_esperada = " + entrega_esperada + ", qtde_entregas = " + qtde_entregas 
				+ ", qtde_entregas_efetivas = " + qtde_entregas_efetivas + ", avaliacao = " + avaliacao 
				+ ", data_avaliacao=" + data_avaliacao + ", justificativa=" + justificativa + "]";
	}
	

	public static String stripHTML(String value) {
		StringBuilder out = new StringBuilder();
		if(value != null) {
			StringReader strReader = new StringReader(value);
			try {
				HTMLStripCharFilter html = new HTMLStripCharFilter(strReader.markSupported() ? strReader : new BufferedReader(strReader));
				char[] cbuf = new char[1024 * 10];
				while (true) {
					int count = html.read(cbuf);
					if (count == -1)
						break; // end of stream mark is -1
					if (count > 0)
						out.append(cbuf, 0, count);
				}
				html.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toString();
	}
	
	/*public static String stripHTML(String value) {
		String strRegEx = "<[^>]*>";
		value = value.replaceAll(strRegEx, "");
		value = value.replace("&nbsp;", " ");
		value = value.replace("&amp;", "&");            
		value = value.replaceAll("&.*?;", "");
		value = value.replaceAll("\r", "");
		value = value.replaceAll("\n", "");
		value = value.replaceAll("\t", "");
		return value;
	}*/
	
	
	public Integer getId_atividade() {
		return id_atividade;
	}
	public void setId_atividade(Integer id_atividade) {
		this.id_atividade = id_atividade;
	}
	
	public String getNome_grupo_atividade() {
		return nome_grupo_atividade;
	}
	public void setNome_grupo_atividade(String nome_grupo_atividade) {
		this.nome_grupo_atividade = nome_grupo_atividade;
	}
	
	public String getNome_atividade() {
		return nome_atividade;
	}
	public void setNome_atividade(String nome_atividade) {
		this.nome_atividade = nome_atividade;
	}
	
	public String getFaixa_complexidade() {
		return faixa_complexidade;
	}
	public void setFaixa_complexidade(String faixa_complexidade) {
		this.faixa_complexidade = faixa_complexidade;
	}
	
	public String getParametros_complexidade() {
		return parametros_complexidade;
	}
	public void setParametros_complexidade(String parametros_complexidade) {
		this.parametros_complexidade = parametros_complexidade;
	}
	
	public Integer getTempo_presencial_estimado() {
		return tempo_presencial_estimado;
	}
	public void setTempo_presencial_estimado(Integer tempo_presencial_estimado) {
		this.tempo_presencial_estimado = tempo_presencial_estimado;
	}

	public Integer getTempo_presencial_programado() {
		return tempo_presencial_programado;
	}
	public void setTempo_presencial_programado(Integer tempo_presencial_programado) {
		this.tempo_presencial_programado = tempo_presencial_programado;
	}

	public Integer getTempo_presencial_executado() {
		return tempo_presencial_executado;
	}
	public void setTempo_presencial_executado(Integer tempo_presencial_executado) {
		this.tempo_presencial_executado = tempo_presencial_executado;
	}

	public Integer getTempo_teletrabalho_estimado() {
		return tempo_teletrabalho_estimado;
	}
	public void setTempo_teletrabalho_estimado(Integer tempo_teletrabalho_estimado) {
		this.tempo_teletrabalho_estimado = tempo_teletrabalho_estimado;
	}

	public Double getTempo_teletrabalho_programado() {
		return tempo_teletrabalho_programado;
	}
	public void setTempo_teletrabalho_programado(Double tempo_teletrabalho_programado) {
		this.tempo_teletrabalho_programado = tempo_teletrabalho_programado;
	}

	public Integer getTempo_teletrabalho_executado() {
		return tempo_teletrabalho_executado;
	}
	public void setTempo_teletrabalho_executado(Integer tempo_teletrabalho_executado) {
		this.tempo_teletrabalho_executado = tempo_teletrabalho_executado;
	}

	public String getEntrega_esperada() {
		return entrega_esperada;
	}
	public void setEntrega_esperada(String entrega_esperada) {
		this.entrega_esperada = entrega_esperada;
	}
	
	public Integer getQtde_entregas() {
		return qtde_entregas;
	}
	public void setQtde_entregas(Integer qtde_entregas) {
		this.qtde_entregas = qtde_entregas;
	}
	
	public Integer getQtde_entregas_efetivas() {
		return qtde_entregas_efetivas;
	}
	public void setQtde_entregas_efetivas(Integer qtde_entregas_efetivas) {
		this.qtde_entregas_efetivas = qtde_entregas_efetivas;
	}
	
	public Integer getAvaliacao() {
		return avaliacao;
	}
	public void setAvaliacao(Integer avaliacao) {
		this.avaliacao = avaliacao;
	}
	
	public String getData_avaliacao() {
		return data_avaliacao;
	}
	public void setData_avaliacao(String data_avaliacao) {
		this.data_avaliacao = data_avaliacao;
	}
	
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
}