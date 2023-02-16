package br.gov.economia.apipgdsusep.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.entity.Usuario;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.service.PerfilService;
import br.gov.economia.apipgdsusep.service.UsuarioService;
import br.gov.economia.apipgdsusep.utils.AjudanteMensagens;

@Component
@SessionScope
public class UsuarioForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private AjudanteMensagens ajudanteMensagens;
	
	private Usuario usuarioSalvo;
	private Usuario usuarioSelecionado;
	
	private List<Usuario> listaUsuario;
	private List<Usuario> listaFiltradaUsuario;
	
	private DualListModel<Perfil> perfisDoUsuarioLista;
	
	
	public Usuario getUsuarioSalvo() {
		return usuarioSalvo;
	}
	public void setUsuarioSalvo(Usuario usuarioSalvo) {
		this.usuarioSalvo = usuarioSalvo;
	}
	
	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}
	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}
	
	public List<Usuario> getListaUsuario() {
		return listaUsuario;
	}
	public void setListaUsuario(List<Usuario> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}
	
	public List<Usuario> getListaFiltradaUsuario() {
		return listaFiltradaUsuario;
	}
	public void setListaFiltradaUsuario(List<Usuario> listaFiltradaUsuario) {
		this.listaFiltradaUsuario = listaFiltradaUsuario;
	}
	
	public DualListModel<Perfil> getPerfisDoUsuarioLista() {
		if(perfisDoUsuarioLista == null) {
			perfisDoUsuarioLista = new DualListModel<Perfil>();
		}
		return perfisDoUsuarioLista;
	}
	public void setPerfisDoUsuarioLista(DualListModel<Perfil> perfisDoUsuarioLista) {
		this.perfisDoUsuarioLista = perfisDoUsuarioLista;
	}
	
	public List<ESituacaoRegistro> getSituacoesRegistro() {
		return Arrays.asList(ESituacaoRegistro.values());
	}
	
	public void novoUsuario() {
		usuarioSalvo = new Usuario(ESituacaoRegistro.ATIVO);
	}
	
	public void popularPerfis(Usuario usuario) {
		usuarioSelecionado = usuarioService.pesquisarPorId(usuario.getId());
		List<Perfil> listaPerfis = perfilService.pesquisarPorFiltros(new Perfil(ESituacaoRegistro.ATIVO));
		List<Perfil> listaPerfisDisponiveis = new ArrayList<Perfil>();
		List<Perfil> perfisDoUsuarioSelecionadoLista = new ArrayList<Perfil>();
		perfisDoUsuarioSelecionadoLista.addAll(usuario.getPerfis());
		if(listaPerfis != null && listaPerfis.size() > 0) {
			for(Perfil perfilDisponivel : listaPerfis) {
				listaPerfisDisponiveis.add(perfilDisponivel);
			}
			Iterator<Perfil> perfisDisponiveisIterator = listaPerfisDisponiveis.iterator();
			while(perfisDisponiveisIterator.hasNext()) {
				Perfil perfilDisponivel = perfisDisponiveisIterator.next();
				for(Perfil perfilDoUsuarioSelecionado : usuarioSelecionado.getPerfis()) {
					if(perfilDisponivel.equals(perfilDoUsuarioSelecionado) 
							&& perfilDoUsuarioSelecionado.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)) {
						perfisDisponiveisIterator.remove();
					}
				}
			}
			Iterator<Perfil> perfisDoUsuarioIterator = perfisDoUsuarioSelecionadoLista.iterator();
			while(perfisDoUsuarioIterator.hasNext()) {
				Perfil perfilDisponivel = perfisDoUsuarioIterator.next();
				for(Perfil perfilDoUsuarioSelecionado : usuarioSelecionado.getPerfis()) {
					if(perfilDisponivel.equals(perfilDoUsuarioSelecionado) 
							&& perfilDoUsuarioSelecionado.getSituacaoRegistro().equals(ESituacaoRegistro.INATIVO)) {
						perfisDoUsuarioIterator.remove();
					}
				}
			}
		}
		perfisDoUsuarioLista = new DualListModel<Perfil>(listaPerfisDisponiveis, perfisDoUsuarioSelecionadoLista);
	}

	public void transferirPerfil(TransferEvent event) {
		try {
			String mensagem = (event.getItems().size() == 1 ? 
					"Transferência realizada com sucesso!" : "Transferências realizadas com sucesso!");
			for(Perfil perfilDoUsuario : perfisDoUsuarioLista.getSource()) {
				if(usuarioSelecionado.getPerfis().contains(perfilDoUsuario))
					usuarioSelecionado.getPerfis().remove(perfilDoUsuario);
			}
			for(Perfil perfil : perfisDoUsuarioLista.getTarget()) {

				if(!usuarioSelecionado.getPerfis().contains(perfil))
					usuarioSelecionado.getPerfis().add(perfil);
			}
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_INFO, "Informação!", mensagem);
		} catch (Exception e) {
			ajudanteMensagens.exibirMensagem(FacesMessage.SEVERITY_ERROR, 
					"Erro!", "Não foi possível realizar a operação solicitada.");
			System.out.println(e.toString());
		}
	}

}