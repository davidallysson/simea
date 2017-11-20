package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Campus;
import play.mvc.Controller;
import play.mvc.Result;

import play.data.*;

@Singleton
public class CampusController extends Controller {

  private FormFactory formFactory;

  @Inject
  public CampusController(FormFactory formFactory) {
      this.formFactory = formFactory;
  }

	public Result index() {
		List<Campus> campus = Campus.find.findList();
		return ok(views.html.Campus.index.render(campus));
	}

	@Permissao("Administrador")
	public Result formulario() {
    return ok(views.html.Campus.formulario.render(formFactory.form(Campus.class)));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		Form<Campus> campusForm = formFactory.form(Campus.class).bindFromRequest();
        if(campusForm.hasErrors()) {
            return badRequest(views.html.Campus.formulario.render(campusForm));
        }
        campusForm.get().save();
        flash("success", "Campus " + campusForm.get().nome + " foi criado");
        return redirect(routes.CampusController.index());
	}

	public Result formularioEdicao(Long id) {
		Campus campus = Campus.find.byId(id);
		Form<Campus> campusForm = formFactory.form(Campus.class).fill(campus);
	    return ok(views.html.Campus.formularioEdicao.render(campusForm, campus));
	}

	public Result editar(Long id) {
		Campus campus = Campus.find.byId(id);
		Form<Campus> campusForm = formFactory.form(Campus.class).bindFromRequest();
        if(campusForm.hasErrors()) {
            return badRequest(views.html.Campus.formularioEdicao.render(campusForm,campus));
        }
        Transaction txn = Ebean.beginTransaction();
        try {
            Campus campusEdicao = Campus.find.byId(id);
            if (campusEdicao != null) {
                Campus novoCampus = campusForm.get();
                campusEdicao.nome = novoCampus.nome;
                campusEdicao.update();
                flash("success", "Campus " + campusForm.get().nome + " foi atualizado");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.CampusController.index());
	}

	public Result deletar(Long id) {
		Campus campus = Campus.find.byId(id);
		if(campus==null){
		  flash().put("error", "O Campus informado não foi encontrado no Sistema.");
		}else{
			Campus.find.ref(id).delete();
		}
		return redirect(routes.CampusController.index());
	}

}
