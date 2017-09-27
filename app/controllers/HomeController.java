package controllers;

import helpers.Seguranca.InformacoesUsuarioHelper;
import models.Diretoria;
import models.Usuario;
import play.data.Form;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render());
    }
    
    
    /**
     * Tela principal do sistema.
     * @return
     */
    public Result homeIndex(){
    	if(InformacoesUsuarioHelper.isLogado()){
    		return ok(views.html.Home.index.render());
    	}else{
    		return ok(index.render());
    	}
    }
    
 }
