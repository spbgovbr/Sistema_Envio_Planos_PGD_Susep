package br.gov.economia.apipgdsusep.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.entity.SituacaoPacto;
import br.gov.economia.apipgdsusep.service.SituacaoPactoService;

@SuppressWarnings("rawtypes")
@Component
public class SituacaoPactoConverter implements Converter {

	@Autowired
	private SituacaoPactoService situacaoPactoService;
	
	private List<SituacaoPacto> listaSituacoesPacto;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<SituacaoPacto> listaSituacoesPacto = this.getListaSituacoesPacto();
		if(value != null && StringUtils.isNotBlank(value)) {
			if(listaSituacoesPacto != null && listaSituacoesPacto.size() > 0) {
				try {
					SituacaoPacto situacaoPacto = listaSituacoesPacto.stream()
							.filter(u -> u.getId().equals(Integer.valueOf(value)))
							.findAny()
							.orElse(null);
					return situacaoPacto;
				} catch (Exception e) {
					throw new ConverterException(e);
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SituacaoPacto situacaoPacto = (SituacaoPacto) value;
		if(situacaoPacto != null && situacaoPacto.getId() != null) {
			return situacaoPacto.getId().toString();
		}
		return "";
	}
	
	public List<SituacaoPacto> getListaSituacoesPacto() {
		if(listaSituacoesPacto == null) {
			listaSituacoesPacto = situacaoPactoService.pesquisarTodos();
		}
		return listaSituacoesPacto;
	}

}