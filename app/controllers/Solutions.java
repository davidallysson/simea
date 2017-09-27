package controllers;

import helpers.Seguranca.InformacoesUsuarioHelper;
import play.cache.Cache;
import play.cache.Cached;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.avaje.ebean.Ebean;

import models.Answer;
import models.Questao;
import models.Quiz;
import models.Solution;
import models.Usuario;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import structures.Score;
import scala.collection.JavaConversions.*;

public class Solutions extends Controller {
	
	/**
	 * 2
	 * @return
	 */
	public Result setup() {
		if (!InformacoesUsuarioHelper.isLogado()) {
			return redirect(routes.SessionsController.login());
		} else {
//			InformacoesUsuarioHelper.verificaMudancaLevel();
//			if (!InformacoesUsuarioHelper.contaAtivada()) {
//				return badRequest(views.html.solutions.newSolution.render(carregaCategoriasEscolhidas()));
//			}else {
				response().setContentType("application/json");
				Usuario user = InformacoesUsuarioHelper.getUsuarioLogado();
				System.out.println("nome do user: "+user.nome);
				Solution solucao  = Solution.find.where().eq("quiz_participant_id", user.id).findUnique();
				System.out.println("solucao: "+solucao);
				if(solucao != null){
					flash().put("warning", "O teste de nivelamento em " + solucao.quiz.category + " ainda não foi concluído!");
					return redirect("/solutions/" + solucao.id+ "/question");
				}
			    DynamicForm requestData = play.data.Form.form().bindFromRequest();
			    System.out.println("Request data"+requestData);
				Quiz q = Quiz.find.where().findUnique();
				if (q == null)
					return notFound("unknown quiz");
				if (!q.isActive())
					return badRequest("Given quiz is not active.\n");
				
				String email = InformacoesUsuarioHelper.getUsuarioLogado().email;
				System.out.println("Usuario = "+ email);
				Usuario u = Usuario.find.where().eq("email", email).findUnique();
				if (u == null)
					return notFound("unknown user");
				Solution s = Solution.find(u, q);

				if (s == null) {
					s = new Solution(u,q);
					s.codigoHabilitacaoNerd = q.id.intValue(); 
					s.save();
					
					List<Questao> questions = Questao.find.orderBy().asc("eixo_id").where().eq("quiz", q).findList();
			    	Collections.shuffle(questions, new Random());
			    	s.questions.addAll(questions);
			    	//s.saveManyToManyAssociations("questions");
			    	Ebean.saveManyToManyAssociations(s, "questions");
			    	s.firstQuestionShow = false;
			    	s.update();
				} else {
					if(s.isDone()) {
					s.setFirstTime(false);
					s.setDone(false);
					s.startSolution();
					s.update();
			    	
					} else
						//return badRequest("Finish your current quiz-session before you start over");
					return redirect("/solutions/" + s.id + "/question");
				}
				return redirect("/solutions/" + s.id + "/question");
			}
		}
//	}
	
	/**
	 * 4
	 */
	public Result solve(Long id) {
		// response().setContentType("application/json"); 
		response().setHeader("Cache-Control", "no-cache"); 
		response().setHeader("Cache-Control", "public"); 
		response().setHeader("Cache-Control", "max-age=0");
		DynamicForm requestData = play.data.Form.form().bindFromRequest();
		
		Long idQuestion = Long.valueOf(requestData.get("id"));
		String resposta = requestData.get("answer");
		
		Solution solution = Solution.find.byId(id);
		Questao question = solution.getQuestion(idQuestion, solution);
		
		if(requestData.get("answer") == null) {
			flash().put("error", "Informe uma das alternativas. Tente novamente!");
			//return redirect("/solutions/" + id + "/question");
			return badRequest(views.html.Quiz.showQuestion.render(question, id));
		}
		
		
		
		Usuario user = InformacoesUsuarioHelper.getUsuarioLogado();
		Solution solucao  = Solution.find.where().eq("quiz_participant_id", user.id).findUnique();
		Solution s = Solution.find.byId(id);
		if (s == null)
			return badRequest("No Setup for Solution");
		if (s.isDone())
			return redirect("/solutions/" + id + "/score");
			//return badRequest("Last question already answered\n");
		if(s.isPaused())
			return badRequest("The quiz is currently paused\n");
		int result = s.evaluateAnswer(requestData.get("answer"), s.id, idQuestion);
		Quiz.removeQuestion(s.id, question.id);
		if(result != -1) {
			s.incCurrentQuestion();
			s.update();
			String cor = result > 0 ? "Richtig :-)" : "Falsch :'-(";
			
			return redirect("/solutions/" + id + "/question");
			//return getQuestion(id);
			
			//return ok(views.html.questions.answeredQuestion.render(cor, id));
		} else
			//return newSolutionForm();
			
			//return redirect("/solutions/" + solucao.id + "/question");
			return badRequest("Answer does not belong to current question");
	}
	
