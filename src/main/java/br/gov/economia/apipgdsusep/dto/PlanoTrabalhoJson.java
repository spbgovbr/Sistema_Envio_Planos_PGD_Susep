package br.gov.economia.apipgdsusep.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.economia.apipgdsusep.entity.Pacto;
import br.gov.economia.apipgdsusep.entity.Produto;
import br.gov.economia.apipgdsusep.entity.Usuario;

public class PlanoTrabalhoJson {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private String cod_plano;
	private String situacao;
	private String matricula_siape;
	private String cpf;
	private String nome_participante;
	private Integer cod_unidade_exercicio;
	private String nome_unidade_exercicio;
	private Integer modalidade_execucao;
	private Double carga_horaria_semanal;
	private String data_inicio;
	private String data_fim;
	private Double carga_horaria_total;
	private String data_interrupcao;
	private String entregue_no_prazo;
	private Double horas_homologadas;
	private List<AtividadeJson> atividades;
	
	@JsonIgnore
	private Usuario usuarioEnvio;
	
	@JsonIgnore
	private Date dataEnvio;
	

	public PlanoTrabalhoJson() { }

	public PlanoTrabalhoJson(Pacto pacto) {
		super();
		this.cod_plano = pacto.getIdPacto();
		this.situacao = pacto.getSituacao();
		this.matricula_siape = pacto.getMatriculaSiape();
		this.cpf = pacto.getCpf();
		this.nome_participante = pacto.getNomeParticipante();
		this.cod_unidade_exercicio = pacto.getCodigoUnidadeExercicio();
		this.nome_unidade_exercicio = pacto.getNomeUnidadeExercicio();
		this.modalidade_execucao = pacto.getModalidadeExecucao();
		this.carga_horaria_semanal = pacto.getCargaHorariaSemanal();
		this.data_inicio = (pacto.getDataInicio() != null ? DATE_FORMAT.format(pacto.getDataInicio()) : null);
		this.data_fim = (pacto.getDataFim() != null ? DATE_FORMAT.format(pacto.getDataFim()) : null);
		this.carga_horaria_total = pacto.getCargaHorariaTotal();
		this.data_interrupcao = (pacto.getDataInterrupcao() != null ? DATE_FORMAT.format(pacto.getDataInterrupcao()) : null);
		this.entregue_no_prazo = pacto.getEntregueNoPrazo();
		this.horas_homologadas = pacto.getHorasHomologadas();
		this.atividades = new ArrayList<AtividadeJson>();
		for(Produto produto : pacto.getProdutos()) {
			atividades.add(new AtividadeJson(produto));
		}
	}
	
	
	public String getRegistroLogNivelUsuario() {
		return "O Plano de Trabalho [" + cod_plano + "] do participante " + nome_participante 
				+ " lotado na unidade " + nome_unidade_exercicio + " foi enviado ao Ministério da Economia " 
				+ "pelo usuário(a) " + usuarioEnvio.getPessoa().getNome() + " no dia "
				+ (dataEnvio != null ? new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss").format(dataEnvio) : "") + ".";
	}
	
	public String getRegistroLogNivelSistema() {
		StringBuilder sb = new StringBuilder();
		if(atividades != null && atividades.size() > 0) {
			atividades.stream().forEach(at -> sb.append(at.getRegistroLogNivelSistema() + ", "));
		}
		return "Plano Trabalho [cod_plano = " + cod_plano + ", situacao = " + situacao + ", matricula_siape = " + matricula_siape 
				+ ", cpf = " + cpf + ", nome_participante = " + nome_participante + ", cod_unidade_exercicio = " + cod_unidade_exercicio
				+ ", nome_unidade_exercicio = " + nome_unidade_exercicio + ", modalidade_execucao = " + modalidade_execucao
				+ ", carga_horaria_semanal = " + carga_horaria_semanal + " horas"
				+ ", data_inicio = " + data_inicio + ", data_fim = " + data_fim 
				+ ", carga_horaria_total = " + carga_horaria_total + " horas" 
				+ ", data_interrupcao = " + data_interrupcao 
				+ ", entregue_no_prazo = " + (entregue_no_prazo != null ? (Boolean.parseBoolean(entregue_no_prazo) ? "Sim" : "Não") : "")
				+ ", horas_homologadas = " + horas_homologadas + " horas"
				+ ", atividades = " + (sb.toString().length() >= 2 ? sb.toString().substring(0, sb.toString().length() - 2) : "") + "]";
	}
	

	public String getCod_plano() {
		return cod_plano;
	}
	public void setCod_plano(String cod_plano) {
		this.cod_plano = cod_plano;
	}
	
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMatricula_siape() {
		return matricula_siape;
	}
	public void setMatricula_siape(String matricula_siape) {
		this.matricula_siape = matricula_siape;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome_participante() {
		return nome_participante;
	}
	public void setNome_participante(String nome_participante) {
		this.nome_participante = nome_participante;
	}

	public Integer getCod_unidade_exercicio() {
		return cod_unidade_exercicio;
	}
	public void setCod_unidade_exercicio(Integer cod_unidade_exercicio) {
		this.cod_unidade_exercicio = cod_unidade_exercicio;
	}

	public String getNome_unidade_exercicio() {
		return nome_unidade_exercicio;
	}
	public void setNome_unidade_exercicio(String nome_unidade_exercicio) {
		this.nome_unidade_exercicio = nome_unidade_exercicio;
	}

	public Integer getModalidade_execucao() {
		return modalidade_execucao;
	}
	public void setModalidade_execucao(Integer modalidade_execucao) {
		this.modalidade_execucao = modalidade_execucao;
	}

	public Double getCarga_horaria_semanal() {
		return carga_horaria_semanal;
	}
	public void setCarga_horaria_semanal(Double carga_horaria_semanal) {
		this.carga_horaria_semanal = carga_horaria_semanal;
	}

	public String getData_inicio() {
		return data_inicio;
	}
	public void setData_inicio(String data_inicio) {
		this.data_inicio = data_inicio;
	}

	public String getData_fim() {
		return data_fim;
	}
	public void setData_fim(String data_fim) {
		this.data_fim = data_fim;
	}

	public Double getCarga_horaria_total() {
		return carga_horaria_total;
	}
	public void setCarga_horaria_total(Double carga_horaria_total) {
		this.carga_horaria_total = carga_horaria_total;
	}

	public String getData_interrupcao() {
		return data_interrupcao;
	}
	public void setData_interrupcao(String data_interrupcao) {
		this.data_interrupcao = data_interrupcao;
	}

	public String getEntregue_no_prazo() {
		return entregue_no_prazo;
	}
	public void setEntregue_no_prazo(String entregue_no_prazo) {
		this.entregue_no_prazo = entregue_no_prazo;
	}

	public Double getHoras_homologadas() {
		return horas_homologadas;
	}
	public void setHoras_homologadas(Double horas_homologadas) {
		this.horas_homologadas = horas_homologadas;
	}

	public List<AtividadeJson> getAtividades() {
		return atividades;
	}
	public void setAtividades(List<AtividadeJson> atividades) {
		this.atividades = atividades;
	}
	
	public Usuario getUsuarioEnvio() {
		return usuarioEnvio;
	}
	public void setUsuarioEnvio(Usuario usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}
	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

}