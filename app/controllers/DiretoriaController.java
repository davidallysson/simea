package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Campus;
import models.Diretoria;
import play.api.libs.json.Json;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class DiretoriaController extends Controller  {

	private FormFactory formFactory;

	@Inject
	public DiretoriaController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}

	public Result index() {
		List<Diretoria> diretoria = Diretoria.find.findList();
		return ok(views.html.Diretoria.index.render(diretoria));
	}

	@Permissao("Administrador")
	public Result formulario() {
		List<Campus> campus = Campus.find.findList();
        return ok(views.html.Diretoria.formulario.render(formFactory.form(Diretoria.class), campus));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		List<Campus> campus = Campus.find.findList();
		Form<Diretoria> diretoriaForm = formFactory.form(Diretoria.class).bindFromRequest();
		Long idCampus = Long.valueOf(diretoriaForm.data().get("idCampus"));
        if(diretoriaForm.hasErrors()) {
            return badRequest(views.html.Diretoria.formulario.render(diretoriaForm, campus));
        }
        Diretoria diretoria = diretoriaForm.get();
        diretoria.campus = Campus.find.byId(idCampus);
        diretoriaForm.get().save();
        flash("success", "Diretoria " + diretoriaForm.get().nome + " foi criado");
        return redirect(routes.DiretoriaController.index());
	}

	public Result formularioEdicao(Long id) {
		List<Campus> campus = Campus.find.findList();
		Diretoria diretoria = Diretoria.find.byId(id);
		Form<Diretoria> diretoriaForm = formFactory.form(Diretoria.class).fill(diretoria);
	    return ok(views.html.Diretoria.formularioEdicao.render(diretoriaForm, diretoria,campus));
	}

	public Result editar(Long id) {
		List<Campus> campus = Campus.find.findList();
		Diretoria diretoria = Diretoria.find.byId(id);
		Form<Diretoria> diretoriaForm = formFactory.form(Diretoria.class).bindFromRequest();
		Long idCampus = Long.valueOf(diretoriaForm.data().get("idCampus"));
        if(diretoriaForm.hasErrors()) {
            return badRequest(views.html.Diretoria.formularioEdicao.render(diretoriaForm,diretoria,campus));
        }
        Transaction txn = Ebean.beginTransaction();
        try {
            Diretoria diretoriaEdicao = Diretoria.find.byId(id);
            if (diretoriaEdicao != null) {
                Diretoria novoDiretoria = diretoriaForm.get();
                diretoriaEdicao.nome = novoDiretoria.nome;
                diretoriaEdicao.campus = Campus.find.byId(idCampus);
                diretoriaEdicao.update();
                flash("success", "Diretoria " + diretoriaForm.get().nome + " foi atualizado");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.DiretoriaController.index());
	}

	public Result deletar(Long id) {
		Diretoria diretoria = Diretoria.find.byId(id);
		if(diretoria==null){
			flash().put("error", "O Diretoria informado não foi encontrado no Sistema.");
		}else{
			 Diretoria.find.ref(id).delete();
		}
		return redirect(routes.DiretoriaController.index());
	}

	//var data = { 'one':1, 'two':2 };
	public Result get(Long id) {
		List<Diretoria> diretoria = Diretoria.find.where().eq("campus_id", id).findList();
		return ok(play.libs.Json.toJson(diretoria));
//		List<String> diretorias = new ArrayList<String>();
//		for (Diretoria d : diretoria) {
//			diretorias.add(d.nome);
//		}
//		return ok(diretorias.toString());
	}
}
