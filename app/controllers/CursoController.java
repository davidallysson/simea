package controllers;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Campus;
import models.Curso;
import models.Diretoria;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import play.routing.JavaScriptReverseRouter;

public class CursoController extends Controller {

	private FormFactory formFactory;

	@Inject
	public CursoController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}
	
	public Result index() {
		List<Curso> cursos = Curso.find.findList();
		return ok(views.html.Curso.index.render(cursos));
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
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
        return ok(views.html.Curso.formulario.render(formFactory.form(Curso.class),diretorias, campus));
	}
	
	@Permissao("Administrador")
	public Result cadastrar() {
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		Form<Curso> cursoForm = formFactory.form(Curso.class).bindFromRequest();
		Long idCampus = Long.valueOf(cursoForm.data().get("idCampus"));
		Long idDiretoria = Long.valueOf(cursoForm.data().get("idDiretoria"));
        if(cursoForm.hasErrors()) {
            return badRequest(views.html.Curso.formulario.render(cursoForm, diretorias, campus));
        }
        Curso curso = cursoForm.get();
        curso.campus = Campus.find.byId(idCampus);
        curso.diretoria = Diretoria.find.byId(idDiretoria);
        curso.save();
        flash("success", "Curso " + cursoForm.get().nome + " foi criado");
        return redirect(routes.CursoController.index());
	}
	
	public Result formularioEdicao(Long id) {
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		Curso curso = Curso.find.byId(id);
		Form<Curso> cursoForm = formFactory.form(Curso.class).fill(curso);
	    return ok(views.html.Curso.formularioEdicao.render(cursoForm, curso, diretorias,campus));
	}
	
	public Result editar(Long id) {
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		Curso curso = Curso.find.byId(id);
		Form<Curso> cursoForm = formFactory.form(Curso.class).bindFromRequest();
		Long idCampus = Long.valueOf(cursoForm.data().get("idCampus"));
		Long idDiretoria = Long.valueOf(cursoForm.data().get("idDiretoria"));
        if(cursoForm.hasErrors()) {
            return badRequest(views.html.Curso.formularioEdicao.render(cursoForm,curso,diretorias,campus));
        }
        Transaction txn = Ebean.beginTransaction();
        try {
            Curso cursoEdicao = Curso.find.byId(id);
            if (cursoEdicao != null) {
                Curso novoDiretoria = cursoForm.get();
                cursoEdicao.nome = novoDiretoria.nome;
                cursoEdicao.campus = Campus.find.byId(idCampus);
                cursoEdicao.diretoria = Diretoria.find.byId(idDiretoria);
                cursoEdicao.update();
                flash("success", "Curso " + cursoEdicao.nome + " foi atualizado");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.CursoController.index());
		
	}
	
	public Result deletar(Long id) {
		/*Curso diretoria = Curso.find.byId(id);
		if(diretoria==null){
			flash().put("error", "O Curso informado não foi encontrado no Sistema.");
		}else{
			 Curso.find.ref(id).delete();
		}
		return redirect(routes.DiretoriaController.index());*/
		
		return TODO;
	}
	
	public Result javascriptRoutes() {
	    return ok(
	        JavaScriptReverseRouter.create("jsRoutes",
	            routes.javascript.DiretoriaController.index(),
	            routes.javascript.DiretoriaController.get(),
	            routes.javascript.CursoController.get(),
	            routes.javascript.TurmaController.get(),
	            routes.javascript.EixoController.get(),
	            routes.javascript.AlunoController.get(),
	            routes.javascript.ResultadosController.consultarCompararDados(),
	            routes.javascript.ResultadosController.testeOk(),
	            routes.javascript.ResultadosController.consultarEvasometro1(),
	            routes.javascript.ResultadosController.consultarEvasometro2(),
	            routes.javascript.ResultadosController.consultarEixo1(),
	            routes.javascript.ResultadosController.consultarEixo2(),
	            routes.javascript.ResultadosController.consultarSexo1(),
	            routes.javascript.ResultadosController.consultarSexo2(),
	            routes.javascript.ResultadosController.consultarFaixaEtaria1(),
	            routes.javascript.ResultadosController.consultarFaixaEtaria2(),
	            routes.javascript.ResultadosController.consultarEstadoCivil1(),
	            routes.javascript.ResultadosController.consultarEstadoCivil2(),
	            routes.javascript.ResultadosController.consultarRaca1(),
	            routes.javascript.ResultadosController.consultarRaca2(),
	            routes.javascript.ResultadosController.consultarRenda1(),
	            routes.javascript.ResultadosController.consultarRenda2(),
	            routes.javascript.ResultadosController.exportCSV()
	        )
	    ).as("text/javascript");
	}

	
	public Result get(Long id) {
		List<Curso> cursos = Curso.find.where().eq("diretoria_id", id).findList();
		return ok(play.libs.Json.toJson(cursos));
	}
	 
}
