package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import br.gov.economia.apipgdsusep.enums.EModalidadeExecucao;
import br.gov.economia.apipgdsusep.enums.ENotaAvaliacao;
import br.gov.economia.apipgdsusep.enums.EQuantidadeEntrega;

@Entity
@Table(name="vw_pacto")
public class Pacto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@Column(name="id_pacto", columnDefinition="uniqueidentifier")
	private String idPacto;
	
	@Column(name="situacao")
	private String situacao; 
	
	@Column(name="matricula_siape")
	private String matriculaSiape; 

	@Column(name="cpf")
	private String cpf;
	
	@Column(name="nome_participante")
	private String nomeParticipante;
	
	@Column(name="cod_unidade_exercicio")
	private Integer codigoUnidadeExercicio;
	
	@Column(name="nome_unidade_exercicio")
	private String nomeUnidadeExercicio;
	
	@Column(name="sigla_unidade_exercicio")
	private String siglaUnidadeExercicio;
	
	@Column(name="desc_situacao_pacto")
	private String descricaoSituacaoPacto;
	
	@Column(name="modalidade_execucao")
	private Integer modalidadeExecucao;
	
	@Column(name="carga_horaria_semanal")
	private Double cargaHorariaSemanal;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_inicio")
	private Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_fim")
	private Date dataFim;
	
	@Column(name="carga_horaria_total")
	private Double cargaHorariaTotal;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_interrupcao")
	private Date dataInterrupcao;
	
	@Column(name="entregue_no_prazo")
	private String entregueNoPrazo;
	
	@Column(name="horas_homologadas")
	private Double horasHomologadas;
	
	@OneToMany(mappedBy = "pacto", fetch = FetchType.LAZY)
    private List <Produto> produtos;
	
	@Transient
	private boolean isSelecionado;
	
	@Transient
	private Date dataInicialPeriodoInicio;
	
	@Transient
	private Date dataFinalPeriodoInicio;
	
	@Transient
	private Date dataInicialPeriodoFim;
	
	@Transient
	private Date dataFinalPeriodoFim;
	
	@Transient
	private Atividade atividade;
	
	@Transient
	private TipoAtividade tipoAtividade;
	
	@Transient
	private SituacaoPacto situacaoPacto;
	
	@Transient
	private ENotaAvaliacao notaAvaliacao;
	
	@Transient
	private Date dataAvaliacao;
	
	@Transient
	private EQuantidadeEntrega quantidadeEntregas;
	
	@Transient
	private EQuantidadeEntrega quantidadeEntregasEfetivas;
	
	@Transient
	private EModalidadeExecucao modalidadeExecucaoEnum;
	
	
	public Pacto() { }
	

	public String getIdPacto() {
		return idPacto;
	}
	public void setIdPacto(String idPacto) {
		this.idPacto = idPacto;
	}
	
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMatriculaSiape() {
		return matriculaSiape;
	}
	public void setMatriculaSiape(String matriculaSiape) {
		this.matriculaSiape = matriculaSiape;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNomeParticipante() {
		return nomeParticipante;
	}
	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}

	public Integer getCodigoUnidadeExercicio() {
		return codigoUnidadeExercicio;
	}
	public void setCodigoUnidadeExercicio(Integer codigoUnidadeExercicio) {
		this.codigoUnidadeExercicio = codigoUnidadeExercicio;
	}

	public String getNomeUnidadeExercicio() {
		return nomeUnidadeExercicio;
	}
	public void setNomeUnidadeExercicio(String nomeUnidadeExercicio) {
		this.nomeUnidadeExercicio = nomeUnidadeExercicio;
	}

	public String getSiglaUnidadeExercicio() {
		return siglaUnidadeExercicio;
	}
	public void setSiglaUnidadeExercicio(String siglaUnidadeExercicio) {
		this.siglaUnidadeExercicio = siglaUnidadeExercicio;
	}
	
	public String getDescricaoSituacaoPacto() {
		return descricaoSituacaoPacto;
	}
	public void setDescricaoSituacaoPacto(String descricaoSituacaoPacto) {
		this.descricaoSituacaoPacto = descricaoSituacaoPacto;
	}

	public Integer getModalidadeExecucao() {
		return modalidadeExecucao;
	}
	public void setModalidadeExecucao(Integer modalidadeExecucao) {
		this.modalidadeExecucao = modalidadeExecucao;
	}

	public Double getCargaHorariaSemanal() {
		return cargaHorariaSemanal;
	}
	public void setCargaHorariaSemanal(Double cargaHorariaSemanal) {
		this.cargaHorariaSemanal = cargaHorariaSemanal;
	}

	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Double getCargaHorariaTotal() {
		return cargaHorariaTotal;
	}
	public void setCargaHorariaTotal(Double cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}

	public Date getDataInterrupcao() {
		return dataInterrupcao;
	}
	public void setDataInterrupcao(Date dataInterrupcao) {
		this.dataInterrupcao = dataInterrupcao;
	}

	public String getEntregueNoPrazo() {
		return entregueNoPrazo;
	}
	public void setEntregueNoPrazo(String entregueNoPrazo) {
		this.entregueNoPrazo = entregueNoPrazo;
	}

	public Double getHorasHomologadas() {
		return horasHomologadas;
	}
	public void setHorasHomologadas(Double horasHomologadas) {
		this.horasHomologadas = horasHomologadas;
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	public boolean isSelecionado() {
		return isSelecionado;
	}
	public boolean getSelecionado() {
		return isSelecionado;
	}
	public void setSelecionado(boolean isSelecionado) {
		this.isSelecionado = isSelecionado;
	}
	
	public Date getDataInicialPeriodoInicio() {
		return dataInicialPeriodoInicio;
	}
	public void setDataInicialPeriodoInicio(Date dataInicialPeriodoInicio) {
		this.dataInicialPeriodoInicio = dataInicialPeriodoInicio;
	}

	public Date getDataFinalPeriodoInicio() {
		return dataFinalPeriodoInicio;
	}
	public void setDataFinalPeriodoInicio(Date dataFinalPeriodoInicio) {
		this.dataFinalPeriodoInicio = dataFinalPeriodoInicio;
	}

	public Date getDataInicialPeriodoFim() {
		return dataInicialPeriodoFim;
	}
	public void setDataInicialPeriodoFim(Date dataInicialPeriodoFim) {
		this.dataInicialPeriodoFim = dataInicialPeriodoFim;
	}

	public Date getDataFinalPeriodoFim() {
		return dataFinalPeriodoFim;
	}
	public void setDataFinalPeriodoFim(Date dataFinalPeriodoFim) {
		this.dataFinalPeriodoFim = dataFinalPeriodoFim;
	}

	public Atividade getAtividade() {
		return atividade;
	}
	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}
	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}
	
	public SituacaoPacto getSituacaoPacto() {
		return situacaoPacto;
	}
	public void setSituacaoPacto(SituacaoPacto situacaoPacto) {
		this.situacaoPacto = situacaoPacto;
	}
	
	public ENotaAvaliacao getNotaAvaliacao() {
		return notaAvaliacao;
	}
	public void setNotaAvaliacao(ENotaAvaliacao notaAvaliacao) {
		this.notaAvaliacao = notaAvaliacao;
	}
	
	public Date getDataAvaliacao() {
		return dataAvaliacao;
	}
	public void setDataAvaliacao(Date dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}
	
	public EQuantidadeEntrega getQuantidadeEntregas() {
		return quantidadeEntregas;
	}
	public void setQuantidadeEntregas(EQuantidadeEntrega quantidadeEntregas) {
		this.quantidadeEntregas = quantidadeEntregas;
	}

	public EQuantidadeEntrega getQuantidadeEntregasEfetivas() {
		return quantidadeEntregasEfetivas;
	}
	public void setQuantidadeEntregasEfetivas(EQuantidadeEntrega quantidadeEntregasEfetivas) {
		this.quantidadeEntregasEfetivas = quantidadeEntregasEfetivas;
	}
	
	public EModalidadeExecucao getModalidadeExecucaoEnum() {
		if(modalidadeExecucaoEnum == null) {
			modalidadeExecucaoEnum = EModalidadeExecucao.fromModalidadeExecucao(modalidadeExecucao);
		}
		return modalidadeExecucaoEnum;
	}
	public void setModalidadeExecucaoEnum(EModalidadeExecucao modalidadeExecucaoEnum) {
		this.modalidadeExecucaoEnum = modalidadeExecucaoEnum;
	}
	
	
	public static final Comparator<Pacto> IdComparator = new Comparator<Pacto>() {
		@Override
		public int compare(final Pacto obj1, final Pacto obj2) {
			// Ordenação Ascendente
			return obj1.getIdPacto().compareTo(obj2.getIdPacto());
		}
	};


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPacto == null) ? 0 : idPacto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pacto other = (Pacto) obj;
		if (idPacto == null) {
			if (other.idPacto != null)
				return false;
		} else if (!idPacto.equals(other.idPacto))
			return false;
		return true;
	}
	
}