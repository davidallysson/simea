package controllers;

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
		List<String> alternativas = new ArrayList<String>();
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
		// Questao questao = Questao.find.byId(id);
		// if(questao==null){
		// 	flash().put("error", "A Questao informada não foi encontrada no Sistema.");
		// }else{
		// 	Questao.find.ref(id).delete();
		// }
		// return redirect(routes.QuestaoController.index());
		return TODO;
	}
}
