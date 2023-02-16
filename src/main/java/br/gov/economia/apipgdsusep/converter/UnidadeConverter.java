package br.gov.economia.apipgdsusep.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.entity.Unidade;
import br.gov.economia.apipgdsusep.service.UnidadeService;

@SuppressWarnings("rawtypes")
@Component
public class UnidadeConverter implements Converter {

	@Autowired
	private UnidadeService unidadeService;
	
	private List<Unidade> listaUnidades;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<Unidade> listaUnidades = this.getListaUnidades();
		if(value != null && StringUtils.isNotBlank(value)) {
			if(listaUnidades != null && listaUnidades.size() > 0) {
				try {
					Unidade unidade = listaUnidades.stream()
							.filter(u -> u.getId().equals(Integer.valueOf(value)))
							.findAny()
							.orElse(null);
					return unidade;
				} catch (Exception e) {
					throw new ConverterException(e);
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Unidade unidade = (Unidade) value;
		if(unidade != null && unidade.getId() != null) {
			return unidade.getId().toString();
		}
		return "";
	}
	
	public List<Unidade> getListaUnidades() {
		if(listaUnidades == null) {
			listaUnidades = unidadeService.pesquisarTodos();
		}
		return listaUnidades;
	}

}