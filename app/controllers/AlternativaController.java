package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import helpers.Seguranca.Permissao;
import models.Answer;
import models.Questao;
import models.Quiz;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class AlternativaController extends Controller{

	private FormFactory formFactory;

    @Inject
    public AlternativaController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }
    
    public Result index() {
		List<Answer> alternativas = Answer.find.findList();
		return ok(views.html.Alternativa.index.render(alternativas));
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
        return ok(views.html.Quiz.formulario.render(formFactory.form(Quiz.class)));
	}
	
	@Permissao("Administrador")
	public Result cadastrar() {
		List<Questao> questoes = Questao.find.findList();
		Form<Answer> quizForm = formFactory.form(Answer.class).bindFromRequest();
        if(quizForm.hasErrors()) {
            //return badRequest(views.html.Alternativa.formulario.render(quizForm));
            return TODO;
        }
        quizForm.get().save();
        flash("success", "Questionário " + quizForm.get().answer + " foi criado");
        return redirect(routes.QuizController.index());
	}
	
	public Result formularioEdicao(Long id) {
		Quiz quiz = Quiz.find.byId(id);
		Form<Quiz> quizForm = formFactory.form(Quiz.class).fill(quiz);
	 //   return ok(views.html.Quiz.formularioEdicao.render(quizForm, quiz));
		return TODO;
	}
	
	public Result editar(Long id) {
		Answer quiz = Answer.find.byId(id);
		Form<Answer> quizForm = formFactory.form(Answer.class).bindFromRequest();
		Long idQuestao = Long.valueOf(quizForm.data().get("idQuestao"));
        if(quizForm.hasErrors()) {
        //    return badRequest(views.html.Alternativa.formularioEdicao.render(quizForm,quiz));
        	return TODO;
        }
        Transaction txn = Ebean.beginTransaction();
        try {
        	Answer quizEdicao = Answer.find.byId(id);
            if (quizEdicao != null) {
            	Answer novoQuiz = quizForm.get();
            	quizEdicao.answer = novoQuiz.answer;
            	quizEdicao.ordem = novoQuiz.ordem;
            	quizEdicao.valid = novoQuiz.valid;
            	quizEdicao.update();
                flash("success", "Questionário: " + quizForm.get().answer + " foi atualizado");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return redirect(routes.AlternativaController.index());
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
