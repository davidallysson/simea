package emails;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import play.Configuration;
import play.libs.Json;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Usuario;

/**
 * Classe responsável pelo envio de emails de notificação de registro, recuperação de senha e alteração de senha.
 * @author alessandroanjos
 * 
 * Ver: https://myaccount.google.com/lesssecureapps?rfn=27&rfnc=1&eid=4925844521394319173&et=0&asae=2&pl
 * Para permissões de aplicativos nao seguros
 */
public class RegistroMailer {

	private static final String smtpHost = "smtp.gmail.com";
    private static Integer smtpPort = 587;
    private static String smtpUser = "simea.ifrn@gmail.com";
    private static String smtpPassword = "5ime@IFRN";

	// Load SMTP configuration
	//Model to Play 2.4 and anteriores
	/*static String smtpHost = Play.application().configuration().getString( "smtp.host" );
    static Integer smtpPort = Play.application().configuration().getInt( "smtp.port" );
    static String smtpUser = Play.application().configuration().getString( "smtp.user" );
    static String smtpPassword = Play.application().configuration().getString( "smtp.password");*/

    public static Collection<InternetAddress> listaDeEmailsEmCC(){
    	Collection<InternetAddress> emails = new ArrayList<InternetAddress>();
    	InternetAddress intAdd = null;
		try {
			intAdd = new InternetAddress("sandro.adsc@hotmail.com");
		} catch (AddressException e1) {
			e1.printStackTrace();
		}
		emails.add(intAdd);
       return emails;
    }

	public static void enviarMensagemRegistro(Usuario usuario) {
			ObjectNode result = Json.newObject();
			String mensagemHTML = views.html.Emails.RegistroMailer.mensagemRegistro.render(usuario).toString();
	        HtmlEmail email = new HtmlEmail();
			try {
				email.setCharset("UTF-8");
				email.setHostName(smtpHost);
				email.setSmtpPort(smtpPort);
				email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPassword));
				email.setSSLOnConnect(true);
				email.setBcc(listaDeEmailsEmCC());
				email.setFrom("[SIMEA] <nao-reponda@ifrn.com.br>");
				email.setSubject("SIMEA - Bem-vindo!");
				email.addTo(usuario.email);
				email.setHtmlMsg(mensagemHTML);
				email.send();
				result.put("status", true);
				result.put("message", " Registro enviado com sucesso ");
				System.out.println("Email de registro enviado...");
			} catch (EmailException e) {
				result.put("status", false);
				result.put("message", " Falha ao enviar e-mail. ");
				e.printStackTrace();
			}
		}
	
//	public void enviarMensagemAtivarConta(Usuario usuario) {
//		ObjectNode result = Json.newObject();
//		String mensagemHTML = views.html.Emails.RegistroMailer.mensagemAtivarConta.render(usuario).toString();
//        HtmlEmail email = new HtmlEmail();
//		try {
//			email.setCharset("UTF-8");
//			email.setHostName(smtpHost);
//			email.setSmtpPort(smtpPort);
//			email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPassword));
//			email.setSSLOnConnect(true);
//			email.setBcc(listaDeEmailsEmCC());
//			email.setFrom("[SIMEA] <nao-reponda@ifrn.com.br>");
//			email.setSubject("SIMEA - Ative a sua conta!");
//			email.addTo(usuario.email);
//			email.setHtmlMsg(mensagemHTML);
//			email.send();
//			result.put("status", true);
//			result.put("message", " Mensagem enviada com sucesso ");
//			System.out.println("Email de ativacao de conta enviado...");
//		} catch (EmailException e) {
//			result.put("status", false);
//			result.put("message", " Falha ao enviar e-mail. ");
//			e.printStackTrace();
//		}
//	}
	
//	public void enviarSolicitacaoRedefinicaoSenha(Usuario usuario) {
//		ObjectNode result = Json.newObject();
//		// Pega o conteúdo da view e joga numa string, utilizando assim os conceitos do MVC.
//		String mensagemHTML = views.html.Emails.RegistroMailer.solicitacaoRedefinicaoSenha.render(usuario).toString();
//        HtmlEmail email = new HtmlEmail();
//		try {
//			email.setCharset("UTF-8");
//			email.setHostName(smtpHost);
//			email.setSmtpPort(smtpPort);
//			email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPassword));
//			email.setSSLOnConnect(true);
//			email.setBcc(listaDeEmailsEmCC());
//			email.setFrom("[Primo Nerd] <nao-reponda@primonerd.com.br>");
//			email.setSubject("Primo Nerd - Solicitação de Redefinição de Senha!");
//			email.addTo(usuario.email);
//			email.setHtmlMsg(mensagemHTML);
//			email.send();
//			result.put("status", true);
//			result.put("message", " Solicitacao de senha realizad com sucesso ");
//			System.out.println("Email de enviarSolicitacaoRedefinicaoSenha enviado...");
//		} catch (EmailException e) {
//			result.put("status", false);
//			result.put("message", " Falha ao enviar e-mail. ");
//			e.printStackTrace();
//		}
//	}
//	
//	public void enviarSenhaRedefinida(Usuario usuario, String novaSenha) {
//		ObjectNode result = Json.newObject();
//		// Pega o conteúdo da view e joga numa string, utilizando assim os conceitos do MVC.
//		String mensagemHTML = views.html.Emails.RegistroMailer.redefinirSenha.render(usuario, novaSenha).toString();
//        HtmlEmail email = new HtmlEmail();
//		try {
//			email.setCharset("UTF-8");
//			email.setHostName(smtpHost);
//			email.setSmtpPort(smtpPort);
//			email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPassword));
//			email.setSSLOnConnect(true);
//			email.setBcc(listaDeEmailsEmCC());
//			email.setFrom("[Primo Nerd] <nao-reponda@primonerd.com.br>");
//			email.setSubject("Primo Nerd - Senha Redefinida!");
//			email.addTo(usuario.email);
//			email.setHtmlMsg(mensagemHTML);
//			email.send();
//			result.put("status", true);
//			result.put("message", "Senha redefinida com sucesso ");
//			System.out.println("Email de enviarSenhaRedefinida enviado...");
//		} catch (EmailException e) {
//			result.put("status", false);
//			result.put("message", " Falha ao enviar e-mail. ");
//			e.printStackTrace();
//		}
//	}
//	
//	public void enviarMensagemExclusao(Usuario usuario) {
//		ObjectNode result = Json.newObject();
//		// Pega o conteúdo da view e joga numa string, utilizando assim os conceitos do MVC.
//		String mensagemHTML = views.html.Emails.RegistroMailer.mensagemExclusao.render(usuario).toString();
//        HtmlEmail email = new HtmlEmail();
//		try {
//			email.setCharset("UTF-8");
//			email.setHostName(smtpHost);
//			email.setSmtpPort(smtpPort);
//			email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPassword));
//			email.setSSLOnConnect(true);
//			email.setBcc(listaDeEmailsEmCC());
//			email.setFrom("[Primo Nerd] <nao-reponda@primonerd.com.br>");
//			email.setSubject("Primo Nerd - Conta Desativada!");
//			email.addTo(usuario.email);
//			email.setHtmlMsg(mensagemHTML);
//			email.send();
//			result.put("status", true);
//			result.put("message", " Exclusao da conta realizada com sucesso ");
//			System.out.println("Email de enviarMensagemExclusao enviado...");
//		} catch (EmailException e) {
//			result.put("status", false);
//			result.put("message", " Falha ao enviar e-mail. ");
//			e.printStackTrace();
//		}
//	}
}
