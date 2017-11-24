package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import helpers.Seguranca.InformacoesUsuarioHelper;
import helpers.Seguranca.Permissao;
import models.Answer;
import models.Campus;
import models.Diretoria;
import models.Eixo;
import models.Questao;
import models.Quiz;
import models.Usuario;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class QuestaoController extends Controller {

	private FormFactory formFactory;

	@Inject
	public QuestaoController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}

	public Result index() {
		List<Questao> questoes = Questao.find.findList();
		return ok(views.html.Questao.index.render(questoes));
	}

	@Permissao("Administrador")
	public Result formulario() {
		List<Quiz> quiz = Quiz.find.findList();
		List<Eixo> eixos = Eixo.find.findList();
    return ok(views.html.Questao.formulario.render(formFactory.form(Questao.class),eixos,quiz));
	}

	@Permissao("Administrador")
	public Result cadastrar() {
		List<Quiz> quiz = Quiz.find.findList();
		List<Eixo> eixos = Eixo.find.findList();
		Form<Questao> questaoForm = formFactory.form(Questao.class).bindFromRequest();
		Long idEixo = Long.valueOf(questaoForm.data().get("idEixo"));
		Long idQuiz = Long.valueOf(questaoForm.data().get("idQuiz"));
        if(questaoForm.hasErrors()) {
            return badRequest(views.html.Questao.formulario.render(questaoForm, eixos, quiz));
        }
        Questao questao = questaoForm.get();
        questao.eixo = Eixo.find.byId(idEixo);
        questao.quiz = Quiz.find.byId(idQuiz);
        questao.usuario = InformacoesUsuarioHelper.getUsuarioLogado();
				// questao.status = true;
        questao.save();

        Answer alternativa1 = new Answer(questaoForm.get().alternativa1, true, questao, 1, 1);
        alternativa1.save();

        Answer alternativa2 = new Answer(questaoForm.get().alternativa2, true, questao, 2, 2);
        alternativa2.save();

        Answer alternativa3 = new Answer(questaoForm.get().alternativa3, true, questao, 3, 3);
        alternativa3.save();

        Answer alternativa4 = new Answer(questaoForm.get().alternativa4, true, questao, 4, 4);
        alternativa4.save();


        flash("success", "Pergunta: " + questaoForm.get().pergunta + " foi cadastrada");
        return redirect(routes.QuestaoController.index());
	}

	public Result formularioEdicao(Long id) {
		List<Quiz> quiz = Quiz.find.findList();
		List<Eixo> eixos = Eixo.find.findList();
		Questao questao = Questao.find.byId(id);
		Form<Questao> questaoForm = formFactory.form(Questao.class).fill(questao);
		List<Answer> alternativas = questao.getAnswers();
	  return ok(views.html.Questao.formularioEdicao.render(questaoForm, questao, eixos, quiz, alternativas));
	}

	public Result editar(Long id) {
		Questao questao = Questao.find.byId(id);
		List<Quiz> quiz = Quiz.find.findList();
		List<Eixo> eixos = Eixo.find.findList();
		List<Answer> alternativas = questao.getAnswers();
		Form<Questao> questaoForm = formFactory.form(Questao.class).bindFromRequest();
		Long idEixo = Long.valueOf(questaoForm.data().get("idEixo"));
		Long idQuiz = Long.valueOf(questaoForm.data().get("idQuiz"));
    if(questaoForm.hasErrors()) {
      return badRequest(views.html.Questao.formularioEdicao.render(questaoForm, questao, eixos, quiz, alternativas));
    }
    Transaction txn = Ebean.beginTransaction();
    try {
				Questao questaoEdicao = Questao.find.byId(id);
				if (questaoEdicao != null) {
						Questao novoQuestao = questaoForm.get();
						questaoEdicao.pergunta = novoQuestao.pergunta;
						questaoEdicao.alternativa1 = novoQuestao.alternativa1;
						questaoEdicao.alternativa2 = novoQuestao.alternativa2;
						questaoEdicao.alternativa3 = novoQuestao.alternativa3;
						questaoEdicao.alternativa4 = novoQuestao.alternativa4;
						questaoEdicao.eixo = Eixo.find.byId(idEixo);
						questaoEdicao.quiz = Quiz.find.byId(idQuiz);
						questaoEdicao.usuario = InformacoesUsuarioHelper.getUsuarioLogado();
						// questaoEdicao.status = true;
						questaoEdicao.update();

						alternativas.get(0).answer = novoQuestao.alternativa1;
						alternativas.get(0).update();

						alternativas.get(1).answer = novoQuestao.alternativa2;
						alternativas.get(1).update();

						alternativas.get(2).answer = novoQuestao.alternativa3;
						alternativas.get(2).update();

						alternativas.get(3).answer = novoQuestao.alternativa4;
						alternativas.get(3).update();

            flash("success", "Questao \"" + questaoEdicao.pergunta + "\" foi atualizado");
          	txn.commit();
           }
  	} finally {
        txn.end();
    }
    return redirect(routes.QuestaoController.index());
	}

	public Result deletar(Long id) {
		Questao questao = Questao.find.byId(id);
		List<Answer> alternativas = questao.getAnswers();
		if (questao==null) {
			flash().put("error", "A Questao informada não foi encontrada no Sistema.");
		} else {
				for (Answer alternativa : alternativas) {
						Answer.find.ref(alternativa.getId()).delete();
				}
				Questao.find.ref(id).delete();
		}
		return redirect(routes.QuestaoController.index());
	}
}
