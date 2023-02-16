package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EDias {

	MTODOS("Todo dia", "*"), D1("01", "1"), D2("02", "2"), D3("03", "3"), D4("04", "4"), D5("05", "5"), D6("06", "6"), D7("07", "7"), D8("08", "8"), D9("09", "9"), D10("10", "10"), 
	D11("11", "11"), D12("12", "12"), D13("13", "13"), D14("14", "14"), D15("15", "15"), D16("16", "16"), D17("17", "17"), D18("18", "18"), D19("19", "19"),
	D20("20", "20"), D21("21", "21"), D22("22", "22"), D23("23", "23"), D24("24", "24"), D25("25", "25"), D26("26", "26"), D27("27", "27"), D28("28", "28"), 
	D29("29", "29"), D30("30", "30"), D31("31", "31");
	
	private static final Map<String, EDias> diasToEnum = new HashMap<String, EDias>();

	private String descricao;
	private String valor;

	private EDias(String descricao, String valor) {
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
		for (EDias dia : values())
			diasToEnum.put(dia.getValor(), dia);
	}
	
	public static EDias fromDia(String dia) {
		return diasToEnum.get(dia);
	}
	
}