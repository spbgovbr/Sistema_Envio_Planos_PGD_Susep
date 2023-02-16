package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EHoras {

	MTODOS("A toda hora", "*"), H0("00", "0"), H1("01", "1"), H2("02", "2"), H3("03", "3"), H4("04", "4"), H5("05", "5"), H6("06", "6"), H7("07", "7"), 
	H8("08", "8"), H9("09", "9"), H10("10", "10"), H11("11", "11"), H12("12", "12"), H13("13", "13"), H14("14", "14"), H15("15", "15"), 
	H16("16", "16"), H17("17", "17"), H18("18", "18"), H19("19", "19"), H20("20", "20"), H21("21", "21"), H22("22", "22"), H23("23", "23");
	
	private static final Map<String, EHoras> horasToEnum = new HashMap<String, EHoras>();

	private String descricao;
	private String valor;

	private EHoras(String descricao, String valor) {
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	static {
		for (EHoras hora : values())
			horasToEnum.put(hora.getValor(), hora);
	}
	
	public static EHoras fromHora(String hora) {
		return horasToEnum.get(hora);
	}
	
}