	/**
	 * 3
	 * @param id
	 * @return
	 */
	public Result getQuestion(Long id) {
		
		response().setHeader("Cache-Control", "no-cache"); 
		response().setHeader("Cache-Control", "public"); 
		response().setHeader("Cache-Control", "max-age=0");
		/*
		Solution s = Solution.find.byId(id);
		Question question = s.getQuestion();
		
		if(question == null) {
			s.finish();
			s.update();
			return redirect("/solutions/" + id + "/score");
		} else {
			// Cache for 15 minutes
		//	return ok(views.html.questions.showQuestion.render(question, id));
			return redirect(routes.Solutions.recebeQuestion(id));
		}
		*/
		return redirect(routes.Solutions.recebeQuestion(id));
	}
	
	public Result recebeQuestion(Long id) {
		response().setHeader("Cache-Control", "no-cache"); 
		response().setHeader("Cache-Control", "public"); 
		response().setHeader("Cache-Control", "max-age=0");
		Solution s = Solution.find.byId(id);
		Questao question = s.getQuestionAletorio(s.id);
		
		if(question == null) {
			s.finish();
			s.update();
			return redirect("/solutions/" + id + "/score");
		} else {
			return ok(views.html.Quiz.showQuestion.render(question, id));
		}
	}
	
	public Result toggleStatus(Long id) {
		Solution s = Solution.find.byId(id);
		String output = new String();
		if(s == null) {
			return notFound("unknown solution");
		}
		if(s.isPaused()) {
			s.resume(); s.update();
			output = "\"result \": \"Solution with id " + id + " resumed\"";
			if (request().accepts("text/html")) {
				redirect("/solutions/" + id + "/question");
			}
		} else {
			s.pause(); s.update();
			output = "\"result \": \"Solution with id " + id + " paused\"";
			if (request().accepts("text/html")) {
				/*
				 * TODO: change target
				 */
				redirect("/");
			}
		}

		if (request().accepts("application/json")) {
			 response().setContentType("application/json");
			 ok(output);
		} 
		return badRequest();
	}
	
	public Result pause(Long id) {
		response().setContentType("application/json");
		Solution s = Solution.find.byId(id);
		if(s == null) {
			return notFound("unknown solution");
		}
		if(s.isPaused())
			return badRequest("Solution was already paused.");
		s.pause();
		s.update();
		return ok("Solution paused");
	}
	
	public Result resume(Long id) {
		response().setContentType("application/json");
		Solution s = Solution.find.byId(id);
		if(s == null) {
			return notFound("unknown solution");
		}
		if(!s.isPaused())
			return badRequest("Solution is still running.");
		s.resume();
		s.update();
		return ok("Solution resumed");
	}
	
