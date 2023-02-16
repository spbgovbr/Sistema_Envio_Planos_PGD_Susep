package br.gov.economia.apipgdsusep.rest.apipgdme;

public interface ApiPGDMEService {
	
	String criarOuAtualizarPlanoTrabalho(AccessTokenApiPGDME accessToken, String idPlanoTrabalho, String planoTrabalhoJson);
	
	String atualizarPlanoTrabalho(AccessTokenApiPGDME accessToken, String idPlanoTrabalho, String planoTrabalhoJson);
	
}