package br.gov.economia.apipgdsusep.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.economia.apipgdsusep.service.EmailService;

@Component("threadEnviarEmail")
public class ThreadEnviarEmail implements Runnable {
	
	@Autowired 
	private EmailService emailService;
	
	private Mail mail;

	@Override
	public void run() {
		try {
			emailService.sendEmail(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Mail getMail() {
		return mail;
	}
	public void setMail(Mail mail) {
		this.mail = mail;
	}
	
}