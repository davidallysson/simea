package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Eixo;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class EixoController extends Controller {

	private FormFactory formFactory;

	@Inject
	public EixoController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}

	public Result index() {
		List<Eixo> eixo = Eixo.find.findList();
		return ok(views.html.Eixo.index.render(eixo));
	}
	
	@Permissao("Administrador")
	public Result formulario() {
        return ok(views.html.Eixo.formulario.render(formFactory.form(Eixo.class)));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		Form<Eixo> eixoForm = formFactory.form(Eixo.class).bindFromRequest();
        if(eixoForm.hasErrors()) {
            return badRequest(views.html.Eixo.formulario.render(eixoForm));
        }
        eixoForm.get().save();
        flash("success", "Eixo " + eixoForm.get().nome + " foi criado");
        return redirect(routes.EixoController.index());
	}

	public Result formularioEdicao(Long id) {
		Eixo eixo = Eixo.find.byId(id);
		Form<Eixo> eixoForm = formFactory.form(Eixo.class).fill(eixo);
	    return ok(views.html.Eixo.formularioEdicao.render(eixoForm, eixo));
	}

	public Result editar(Long id) {
		Eixo eixo = Eixo.find.byId(id);
		Form<Eixo> eixoForm = formFactory.form(Eixo.class).bindFromRequest();
        if(eixoForm.hasErrors()) {
            return badRequest(views.html.Eixo.formularioEdicao.render(eixoForm,eixo));
        }
        Transaction txn = Ebean.beginTransaction();
        try {
            Eixo eixoEdicao = Eixo.find.byId(id);
            if (eixoEdicao != null) {
                Eixo novoEixo = eixoForm.get();
                eixoEdicao.nome = novoEixo.nome;
                eixoEdicao.update();
                flash("success", "Eixo " + eixoForm.get().nome + " foi atualizado");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.EixoController.index());
	}

	public Result deletar(Long id) {
		Eixo eixo = Eixo.find.byId(id);
		if(eixo==null){
			flash().put("error", "O Eixo informado não foi encontrado no Sistema.");
		}else{
			 Eixo.find.ref(id).delete();
		}
		return redirect(routes.EixoController.index());
	}

	public Result get() {
		List<Eixo> eixo = Eixo.find.where().findList();
		return ok(play.libs.Json.toJson(eixo));
	}
}
