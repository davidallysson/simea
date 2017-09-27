package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import forms.InativoForm;
import helpers.Seguranca.Permissao;
import models.Eixo;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class InativosController  extends Controller {

	private FormFactory formFactory;

	@Inject
	public InativosController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}
	
	/**
	 * Lista os formularios de inativos criados
	 * @return
	 */
	public Result index() {
		List<Eixo> eixo = Eixo.find.findList();
		return ok(views.html.Eixo.index.render(eixo));
	}
	
	/**
	 * Form para criar um formulario pelo admin
	 * @return
	 */
	public Result formulario() {
		List<Eixo> eixo = Eixo.find.findList();
		return ok(views.html.Eixo.index.render(eixo));
	}
	
	/**
	 * Form para os alunos inativos responderem
	 * @return
	 */
	public Result formularioResponderInativos() {
		return ok(views.html.Inativos.formularioResponderInativos.render(formFactory.form(InativoForm.class)));
	}
	
	public Result responder() {
		Form<InativoForm> form=formFactory.form(InativoForm.class).bindFromRequest();
		if (form.hasErrors()) {
			flash("error","Dados inválidos. Tente novamente!");
			return badRequest(views.html.Inativos.formularioResponderInativos.render(form));
		} else {
			if("true".equals(form.field("opcao1").value())) {
				System.out.println("opcao 01");
			}
			flash("success","Agradecemos as suas respostas!");
			return redirect(routes.HomeController.homeIndex());
		}
	} 
}
