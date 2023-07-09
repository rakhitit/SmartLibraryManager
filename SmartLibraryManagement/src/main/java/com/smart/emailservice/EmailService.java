package com.smart.emailservice;

import java.io.File;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;



@Service
public class EmailService {
	
	@Value("${user.value}")
	private String from;

	@Value("${subject.value}")
	private String subject;

	@Value("${message.value}")
	private String text;
	
	@Value("${password.value}")
	private String password;

public boolean sendEmail(int otp, String to) {
		
		boolean flag=false;
		
		Properties properties=new Properties();
		System.out.println("Reading PROPERTIES ");
		
		//setting important information to properties object
		properties.put("mail.smtp.auth",true);
		
		properties.put("mail.smtp.starttls.enable",true);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host","smtp.gmail.com");
		
		Session session=Session.getInstance(properties, new Authenticator(){

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(from,password);
			}
		});
		
		try {
		text=""
			+"<div style='border:1px solid #e2e2e2;padding:20px'>"
			+"<h1>"
			+"OTP is "
			+"<b>"+otp
			+"</n>"
			+"</h1>"
			+"<div>";
			
			
			
			Message message=new MimeMessage(session);
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setContent(text, "text/html");
			//message.setText(text+": "+otp);
			
			Transport.send(message);
			flag=true;
		}
		
		catch(Exception e) {}
		return flag;
	}
	
	public boolean sendEmailWithAttachment(String to, String from,String subject,String text,File file) {
		
		boolean flag=false;
		
		Properties properties=new Properties();
		System.out.println("Reading PROPERTIES ");
		
		//setting important information to properties object
		properties.put("mail.smtp.auth",true);
		properties.put("mail.smtp.starttls.enable",true);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host","smtp.gmail.com");
		
		final String username="rakhi782970";
		final String password="eorudlebyfwgvyto";
		
		Session session=Session.getInstance(properties, new Authenticator(){

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(username,password);
			}
		}); 
		try {
		Message message=new MimeMessage(session);
		message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
		message.setFrom(new InternetAddress(from));
		message.setSubject(subject);
		
		MimeBodyPart part1=new MimeBodyPart();
		part1.setText(text);
		
		MimeBodyPart part2=new MimeBodyPart();
		part2.attachFile(file);
		
		
		MimeMultipart mimeMultipart=new MimeMultipart();
		mimeMultipart.addBodyPart(part1);
		mimeMultipart.addBodyPart(part2);
		
		message.setContent(mimeMultipart);
		
		Transport.send(message);
		
		flag=true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return flag;	
	}
}
