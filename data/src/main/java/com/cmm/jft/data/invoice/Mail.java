/**
 * 
 */
package com.cmm.jft.data.invoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

/**
 * <p>
 * <code>Mail.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 15/04/2014 00:28:07
 *
 */
public class Mail {

	private String user;
	private String password;
	private String host;
	private Session session;

	public Mail(String host) {
		this.host = host;
	}

	public void connect(String user, String password) {
		this.user = user;
		this.password = password;
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "imap.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		session = Session.getInstance(properties);
	}

	public Message[] listMails(String folder, final String searchTerm) {

		Message[] ret = null;
		try {
			Store store = session.getStore("imaps");
			store.connect(host, user, password);
			Folder f = store.getFolder(folder);
			f.open(Folder.READ_WRITE);

			if (searchTerm.length() > 0) {
				SearchTerm st = new SearchTerm() {
					@Override
					public boolean match(Message msg) {
						boolean ret = false;
						try {
							ret = msg.getSubject().contains(searchTerm);
						} catch (MessagingException e) {
							ret = false;
							e.printStackTrace();
						}
						return ret;
					}
				};

				ret = f.search(st);
			} else {
				ret = f.getMessages();
			}

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String args[]) throws MessagingException,
			IOException {
		Mail m = new Mail("imap.gmail.com");
		m.connect("cristranus", "pass");
		Message[] msgs = m.listMails("INBOX", "");
		System.out.println("Found: " + msgs.length);
		for (int i = msgs.length - 1; i >= 0; i--) {
			Message msg = msgs[i];

			if (msg.getSubject() != null
					&& msg.getSubject().contains(
							"Relat�rio di�rio de Day Trade")) {
				String path = "C:/Disco/Workspaces/JFT/jft_data/file/relatoriosDT/"
						+ msg.getFrom()[0].toString().replace(".", "-")
								.replace("@", "").trim()
						+ "_"
						+ msg.getReceivedDate().toString().replace(" ", "_")
								.replace(":", "-") + ".html";

				String content = "";
				Multipart mp = (Multipart) msg.getContent();
				for (int p = 0; p < mp.getCount(); p++) {
					BodyPart bp = mp.getBodyPart(p);
					content += bp.getContent();
				}

				File f = new File(path);
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(content.getBytes());
			}
		}

	}

}
