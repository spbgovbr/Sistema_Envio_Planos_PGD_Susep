package br.gov.economia.apipgdsusep.enums;

import java.util.HashMap;
import java.util.Map;

public enum EMinutos {

	MTODOS("A todo minuto", "*"), M0("00", "0"), M1("01", "1"), M2("02", "2"), M3("03", "3"), M4("04", "4"), M5("05", "5"), M6("06", "6"), M7("07", "7"), M8("08", "8"), M9("09", "9"),
	M10("10", "10"), M11("11", "11"), M12("12", "12"), M13("13", "13"), M14("14", "14"), M15("15", "15"), M16("16", "16"), M17("17", "17"), M18("18", "18"), M19("19", "19"),
	M20("20", "20"), M21("21", "21"), M22("22", "22"), M23("23", "23"), M24("24", "24"), M25("25", "25"), M26("26", "26"), M27("27", "27"), M28("28", "28"), M29("29", "29"),
	M30("30", "30"), M31("31", "31"), M32("32", "32"), M33("33", "33"), M34("34", "34"), M35("35", "35"), M36("36", "36"), M37("37", "37"), M38("38", "38"), M39("39", "39"),
	M40("40", "40"), M41("41", "41"), M42("42", "42"), M43("43", "43"), M44("44", "44"), M45("45", "45"), M46("46", "46"), M47("47", "47"), M48("48", "48"), M49("49", "49"),
	M50("50", "50"), M51("51", "51"), M52("52", "52"), M53("53", "53"), M54("54", "54"), M55("55", "55"), M56("56", "56"), M57("57", "57"), M58("58", "58"), M59("59", "59");

	private static final Map<String, EMinutos> minutosToEnum = new HashMap<String, EMinutos>();
	
	private String descricao;
	private String valor;

	private EMinutos(String descricao, String valor) {
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
		for (EMinutos minuto : values())
			minutosToEnum.put(minuto.getValor(), minuto);
	}
	
	public static EMinutos fromMinuto(String minuto) {
		return minutosToEnum.get(minuto);
	}
	
}