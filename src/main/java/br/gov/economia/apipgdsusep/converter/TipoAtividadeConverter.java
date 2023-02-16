package br.gov.economia.apipgdsusep.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.entity.TipoAtividade;
import br.gov.economia.apipgdsusep.service.TipoAtividadeService;

@SuppressWarnings("rawtypes")
@Component
public class TipoAtividadeConverter implements Converter {

	@Autowired
	private TipoAtividadeService tipoAtividadeService;
	
	private List<TipoAtividade> listaTiposAtividades;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<TipoAtividade> listaTiposAtividades = this.getListaTiposAtividades();
		if(value != null && StringUtils.isNotBlank(value)) {
			if(listaTiposAtividades != null && listaTiposAtividades.size() > 0) {
				try {
					TipoAtividade tipoAtividade = listaTiposAtividades.stream()
							.filter(u -> u.getId().equals(Integer.valueOf(value)))
							.findAny()
							.orElse(null);
					return tipoAtividade;
				} catch (Exception e) {
					throw new ConverterException(e);
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		TipoAtividade tipoAtividade = (TipoAtividade) value;
		if(tipoAtividade != null && tipoAtividade.getId() != null) {
			return tipoAtividade.getId().toString();
		}
		return "";
	}
	
	public List<TipoAtividade> getListaTiposAtividades() {
		if(listaTiposAtividades == null) {
			listaTiposAtividades = tipoAtividadeService.pesquisarTodos();
		}
		return listaTiposAtividades;
	}

}