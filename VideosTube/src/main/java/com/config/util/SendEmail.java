package com.config.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import com.config.model.User;

public class SendEmail extends Thread {

		private List<User> recipients = new ArrayList<>();
		private static String USER_NAME = "videosTubeProject";  // GMail user name (just the part before "@gmail.com")
	    private static String PASSWORD = "asd123dsa"; // GMail password
	    private String videoName;
	    private String channelName;
	    
	    public SendEmail(List<User> users, String videoName, String channelName){
	    	this.recipients= users;
	    	this.videoName=videoName;
	    	this.channelName=channelName;
	    }
	    
	    @Override
	    public void run() {
		        List<User> to = recipients ; // list of recipient email addresses
		        String subject = "Hello! New video was added in channel "+channelName;
		        String body = "Hello ! New video "+ videoName + " was added in channel "+channelName;

		        sendFromGMail( to, subject, body);
	    }

	    private  void sendFromGMail(List<User> to, String subject, String body) {
	        Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", USER_NAME);
	        props.put("mail.smtp.password", PASSWORD);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

//	        Session session = Session.getDefaultInstance(props);
//	        MimeMessage message = new MimeMessage(session);
//
//	        try {
//	            message.setFrom(new InternetAddress(USER_NAME));
//	            InternetAddress[] toAddress = new InternetAddress[to.size()];
//
//	            // To get the array of addresses
//	            for( int i = 0; i < to.size(); i++ ) {
//	                toAddress[i] = new InternetAddress(to.get(i).getEmail());
//	            }
//
//	            for( int i = 0; i < toAddress.length; i++) {
//	                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
//	            }
//
//	            message.setSubject(subject);
//	            message.setText(body);
//	            Transport transport = session.getTransport("smtp");
//	            transport.connect(host, USER_NAME, PASSWORD);
//	            transport.sendMessage(message, message.getAllRecipients());
//	            transport.close();
	        }
//	        catch (AddressException ae) {
//	            ae.printStackTrace();
//	            System.out.println(ae.getMessage());
//	        }
//	        catch (MessagingException me) {
//	            me.printStackTrace();
//	            System.out.println(me.getMessage());
//	        }
	    }

