package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Campus;
import models.Curso;
import models.Diretoria;
import models.Turma;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class TurmaController extends Controller {

	private FormFactory formFactory;

	@Inject
	public TurmaController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}

	public Result index() {
		List<Turma> turma = Turma.find.findList();
		return ok(views.html.Turma.index.render(turma));
	}

	@Permissao("Administrador")
	public Result formulario() {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
        return ok(views.html.Turma.formulario.render(formFactory.form(Turma.class), cursos, diretorias, campus));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		Form<Turma> turmaForm = formFactory.form(Turma.class).bindFromRequest();
        if(turmaForm.hasErrors()) {
            return badRequest(views.html.Turma.formulario.render(turmaForm, cursos, diretorias, campus));
        }
        Long idCampus = Long.valueOf(turmaForm.data().get("idCampus"));
        Long idDiretoria = Long.valueOf(turmaForm.data().get("idDiretoria"));
        Long idCurso = Long.valueOf(turmaForm.data().get("idCurso"));

        Turma turma = turmaForm.get();
        turma.campus = Campus.find.byId(idCampus);
        turma.diretoria = Diretoria.find.byId(idDiretoria);
        turma.curso = Curso.find.byId(idCurso);
        turma.save();
        flash("success", "Turma " + turmaForm.get().nome + " foi criada");
        return redirect(routes.TurmaController.index());

	}

	public Result formularioEdicao(Long id) {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		Turma turma = Turma.find.byId(id);
		Form<Turma> turmaForm = formFactory.form(Turma.class).fill(turma);
	    return ok(views.html.Turma.formularioEdicao.render(turmaForm, turma, cursos, diretorias, campus));
	}

	public Result editar(Long id) {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		Turma turma = Turma.find.byId(id);
		Form<Turma> turmaForm = formFactory.form(Turma.class).bindFromRequest();
		Long idCampus = Long.valueOf(turmaForm.data().get("idCampus"));
		Long idDiretoria = Long.valueOf(turmaForm.data().get("idDiretoria"));
		Long idCurso = Long.valueOf(turmaForm.data().get("idCurso"));

        if(turmaForm.hasErrors()) {
            return badRequest(views.html.Turma.formularioEdicao.render(turmaForm, turma, cursos, diretorias, campus));
        }
        Transaction txn = Ebean.beginTransaction();
        try {
            Turma turmaEdicao = Turma.find.byId(id);
            if (turmaEdicao != null) {
                Turma novoTurma = turmaForm.get();
                turmaEdicao.nome = novoTurma.nome;
                turmaEdicao.campus = Campus.find.byId(idCampus);
                turmaEdicao.diretoria = Diretoria.find.byId(idDiretoria);
                turmaEdicao.curso = Curso.find.byId(idCurso);
                turmaEdicao.update();
                flash("success", "Turma " + turmaForm.get().nome + " foi atualizada");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.TurmaController.index());
	}

	public Result deletar(Long id) {
		Turma turma = Turma.find.byId(id);
		if(turma==null){
			flash().put("error", "A Turma informada não foi encontrada no Sistema.");
		}else{
			 Turma.find.ref(id).delete();
		}
		return redirect(routes.TurmaController.index());
	}

	public Result get(Long id) {
		List<Turma> turmas = Turma.find.where().eq("curso_id", id).findList();
		return ok(play.libs.Json.toJson(turmas));
	}
}
