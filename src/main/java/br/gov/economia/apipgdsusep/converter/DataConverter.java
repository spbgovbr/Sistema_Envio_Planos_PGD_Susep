package br.gov.economia.apipgdsusep.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
public class DataConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		if (value != null && StringUtils.isNotBlank(value) && !value.contains("_")) {
			try {
				return format.parse(value);
			} catch (ParseException e) {
				throw new ConverterException(e);
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date valorConvertido = (Date) value;
		if (valorConvertido != null) {
			return dateFormat.format(valorConvertido.getTime());
		}
		return "";
	}
}