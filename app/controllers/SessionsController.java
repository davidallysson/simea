package controllers;

/*
import helpers.SegurancaHelpers.Autenticar;
import helpers.SegurancaHelpers.InformacoesUsuarioHelper;
*/
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import akka.util.Crypt;
import models.Usuario;
import forms.LoginForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

//------ Não incluir esse import abaixo ------------

///import play.api.data.Form; /// 

//------------------------------------
import static play.data.Form.*;

import play.Logger;
import play.data.*;
import play.data.FormFactory;

/**
 * Controler responsável pelo controle da autenticação e sessão dos usuários.
 * 
 * @author alessandroanjos
 *
 */
@Singleton
public class SessionsController extends Controller{

	@Inject
	private FormFactory formFactory;	
	 
	/**
	 * Metodo para abrir a tela de Login.
	 *
	 * @return
	 */
	public Result login() {
		return ok(views.html.Sessions.login.render(formFactory.form(LoginForm.class)));
	}
	
	/**
	 * Metodo para efetuar o login.
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public Result efetuarLogin() throws NoSuchAlgorithmException {
		Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest();
		String email = loginForm.field("email").value();

		System.out.println("LOGIN:"+email);
		if (loginForm.hasErrors()) {
			flash("error","Login ou Senha Inválida(s). Tente novamente!");
			Logger.info("Login ou Senha Inválida(s)");
			return badRequest(views.html.Sessions.login.render(loginForm));
		} else {
			Usuario usuario = Usuario.find.where().ilike("email", email).findUnique();

			if(usuario != null){
				session().put("usuarioLogadoID", usuario.id.toString());
				Logger.info("Logou");
				flash("success","Bem-vindo!");
				if(usuario.chaveRedefinicaoSenha != null ){
					Logger.info("chave refefinicao de senha");
					return null;
					//return ok(views.html.Sessions.formularioAlteracaoSenha.render(formFactory.form(AlterarSenhaForm.class)));
				} else {
					if (!usuario.isAdministrador) {
						Logger.info("Logou como usuario - não eh Admin");
						return redirect(routes.AlunoController.statusAlunoForm());
					//TODO: criar uma page para aluno	return redirect(routes.HomeController.index());
					//	return redirect(controllers.routes.Usuarios.index());
					} else if(usuario.isAdministrador){
						Logger.info("Admin logado");
						return redirect(routes.HomeController.homeIndex());
					//	return redirect(controllers.routes.Administracao.index());
					} else{			
						Logger.info("Logado como outro");
						return redirect(routes.HomeController.index());
					}
				}
			} else {
				flash("error","E-mail ou senha incorretos!");
				return redirect(routes.HomeController.index());
			}
		}
	}
	
	/**
	 * Metodo para efetuar o logout.
	 *
	 * @return
	 */
	public Result efetuarlogout() {
		session().clear();
        flash("success", "Logout realizado com sucesso!");
        return redirect(routes.SessionsController.login());
	}
	
	/**
	 * Ativa a conta do usuario
	 * @param email
	 * @return
	 */
	public Result ativarConta(String email) {
		Usuario usuario = Usuario.find.where().eq("email", email).findUnique();
//		if(usuario != null){
//			if(usuario.ativo == true){
//				flash().put("info", String.format(usuario.nome +", sua conta já foi ativada!"));
//			} else {
//				usuario.ativo = true;
//				usuario.update();
//				flash().put("success", String.format(usuario.nome +", sua conta foi ativada com sucesso! <br /><br />Agora você poderá aproveitar de tudo que a gente preparou para te ajudar!"));
//				if(InformacoesUsuarioHelper.isLogado()){
//					return ok(views.html.Usuario.index.render(InformacoesUsuarioHelper.getRankingColocacao()));
//				}else {
//					return redirect(routes.Sessions.login());
//				}
//			}
//		} else {
//			flash().put("error","Chave para ativação de conta não encontrada ou inválida!");
//			return redirect(routes.Sessions.login());
//		}
//		return redirect(routes.Sessions.login());
		return TODO;
	}
}
