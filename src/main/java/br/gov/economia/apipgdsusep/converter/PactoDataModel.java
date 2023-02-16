package br.gov.economia.apipgdsusep.converter;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.gov.economia.apipgdsusep.entity.Pacto;

@SuppressWarnings("unchecked")
public class PactoDataModel extends ListDataModel<Pacto> implements SelectableDataModel<Pacto>, Serializable {  

	private static final long serialVersionUID = 1L;

	public PactoDataModel() { }

    public PactoDataModel(List<Pacto> data) {
        super(data);
    }
    
	@Override
    public Pacto getRowData(String rowKey) {
        List<Pacto> pactos = (List<Pacto>) getWrappedData();
        for(Pacto pacto : pactos) {
            if(pacto.getIdPacto().equals(Integer.valueOf(rowKey)))
                return pacto;
        }
        return null;
    }

    @Override
    public Object getRowKey(Pacto pacto) {
        return pacto.getIdPacto().toString();
    }
}