package controllers;

import helpers.Seguranca.InformacoesUsuarioHelper;
import models.Diretoria;
import models.Usuario;
import play.data.Form;
import play.mvc.*;

import views.html.*;

public class HomeController extends Controller {

    /**
     * Método responsável por exibir a tela inicial do sistema.
     */
    public Result index() {
        return ok(index.render());
    }

    /**
     * Método responsável por exebir a tela principal do sistema.
     * Caso o usuário não esteja logado ele será redirecionado para a tela inicial.
     */
    public Result homeIndex(){
    	if(InformacoesUsuarioHelper.isLogado()){
    		return ok(views.html.Home.index.render());
    	}else{
    		return ok(index.render());
    	}
    }

 }
