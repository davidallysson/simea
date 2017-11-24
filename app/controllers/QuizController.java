package controllers;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.InformacoesUsuarioHelper;
import helpers.Seguranca.Permissao;
import models.Quiz;
import models.Usuario;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class QuizController extends Controller {

	private FormFactory formFactory;

  @Inject
  public QuizController(FormFactory formFactory) {
      this.formFactory = formFactory;
  }

  public Result index() {
		List<Quiz> campus = Quiz.find.findList();
		return ok(views.html.Quiz.index.render(campus));
	}

	@Permissao("Administrador")
	public Result formulario() {
    return ok(views.html.Quiz.formulario.render(formFactory.form(Quiz.class)));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		Form<Quiz> quizForm = formFactory.form(Quiz.class).bindFromRequest();
    if(quizForm.hasErrors()) {
        return badRequest(views.html.Quiz.formulario.render(quizForm));
    }
    quizForm.get().difficulty = 1;
    quizForm.get().creator = InformacoesUsuarioHelper.getUsuarioLogado();
    quizForm.get().active = true;
    quizForm.get().start = new Date();
    quizForm.get().fim = new Date();
    quizForm.get().save();
    flash("success", "Questionário " + quizForm.get().name + " foi criado");
    return redirect(routes.QuizController.index());
	}

	public Result formularioEdicao(Long id) {
		Quiz quiz = Quiz.find.byId(id);
		Form<Quiz> quizForm = formFactory.form(Quiz.class).fill(quiz);
	  return ok(views.html.Quiz.formularioEdicao.render(quizForm, quiz));
	}

	public Result editar(Long id) {
		Quiz quiz = Quiz.find.byId(id);
		Form<Quiz> quizForm = formFactory.form(Quiz.class).bindFromRequest();
        if(quizForm.hasErrors()) {
            return badRequest(views.html.Quiz.formularioEdicao.render(quizForm,quiz));
        }
        Transaction txn = Ebean.beginTransaction();
        try {
        	Quiz quizEdicao = Quiz.find.byId(id);
            if (quizEdicao != null) {
            	Quiz novoQuiz = quizForm.get();
            	quizEdicao.name = novoQuiz.name;
            	quizEdicao.category = novoQuiz.category;
            	quizEdicao.creator = InformacoesUsuarioHelper.getUsuarioLogado();
            	quizEdicao.start = new Date();
            	quizEdicao.fim = new Date();
            	quizEdicao.update();
              flash("success", "Questionário: " + quizForm.get().name + " foi atualizado");
              txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.QuizController.index());
	}

	public Result deletar(Long id) {
		Quiz campus = Quiz.find.byId(id);
		if(campus==null){
			flash().put("error", "O Questionário informado não foi encontrado no Sistema.");
		}else{
			Quiz.find.ref(id).delete();
		}
		return redirect(routes.QuizController.index());
	}
}
