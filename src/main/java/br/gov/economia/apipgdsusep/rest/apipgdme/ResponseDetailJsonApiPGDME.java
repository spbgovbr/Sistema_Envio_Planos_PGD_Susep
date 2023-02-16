package br.gov.economia.apipgdsusep.rest.apipgdme;

import java.util.List;

public class ResponseDetailJsonApiPGDME {
	
	private List<String> loc;
	private String msg;
	private String type;
	
	
	public ResponseDetailJsonApiPGDME() { }
	
	public ResponseDetailJsonApiPGDME(String msg, String type) {
		super();
		this.msg = msg;
		this.type = type;
	}


	public List<String> getLoc() {
		return loc;
	}
	public void setLoc(List<String> loc) {
		this.loc = loc;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}