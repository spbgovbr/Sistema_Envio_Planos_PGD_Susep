package br.gov.economia.apipgdsusep.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="vw_produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@Column(name="id_produto", columnDefinition="uniqueidentifier")
	private String idProduto;
	
	@ManyToOne
	@JoinColumn(name="id_pacto")
	private Pacto pacto;
	
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@Column(name="id_atividade", columnDefinition="uniqueidentifier")
	private String idAtividade;
	
	@Column(name="nome_grupo_atividade")
	private String nomeGrupoAtividade;

	@Column(name="nome_atividade")
	private String nomeAtividade;
	
	@Column(name="faixa_complexidade")
	private String faixaComplexidade;
	
	@Column(name="parametros_complexidade")
	private String parametrosComplexidade;
	
	@Column(name="tempo_presencial_estimado")
	private Integer tempoPresencialEstimado;
	
	@Column(name="tempo_presencial_programado")
	private Integer tempoPresencialProgramado;
	
	@Column(name="tempo_presencial_executado")
	private Integer tempoPresencialExecutado;
	
	@Column(name="tempo_teletrabalho_estimado")
	private Integer tempoTeletrabalhoEstimado;
	
	@Column(name="tempo_teletrabalho_programado")
	private Double tempoTeletrabalhoProgramado;
	
	@Column(name="tempo_teletrabalho_executado")
	private Integer tempoTeletrabalhoExecutado;
	
	@Column(name="entrega_esperada")
	private String entregaEsperada;
	
	@Column(name="qtde_entregas")
	private Integer quantidadeEntregas;
	
	@Column(name="qtde_entregas_efetivas")
	private Integer quantidadeEntregasEfetivas;
	
	@Column(name="avaliacao")
	private Integer avaliacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_avaliacao")
	private Date dataAvaliacao;
	
	@Column(name="justificativa")
	private String justificativa;
	
	
	public Produto() { }


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}
	
	public Pacto getPacto() {
		return pacto;
	}
	public void setPacto(Pacto pacto) {
		this.pacto = pacto;
	}
	
	public String getIdAtividade() {
		return idAtividade;
	}
	public void setIdAtividade(String idAtividade) {
		this.idAtividade = idAtividade;
	}

	public String getNomeGrupoAtividade() {
		return nomeGrupoAtividade;
	}

	public void setNomeGrupoAtividade(String nomeGrupoAtividade) {
		this.nomeGrupoAtividade = nomeGrupoAtividade;
	}
	public String getNomeAtividade() {
		return nomeAtividade;
	}

	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}
	public String getFaixaComplexidade() {
		return faixaComplexidade;
	}

	public void setFaixaComplexidade(String faixaComplexidade) {
		this.faixaComplexidade = faixaComplexidade;
	}

	public String getParametrosComplexidade() {
		return parametrosComplexidade;
	}
	public void setParametrosComplexidade(String parametrosComplexidade) {
		this.parametrosComplexidade = parametrosComplexidade;
	}

	public Integer getTempoPresencialEstimado() {
		return tempoPresencialEstimado;
	}
	public void setTempoPresencialEstimado(Integer tempoPresencialEstimado) {
		this.tempoPresencialEstimado = tempoPresencialEstimado;
	}

	public Integer getTempoPresencialProgramado() {
		return tempoPresencialProgramado;
	}
	public void setTempoPresencialProgramado(Integer tempoPresencialProgramado) {
		this.tempoPresencialProgramado = tempoPresencialProgramado;
	}

	public Integer getTempoPresencialExecutado() {
		return tempoPresencialExecutado;
	}
	public void setTempoPresencialExecutado(Integer tempoPresencialExecutado) {
		this.tempoPresencialExecutado = tempoPresencialExecutado;
	}

	public Integer getTempoTeletrabalhoEstimado() {
		return tempoTeletrabalhoEstimado;
	}
	public void setTempoTeletrabalhoEstimado(Integer tempoTeletrabalhoEstimado) {
		this.tempoTeletrabalhoEstimado = tempoTeletrabalhoEstimado;
	}

	public Double getTempoTeletrabalhoProgramado() {
		return tempoTeletrabalhoProgramado;
	}
	public void setTempoTeletrabalhoProgramado(Double tempoTeletrabalhoProgramado) {
		this.tempoTeletrabalhoProgramado = tempoTeletrabalhoProgramado;
	}

	public Integer getTempoTeletrabalhoExecutado() {
		return tempoTeletrabalhoExecutado;
	}
	public void setTempoTeletrabalhoExecutado(Integer tempoTeletrabalhoExecutado) {
		this.tempoTeletrabalhoExecutado = tempoTeletrabalhoExecutado;
	}

	public String getEntregaEsperada() {
		return entregaEsperada;
	}
	public void setEntregaEsperada(String entregaEsperada) {
		this.entregaEsperada = entregaEsperada;
	}

	public Integer getQuantidadeEntregas() {
		return quantidadeEntregas;
	}
	public void setQuantidadeEntregas(Integer quantidadeEntregas) {
		this.quantidadeEntregas = quantidadeEntregas;
	}

	public Integer getQuantidadeEntregasEfetivas() {
		return quantidadeEntregasEfetivas;
	}
	public void setQuantidadeEntregasEfetivas(Integer quantidadeEntregasEfetivas) {
		this.quantidadeEntregasEfetivas = quantidadeEntregasEfetivas;
	}

	public Integer getAvaliacao() {
		return avaliacao;
	}
	public void setAvaliacao(Integer avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Date getDataAvaliacao() {
		return dataAvaliacao;
	}
	public void setDataAvaliacao(Date dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}

	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}