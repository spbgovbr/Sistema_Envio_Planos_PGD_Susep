package br.gov.economia.apipgdsusep.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.entity.Atividade;
import br.gov.economia.apipgdsusep.service.AtividadeService;

@SuppressWarnings("rawtypes")
@Component
public class AtividadeConverter implements Converter {

	@Autowired
	private AtividadeService atividadeService;
	
	private List<Atividade> listaAtividades;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<Atividade> listaAtividades = this.getListaAtividades();
		if(value != null && StringUtils.isNotBlank(value)) {
			if(listaAtividades != null && listaAtividades.size() > 0) {
				try {
					Atividade atividade = listaAtividades.stream()
							.filter(u -> u.getId().equals(Integer.valueOf(value)))
							.findAny()
							.orElse(null);
					return atividade;
				} catch (Exception e) {
					throw new ConverterException(e);
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Atividade atividade = (Atividade) value;
		if(atividade != null && atividade.getId() != null) {
			return atividade.getId().toString();
		}
		return "";
	}
	
	public List<Atividade> getListaAtividades() {
		if(listaAtividades == null) {
			listaAtividades = atividadeService.pesquisarTodos();
		}
		return listaAtividades;
	}

}