package br.gov.economia.apipgdsusep.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.entity.Funcao;
import br.gov.economia.apipgdsusep.service.FuncaoService;

@SuppressWarnings("rawtypes")
@Component
public class FuncaoConverter implements Converter {

	@Inject
	private FuncaoService funcaoService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value != null && StringUtils.isNotBlank(value) && !value.equals("Selecione")) {
			try {
				Funcao funcao = funcaoService.pesquisarPorId(new Long(value));
				return funcao;
			} catch (Exception e) {
				throw new ConverterException(e);
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Funcao funcao = (Funcao) value;
		if(funcao != null) {
			return funcao.getId().toString();
		}
		return "";
	}
	
}