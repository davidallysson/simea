package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Campus;
import models.Usuario;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class AdministradorController extends Controller {

	private FormFactory formFactory;

    @Inject
    public AdministradorController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

	public Result index() {
		List<Usuario> administradores = Usuario.find.where().eq("is_administrador",true).eq("is_supervisor",false).findList();
		return ok(views.html.Administrador.index.render(administradores));
	}

	@Permissao("Administrador")
	public Result formulario() {
    // return ok(views.html.Administrador.formulario.render(formFactory.form(Usuario.class)));
		return TODO;
	}

	@Permissao("Administrador")
	public Result cadastrar() {
//		Form<Campus> campusForm = formFactory.form(Campus.class).bindFromRequest();
//        if(campusForm.hasErrors()) {
//            return badRequest(views.html.Campus.formulario.render(campusForm));
//        }
//        campusForm.get().save();
//        flash("success", "Campus " + campusForm.get().nome + " foi criado");
//        return redirect(routes.CampusController.index());
		return TODO;
	}

	public Result formularioEdicao(Long id) {
		// Usuario administrador = Usuario.find.byId(id);
		// Form<Usuario> administradorForm = formFactory.form(Usuario.class).fill(administrador);
	  // return ok(views.html.Campus.formularioEdicao.render(administradorForm, administrador));
		return TODO;
	}

	public Result editar(Long id) {
//		Campus campus = Campus.find.byId(id);
//		Form<Campus> campusForm = formFactory.form(Campus.class).bindFromRequest();
//        if(campusForm.hasErrors()) {
//            return badRequest(views.html.Campus.formularioEdicao.render(campusForm,campus));
//        }
//        Transaction txn = Ebean.beginTransaction();
//        try {
//            Campus campusEdicao = Campus.find.byId(id);
//            if (campusEdicao != null) {
//                Campus novoCampus = campusForm.get();
//                campusEdicao.nome = novoCampus.nome;
//                campusEdicao.update();
//                flash("success", "Campus " + campusForm.get().nome + " foi atualizado");
//                txn.commit();
//            }
//        } finally {
//            txn.end();
//        }
//        return redirect(routes.CampusController.index());
		return TODO;
	}

	public Result deletar(Long id) {
		Usuario administradores = Usuario.find.byId(id);
		if(administradores==null){
			flash().put("error", "O Administrador informado não foi encontrado no Sistema.");
		}else{
			 Usuario.find.ref(id).delete();
		}
		return redirect(routes.AdministradorController.index());
	}
}
