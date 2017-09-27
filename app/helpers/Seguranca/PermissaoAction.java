package helpers.Seguranca;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.Context;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import models.Usuario;

public class PermissaoAction extends Action<Permissao> {
	
	public CompletionStage<Result> call(Http.Context ctx) {
		Usuario usuario = new Usuario();
		try {
			usuario = Usuario.find.byId(Long.valueOf(ctx.session().get("usuarioLogadoID")));
		} catch (NumberFormatException e) {
			usuario = null;
		}		
		if (usuario == null) {
			ctx.flash().put("url", "GET".equals(ctx.request().method()) ? ctx.request().uri() : "/");
			return CompletableFuture.completedFuture(redirect(controllers.routes.SessionsController.login()));

		} else if (configuration.value().equalsIgnoreCase("Administrador") && !usuario.isAdministrador) {
			ctx.flash().put("error", "Você não tem permissão para acessar este conteúdo!");
			return CompletableFuture.completedFuture(redirect(controllers.routes.HomeController.index()));
		}
		return delegate.call(ctx);
	}
}

