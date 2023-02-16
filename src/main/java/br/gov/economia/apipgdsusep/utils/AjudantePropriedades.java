package br.gov.economia.apipgdsusep.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class AjudantePropriedades {
	
	public static final String DOCKER = "-docker";
	public static final String HOMOL  = "-homol";
	public static final String LOCAL  = "-local";
	public static final String PROD   = "-prod";

	private static Properties properties;
	private static String context;
	
	static {
		properties = new Properties();
		File fileProp = null;
		if(getContext() != null && getContext().equals(DOCKER)) {
			fileProp = new File("/opt/" + "application" + getContext() + ".properties");
		} else {
			URL url = new AjudantePropriedades().getClass().getClassLoader().getResource("application" + getContext() + ".properties");
			fileProp = new File(url.getPath());
		}
		try {
			properties.load(new FileInputStream(fileProp));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String ctx, String key) {
		setContext(ctx);
		return properties.getProperty(key);
	}
	
	public static String getContext() {
		return (context == null || context.equals("") ? LOCAL : context);
	}
	
	public static String setContext(String ctx) {
		return context = ctx;
	}

}