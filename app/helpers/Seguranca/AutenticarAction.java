package helpers.Seguranca;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import models.Usuario;
import play.mvc.Action;
import play.Play;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Helper que restringe o acesso somente para usuários autenticados no sistema. <br/>
 * <br/>
 * <i>Atenção: Utilizar a Anotação <strong>@Autenticar</strong> em cada método onde o acesso seja restrito.</i>
 * 
 * @author alessandroanjos
 */
public class AutenticarAction extends Action.Simple {
	
	public CompletionStage<Result> call(Http.Context ctx) {
		if (ctx.session().get("usuarioLogadoID") == null) {
			ctx.flash().put("url", "GET".equals(ctx.request().method()) ? ctx.request().uri() : "/");
			return CompletableFuture.completedFuture(redirect(controllers.routes.SessionsController.login()));
		}
		return delegate.call(ctx);
	}
	
	/*
	@Override
	public Promise<SimpleResult> call(Context ctx) throws Throwable {
		Usuario usuario = new Usuario();
		try {
			usuario = Usuario.find.byId(Long.valueOf(ctx.session().get("usuarioLogadoID")));
		} catch (NumberFormatException e) {
			usuario = null;
		}		
		if (usuario == null) {
			ctx.flash().put("url", "GET".equals(ctx.request().method()) ? ctx.request().uri() : "/");
			return F.Promise.pure(redirect(controllers.routes.Sessions.login()));

		} else if (configuration.value().equalsIgnoreCase("Administrador") && !usuario.isAdministrador) {
			ctx.flash().put("error", "Você não tem permissão para acessar este conteúdo!");
			return F.Promise.pure(redirect(controllers.routes.Application.index()));
		}

		return delegate.call(ctx);
	}
	*/
}