	public Result getScore(Long id) {
		// response().setContentType("application/json");
		response().setHeader("Cache-Control", "no-cache"); 
		response().setHeader("Cache-Control", "public"); 
		response().setHeader("Cache-Control", "max-age=0");
		Solution s = Solution.find.byId(id);
		if(s == null) {
			return notFound("unknown solution");
		} 
		if(s.isDone()) {
			//Se no quiz o user fizer mais de 80 pontos ele é habilitado como NERD
//			if(s.getPoints() >= 160){
//				Pontuacao pontuacao = new Pontuacao();
//				pontuacao.dataCadastro = new Date();
//				pontuacao.usuario = InformacoesUsuarioHelper.getUsuarioLogado();
//				pontuacao.pontuacaoObtida = InformacoesUsuarioHelper.PONTUACAO_HABILIDADE_NERD;
//				pontuacao.save();
//				
//				Usuario user = InformacoesUsuarioHelper.getUsuarioLogado();
//				Integer codigo = s.codigoHabilitacaoNerd;
//				CategoriaPergunta cat = CategoriaPergunta.find.where().eq("codigo", codigo).findUnique();
//				if(cat != null){
//					if(cat.codigo == InformacoesUsuarioHelper.CATEGORIA_FISICA){
//						user.habilidadeFisica = true;
//					} else if(cat.codigo == InformacoesUsuarioHelper.CATEGORIA_MATEMATICA){
//						user.habilidadeMatematica = true;
//					} else if(cat.codigo == InformacoesUsuarioHelper.CATEGORIA_QUIMICA){
//						user.habilidadeFisica = true;
//					}
//				}
//				user.nerdHabilitado = true;
//				user.update();
//			}
			
			Quiz q = s.getQuiz();
			int allQuestions = q.getTotalQuestions();
			Score score =  new Score(q.getDifficulty(), s.getPoints(), allQuestions, s.getAnsweredCorrectly(),
					s.getQuizAnswerTime(), s.getBestTime(), q.getName(), s.getQuizParticipant());
			return ok(views.html.Quiz.viewScore.render(score));
		}
		else
			return badRequest("You cannot watch the score, if the quiz is currently running");
		
	}
	
	/**
	 * 
	 * @param id
	 * @return 
	 */
	public Result show(Long id) {
		if(InformacoesUsuarioHelper.getUsuarioLogado().isAdministrador = false){
			response().setContentType("application/json");
			Solution s = Solution.find.byId(id);
			return ok(views.txt.Quiz.show.render(s));	
		} else{
			return newSolutionForm();
		}
	}
	
	/**
	 * 1 
	 * @return
	 */
	public Result newSolutionForm() {
		if (!InformacoesUsuarioHelper.isLogado()) {
			return redirect(routes.SessionsController.login());
		} else {
			Usuario user = InformacoesUsuarioHelper.getUsuarioLogado();
			Solution solucao  = Solution.find.where().eq("quiz_participant_id", user.id).findUnique();
			//InformacoesUsuarioHelper.verificaMudancaLevel();
			if(solucao != null){
				if(solucao.done == true){
					flash().put("error", "\"" + user.nome + "\", você já realizou o seu questionário!");
					return redirect(routes.HomeController.homeIndex());
				}
			}
			return ok(views.html.Quiz.newSolution.render());
		}
	}
	
//	public static List<CategoriaPergunta> carregaCategoriasEscolhidas(){
//		List<CategoriaPergunta> categoriasEscolhidasUser = new ArrayList<CategoriaPergunta>();
//		Usuario user = InformacoesUsuarioHelper.getUsuarioLogado();
//		if(user.habilidadeFisica == true){
//			CategoriaPergunta fisica = CategoriaPergunta.find.where().eq("id", InformacoesUsuarioHelper.CATEGORIA_FISICA).findUnique();
//			categoriasEscolhidasUser.add(fisica);
//		}
//		if(user.habilidadeMatematica == true){
//			CategoriaPergunta matematica = CategoriaPergunta.find.where().eq("id", InformacoesUsuarioHelper.CATEGORIA_MATEMATICA).findUnique();
//			categoriasEscolhidasUser.add(matematica);
//		}
//		if(user.habilidadeQuimica == true){
//			CategoriaPergunta quimica = CategoriaPergunta.find.where().eq("id", InformacoesUsuarioHelper.CATEGORIA_QUIMICA).findUnique();
//			categoriasEscolhidasUser.add(quimica);
//		}
//		return categoriasEscolhidasUser;
//	}
}
