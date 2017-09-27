package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import helpers.Seguranca.Permissao;
import models.Usuario;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class SupervisorController extends Controller {

	private FormFactory formFactory;

    @Inject
    public SupervisorController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }
	
	public Result index() {
		List<Usuario> supervisores = Usuario.find.where().eq("is_administrador",false).eq("is_supervisor",true).findList();
		return ok(views.html.Supervisor.index.render(supervisores));
	}
	
	public Result visualizar(Long id) {
		/*
		CategoriaPergunta curso = CategoriaPergunta.find.byId(id);
		return ok(views.html.Cursos.visualizar.render(curso));
		*/
		return TODO;
	}
	
	@Permissao("Administrador")
	public Result formulario() {      
        //return ok(views.html.Campus.formulario.render(formFactory.form(Campus.class)));
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
//		Campus campus = Campus.find.byId(id);
//		Form<Campus> campusForm = formFactory.form(Campus.class).fill(campus);
//	    return ok(views.html.Campus.formularioEdicao.render(campusForm, campus));
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
//		Campus campus = Campus.find.byId(id);
//		if(campus==null){
//			flash().put("error", "O Campus informado não foi encontrado no Sistema.");
//		}else{
//			 Campus.find.ref(id).delete();
//		}
//		return redirect(routes.CampusController.index());
		return TODO;
	}
}
