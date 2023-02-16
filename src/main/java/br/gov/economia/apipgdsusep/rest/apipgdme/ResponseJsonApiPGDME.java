package br.gov.economia.apipgdsusep.rest.apipgdme;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ResponseJsonApiPGDME {
	
	private static final Gson GSON = new GsonBuilder().create();

	private List<ResponseDetailJsonApiPGDME> detail;

	public ResponseJsonApiPGDME() { }
	
	public ResponseJsonApiPGDME(DetailJsonApiPGDME detailJsonApiPGDME) { 
		detail = new ArrayList<ResponseDetailJsonApiPGDME>();
		detail.add(new ResponseDetailJsonApiPGDME(detailJsonApiPGDME.getDetail(), "error"));
	}


	public static boolean isJsonArray(String jsonString) {
		if(jsonString.contains("[")) {
			return true;
		}
		return false;
	}
	
	public static ResponseJsonApiPGDME gerarResponseJsonApiPGDME(String jsonString) {
		DetailJsonApiPGDME detailJsonApiPGDME = null;
		ResponseJsonApiPGDME responseJsonApiPGDME = null;
		if(ResponseJsonApiPGDME.isJsonArray(jsonString)) {
			responseJsonApiPGDME = GSON.fromJson(jsonString, ResponseJsonApiPGDME.class);
		} else {
			try {
				detailJsonApiPGDME = GSON.fromJson(jsonString, DetailJsonApiPGDME.class);
				responseJsonApiPGDME = new ResponseJsonApiPGDME(detailJsonApiPGDME);
			} catch (Exception e) {
				String detail = "{\"detail\":"+jsonString+"}";
				detailJsonApiPGDME = GSON.fromJson(detail, DetailJsonApiPGDME.class);
				responseJsonApiPGDME = new ResponseJsonApiPGDME(detailJsonApiPGDME);
			}
		}
		return responseJsonApiPGDME;
	}
	
	@Override
	public String toString() {
		StringBuilder jsonSBError = new StringBuilder();
		if(detail != null) {
			for(ResponseDetailJsonApiPGDME det : detail) {
				jsonSBError.append("Tipo: " + det.getType() + "\n");
				jsonSBError.append("Mensagem: " + det.getMsg());
				if(det.getLoc() != null) {
					for(String loc : det.getLoc()) {
						jsonSBError.append("Detalhes: " + loc + "\n");
					}
				}
			}
		}
		return "ResponseJsonApiPGDME [detail=" + jsonSBError.toString() + "]";
	}

	public List<ResponseDetailJsonApiPGDME> getDetail() {
		return detail;
	}
	public void setDetail(List<ResponseDetailJsonApiPGDME> detail) {
		this.detail = detail;
	}

}