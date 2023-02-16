package br.gov.economia.apipgdsusep.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.entity.Perfil;
import br.gov.economia.apipgdsusep.enums.ESituacaoRegistro;
import br.gov.economia.apipgdsusep.service.PerfilService;

@SuppressWarnings("rawtypes")
@Component
public class PerfilDataModelConverter implements Converter {

	@Autowired
	private PerfilService perfilService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object objeto = null;
		if (component instanceof PickList) {
			Object dualList = ((PickList) component).getValue();
			DualListModel dl = (DualListModel) dualList;
			for (Object perfilDisponivel : dl.getSource()) {
				Perfil perfil = (Perfil) perfilDisponivel;
				if (value.equals(perfil.getId().toString()) && 
						perfil.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)) {
					objeto = perfilDisponivel;
					break;
				}
			}
			if (objeto == null) {
				for (Object perfilDoUsuario : dl.getTarget()) {
					Perfil perfil = (Perfil) perfilDoUsuario;
					if (value.equals(perfil.getId().toString()) && 
							perfil.getSituacaoRegistro().equals(ESituacaoRegistro.ATIVO)) {
						objeto = perfilDoUsuario;
						break;
					}
				}
			}
		}
		return objeto;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Perfil) {
			return ((Perfil) value).getId().toString();
		}
		return "";
	}
	
	public Perfil getPerfilPesquisado(Perfil perfil) {
		return perfilService.pesquisarPorId(perfil.getId());
	}

}