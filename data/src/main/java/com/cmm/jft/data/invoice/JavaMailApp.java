package com.cmm.jft.data.invoice;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.URLName;

public class JavaMailApp {
	public static void main(String[] args) {
		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "imap.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"cristranus@gmail.com", "JfD!347>61&<52");
					}
				});

		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {
			Folder folder = session.getFolder(new URLName("inbox"));
			System.out.println("Messages: " + folder.getMessageCount());

			// Message message = new MimeMessage(session);
			// message.setFrom(new InternetAddress("seuemail@gmail.com"));
			// //Remetente
			//
			// Address[] toUser = InternetAddress //Destinatário(s)
			// .parse("seuamigo@gmail.com, seucolega@hotmail.com, seuparente@yahoo.com.br");
			//
			// message.setRecipients(Message.RecipientType.TO, toUser);
			// message.setSubject("Enviando email com JavaMail");//Assunto
			// message.setText("Enviei este email utilizando JavaMail com minha conta GMail!");
			// /**Método para enviar a mensagem criada*/
			// Transport.send(message);
			//
			// System.out.println("Feito!!!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
