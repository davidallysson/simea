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
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
    return ok(views.html.Administrador.formulario.render(formFactory.form(Usuario.class), turmas, cursos, diretorias, campus));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		Form<Usuario> adminForm = formFactory.form(Usuario.class).bindFromRequest();
				if(adminForm.hasErrors()) {
						return badRequest(views.html.Administrador.formulario.render(adminForm, turmas, cursos, diretorias, campus));
				}
				Long idCampus = Long.valueOf(adminForm.data().get("idCampus"));
				Long idDiretoria = Long.valueOf(adminForm.data().get("idDiretoria"));
				Long idCurso = Long.valueOf(adminForm.data().get("idCurso"));
				Long idTurma = Long.valueOf(adminForm.data().get("idTurma"));

				String senha = adminForm.data().get("password");

				String idGenero = adminForm.data().get("idGenero");
				Long idFaixaEtaria = Long.valueOf(adminForm.data().get("idFaixaEtaria"));
				Long idEstadoCivil = Long.valueOf(adminForm.data().get("idEstadoCivil"));
				Long idRaca = Long.valueOf(adminForm.data().get("idRaca"));
				Long idRenda = Long.valueOf(adminForm.data().get("idRenda"));

				Usuario usuario = adminForm.get();
				usuario.campus = Campus.find.byId(idCampus);
				usuario.diretoria = Diretoria.find.byId(idDiretoria);
				usuario.curso = Curso.find.byId(idCurso);
				usuario.turma = Turma.find.byId(idTurma);
				usuario.isAdministrador = true;
				usuario.isSupervisor = false;
				usuario.password = senha;
				usuario.faixaEtaria = idFaixaEtaria.intValue();
				usuario.estadoCivil = idEstadoCivil.intValue();
				usuario.raca = idRaca.intValue();
				usuario.renda = idRenda.intValue();

				if(idGenero.equalsIgnoreCase("M")){
					usuario.masculino=true;
					usuario.feminino=false;
				}else{
					usuario.masculino=false;
					usuario.feminino=true;
				}

				usuario.save();

				flash("success", "Cadastrado realizado com sucesso!");
				return redirect(routes.AdministradorController.index());
	}

	public Result formularioEdicao(Long id) {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		Usuario administrador = Usuario.find.byId(id);
		Form<Usuario> administradorForm = formFactory.form(Usuario.class).fill(administrador);
	  return ok(views.html.Administrador.formularioEdicao.render(administradorForm, administrador, turmas, cursos, diretorias, campus));
	}

	public Result editar(Long id) {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		Usuario administrador = Usuario.find.byId(id);
		Form<Usuario> adminForm = formFactory.form(Usuario.class).bindFromRequest();
		Long idCampus = Long.valueOf(adminForm.data().get("idCampus"));
		Long idDiretoria = Long.valueOf(adminForm.data().get("idDiretoria"));
		Long idCurso = Long.valueOf(adminForm.data().get("idCurso"));
		Long idTurma = Long.valueOf(adminForm.data().get("idTurma"));

		if(adminForm.hasErrors()) {
			System.out.println(adminForm.errors());
			return badRequest(views.html.Administrador.formularioEdicao.render(adminForm, administrador, turmas, cursos, diretorias, campus));
		}
		String senha = adminForm.data().get("password");
		Transaction txn = Ebean.beginTransaction();
				try {
						Usuario usuarioEdicao = Usuario.find.byId(id);
						if (usuarioEdicao != null) {
							Usuario novoUsuario = adminForm.get();
							usuarioEdicao.nome = novoUsuario.nome;
							usuarioEdicao.matricula = novoUsuario.matricula;
							usuarioEdicao.email = novoUsuario.email;
							usuarioEdicao.campus = Campus.find.byId(idCampus);
							usuarioEdicao.diretoria = Diretoria.find.byId(idDiretoria);
							usuarioEdicao.curso = Curso.find.byId(idCurso);
							usuarioEdicao.turma = Turma.find.byId(idTurma);
							usuarioEdicao.isAdministrador = true;
							usuarioEdicao.isSupervisor = false;
							usuarioEdicao.password = senha;
							usuarioEdicao.update();
							
							flash("success", "Administrador " + usuarioEdicao.nome + " foi atualizado");
							txn.commit();
						}
				} finally {
						txn.end();
				}
				return redirect(routes.AdministradorController.index());
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
