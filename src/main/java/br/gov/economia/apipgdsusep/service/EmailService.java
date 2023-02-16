package br.gov.economia.apipgdsusep.service;

import java.nio.charset.StandardCharsets;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import br.gov.economia.apipgdsusep.utils.Mail;

@Service
public class EmailService {

	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(Mail mail) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.addRecipients(Message.RecipientType.TO, 
            		InternetAddress.parse(mail.getRecipientsTO()));
            if(mail.getRecipientsCC() != null && StringUtils.isNotBlank(mail.getRecipientsCC())) {
            	message.addRecipients(Message.RecipientType.CC, 
                		InternetAddress.parse(mail.getRecipientsCC()));
    		}
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(mail.getModelo());
            String html = templateEngine.process(mail.getTemplate(), context);

            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());
            
            emailSender.send(message);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}