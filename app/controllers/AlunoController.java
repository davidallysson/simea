package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;

import emails.RegistroMailer;
import forms.UsuarioStatusForm;
import helpers.Seguranca.InformacoesUsuarioHelper;
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
public class AlunoController extends Controller {

	private FormFactory formFactory;

	@Inject
	public AlunoController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}
	
	@Permissao("Administrador")
	public Result index() {
		List<Usuario> alunos = Usuario.find.where().eq("is_administrador",false).eq("is_supervisor",false).findList();
		return ok(views.html.Aluno.index.render(alunos));
	}
	
	public Result visualizar(Long id) {
		/*
		CategoriaPergunta curso = CategoriaPergunta.find.byId(id);
		return ok(views.html.Cursos.visualizar.render(curso));
		*/
		return TODO;
	}
	
	public Result formulario() {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
        return ok(views.html.Aluno.formulario.render(formFactory.form(Usuario.class), turmas, cursos, diretorias, campus));
	}
	
	public Result cadastrar() {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		Form<Usuario> alunoForm = formFactory.form(Usuario.class).bindFromRequest();
        if(alunoForm.hasErrors()) {
            return badRequest(views.html.Aluno.formulario.render(alunoForm, turmas, cursos, diretorias, campus));
        }
        Long idCampus = Long.valueOf(alunoForm.data().get("idCampus"));
        Long idDiretoria = Long.valueOf(alunoForm.data().get("idDiretoria"));
        Long idCurso = Long.valueOf(alunoForm.data().get("idCurso"));
        Long idTurma = Long.valueOf(alunoForm.data().get("idTurma"));
        
        String senha = alunoForm.data().get("password");
        
        String idGenero = alunoForm.data().get("idGenero");
        Long idFaixaEtaria = Long.valueOf(alunoForm.data().get("idFaixaEtaria"));
        Long idEstadoCivil = Long.valueOf(alunoForm.data().get("idEstadoCivil"));
        Long idRaca = Long.valueOf(alunoForm.data().get("idRaca"));
        Long idRenda = Long.valueOf(alunoForm.data().get("idRenda"));
        
        Usuario usuario = alunoForm.get();
        usuario.campus = Campus.find.byId(idCampus);
        usuario.diretoria = Diretoria.find.byId(idDiretoria);
        usuario.curso = Curso.find.byId(idCurso);
        usuario.turma = Turma.find.byId(idTurma);
        usuario.isAdministrador = false;
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
        
        // Envia o email de confirmação de cadastro no sistema!
     	RegistroMailer.enviarMensagemRegistro(usuario);
     			
        if(InformacoesUsuarioHelper.isLogado()&&InformacoesUsuarioHelper.getUsuarioLogado().isAdministrador){
        	flash("success", "Cadastrado realizado com sucesso!");
        	 return redirect(routes.AlunoController.index());
        }else{
        	flash("success","Bem-vindo!");
        	session().put("usuarioLogadoID", usuario.id.toString());
        	return redirect(routes.HomeController.homeIndex());
        }
       
	}
	
	public Result formularioEdicao(Long id) {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		Usuario aluno = Usuario.find.byId(id);
		Form<Usuario> alunoForm = formFactory.form(Usuario.class).fill(aluno);
	    return ok(views.html.Aluno.formularioEdicao.render(alunoForm, aluno, turmas, cursos, diretorias, campus));
	}
	
	public Result editar(Long id) {
		List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		Usuario aluno = Usuario.find.byId(id);
		Form<Usuario> alunoForm = formFactory.form(Usuario.class).bindFromRequest();
		Long idCampus = Long.valueOf(alunoForm.data().get("idCampus"));
		Long idDiretoria = Long.valueOf(alunoForm.data().get("idDiretoria"));
		Long idCurso = Long.valueOf(alunoForm.data().get("idCurso"));
		Long idTurma = Long.valueOf(alunoForm.data().get("idTurma"));
		
        if(alunoForm.hasErrors()) {
        	System.out.println(alunoForm.errors());
            return badRequest(views.html.Aluno.formularioEdicao.render(alunoForm, aluno, turmas, cursos, diretorias, campus));
        }
        String senha = alunoForm.data().get("password");
        
        Transaction txn = Ebean.beginTransaction();
        try {
            Usuario usuarioEdicao = Usuario.find.byId(id);
            if (usuarioEdicao != null) {
            	 Usuario novoUsuario = alunoForm.get();
            	 usuarioEdicao.nome = novoUsuario.nome;
            	 usuarioEdicao.campus = Campus.find.byId(idCampus);
            	 usuarioEdicao.diretoria = Diretoria.find.byId(idDiretoria);
            	 usuarioEdicao.curso = Curso.find.byId(idCurso);
            	 usuarioEdicao.turma = Turma.find.byId(idTurma);
            	 usuarioEdicao.isAdministrador = false;
            	 usuarioEdicao.isSupervisor = false;
            	 usuarioEdicao.password = senha;
            	 usuarioEdicao.save();
                flash("success", "Aluno " + usuarioEdicao.nome + " foi atualizado");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.AlunoController.index());
	}
	
	public Result deletar(Long id) {
		 return TODO;
	}
	
	public Result statusAlunoForm() {
		UsuarioStatusForm aluno = new UsuarioStatusForm();
		return ok(views.html.Home.statusAluno.render(aluno));
	}

	@SuppressWarnings("static-access")
	public Result editarStatusAluno(){
		boolean statusAtivo=true;
		Form<UsuarioStatusForm> userForm = formFactory.form(UsuarioStatusForm.class).bindFromRequest();
		String id = userForm.data().get("idStatus");
		if(id.length()>1){
			UsuarioStatusForm aluno = new UsuarioStatusForm();
			flash("error", "Informe a sua situação corretamente!");
			return badRequest(views.html.Home.statusAluno.render(aluno));
		}else{
			Integer idStatus = Integer.valueOf(userForm.data().get("idStatus"));
			if(idStatus==1){
				statusAtivo=true;
			}else{
				statusAtivo=false;
			}
			Usuario user=InformacoesUsuarioHelper.getUsuarioLogado();
			user.setAtivo(statusAtivo);
			user.update();
//			Transaction txn = Ebean.beginTransaction();
//			 try {
//				 Usuario userUpdate = new Usuario();
//				 userUpdate.id = user.id;
//				 userUpdate.ativo = statusAtivo;
//				 userUpdate.update();
//		         txn.commit();
//		        } finally {
//		            txn.end();
//		        }
			if(statusAtivo){
				return redirect(routes.Solutions.newSolutionForm());
			}else{
				return redirect(routes.InativosController.formularioResponderInativos());
			}
		}
	}
	
	public Result get() {
		List<Usuario> alunos = Usuario.find.where().eq("isAdministrador", false).eq("isSupervisor", false).findList();
		return ok(play.libs.Json.toJson(alunos));
	}
}
