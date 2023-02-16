package br.gov.economia.apipgdsusep.rest.apipgdme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiPGDMEServiceImpl implements ApiPGDMEService {
	
	//private static final String GET_REQUEST_METHOD = "GET";
	private static final String PATCH_REQUEST_METHOD = "PATCH";
	private static final String POST_REQUEST_METHOD = "POST";
	private static final String PUT_REQUEST_METHOD = "PUT";
	
	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	
	@Value("${spring.apipgdme.url}")
    private String urlApiPGDME;
	
	@Value("${spring.apipgdme.service.access_token}")
    private String uriServiceAccessToken;
	
	@Value("${spring.apipgdme.service.create_or_update_plano_trabalho}")
    private String uriServiceCreateOrUpdatePlanoTrabalho;
	
	@Value("${spring.apipgdme.service.update_plano_trabalho}")
    private String uriServiceUpdatePlanoTrabalho;
	
	@Value("${spring.apipgdme.usuario}")
    private String usuario;
	
	@Value("${spring.apipgdme.senha}")
    private String senha;
	
	
	public String criarOuAtualizarPlanoTrabalho(AccessTokenApiPGDME accessToken, String idPlanoTrabalho, String planoTrabalhoJson) {
		String result = null;
		try {
			result = this.getResultadoServicoRestApiPGDME(accessToken, PUT_REQUEST_METHOD, CONTENT_TYPE_APPLICATION_JSON, 
					uriServiceCreateOrUpdatePlanoTrabalho.replace("{cod_plano}", idPlanoTrabalho), planoTrabalhoJson);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	public String atualizarPlanoTrabalho(AccessTokenApiPGDME accessToken, String idPlanoTrabalho, String planoTrabalhoJson) {
		String result = null;
		try {
			result = this.getResultadoServicoRestApiPGDME(accessToken, PATCH_REQUEST_METHOD, CONTENT_TYPE_APPLICATION_JSON, 
					uriServiceUpdatePlanoTrabalho.replace("{cod_plano}", idPlanoTrabalho), planoTrabalhoJson);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	public String obterAccessTokenApiPGDME() {
		StringBuilder result = new StringBuilder();
		try {
			String credenciais = usuario + ":" + senha;
			String output = null;
			String urlParameters  = "grant_type=password&username="+usuario+"&password="+senha;
			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			URL urlServiceApiJuno = new URL(urlApiPGDME + uriServiceAccessToken);
			HttpURLConnection conn = (HttpURLConnection) urlServiceApiJuno.openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestMethod(POST_REQUEST_METHOD);
			conn.setRequestProperty("Authorization", "basic " + Base64.getEncoder().encodeToString(credenciais.getBytes()));
			conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.write(postData);
			dos.flush();
			dos.close();
			BufferedReader br = null;
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
					conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
				br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
			} else {
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			}
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				result.append(output);
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public String getResultadoServicoRestApiPGDME(AccessTokenApiPGDME accessToken, String requestMethod, 
			String contentType, String servicoRestApiJuno, String inputJson) {
		StringBuilder result = new StringBuilder();
		try {
			String output = null;
			URL urlServiceApiJuno = new URL(urlApiPGDME + servicoRestApiJuno);
			HttpURLConnection conn = (HttpURLConnection) urlServiceApiJuno.openConnection();
			/*if(requestMethod.equals(PATCH_REQUEST_METHOD)) {
				conn.setRequestProperty("X-HTTP-Method-Override", PATCH_REQUEST_METHOD);
				conn.setRequestMethod(POST_REQUEST_METHOD);
			} else {
				conn.setRequestMethod(requestMethod);
			}*/
			this.setRequestMethodViaJreBugWorkaround();
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("Authorization", accessToken.getToken_type() + " " + accessToken.getAccess_token());
			if(contentType != null) {
				conn.setRequestProperty("Content-Type", contentType);
			}
			if(requestMethod.equals(PATCH_REQUEST_METHOD) || 
					requestMethod.equals(POST_REQUEST_METHOD) || 
							requestMethod.equals(PUT_REQUEST_METHOD)) {
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.connect();
				OutputStream os = conn.getOutputStream();
				os.write(inputJson.getBytes(StandardCharsets.UTF_8));
				os.flush();
				os.close();
			}
			BufferedReader br = null;
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
					conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
				br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
			} else {
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			}
			while ((output = br.readLine()) != null) {
				String responseAPI = new String(output.getBytes(), StandardCharsets.UTF_8);
				System.out.println(responseAPI);
				result.append(responseAPI);
			}
			conn.disconnect();
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result.toString();
	}
	
	/*
	 https://stackoverflow.com/questions/22355235/patch-request-using-jersey-client/39641592#39641592
	 */
	public void setRequestMethodViaJreBugWorkaround() {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);
			// get the methods field modifiers
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			// bypass the "private" modifier 
			modifiersField.setAccessible(true);
			// remove the "final" modifier
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);
			/* valid HTTP methods */
			String[] methods = {
					"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
			};
			// set the new methods - including patch
			methodsField.set(null, methods);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

}