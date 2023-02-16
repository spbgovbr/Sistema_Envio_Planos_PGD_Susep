package br.gov.economia.apipgdsusep.security;

import java.security.MessageDigest;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Criptografia {
	
	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String getCriptoHash(String palavra) throws Exception {
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest(palavra.getBytes("UTF-8"));
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}
		return hexString.toString();
	}
	
	public static String getNumerosAleatorios(int qtdDigitos) {
		StringBuilder senha = new StringBuilder("");
		Random rand = new Random();
		for (int i = 0; i < qtdDigitos; i++) {
			senha.append(rand.nextInt(10));
		}
		return senha.toString() ;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(Criptografia.getNumerosAleatorios(6));
		System.out.println(Criptografia.getCriptoHash("123456"));
		System.out.println(passwordEncoder.encode("123456"));
	}
	
}