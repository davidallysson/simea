package models;

import helpers.Seguranca.InformacoesUsuarioHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;

/**
 * Solution são as respostas informadas pelo aluno.
 * @author Alessandro
 *
 */
@SuppressWarnings("serial")
@Entity
public class Solution extends Model {
	
	@Id
	@Constraints.Min(10)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	// wird das Quiz zum ersten mal belegt?
	public boolean firstTime;
	
	public boolean firstQuestionShow;
	// hat der Spieler das Quiz abgeschlossen?
	public boolean done;
	// die Zeit, die der Spieler (bisher) für das Quiz benötigt hat
	public Long quizAnswerTime;
	
	public Long bestTime;
	// die erzielten Punkte
	public int points;
	// falls das Quiz unterbrochen wird => Long (ID der Frage im Quiz)
	public int currentQuestion;
	
	public Date start;
	
	public Integer codigoHabilitacaoNerd;
	
	@ManyToOne
	public Usuario quizParticipant;
	
	@ManyToOne
	public Quiz quiz;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Set<Questao> questions;
	
	public boolean paused;
	
	public int answeredCorreclty;
	
	public static Finder<Long, Solution> find = new Finder<Long,Solution>(Solution.class);
	
	public static Solution find(Usuario u, Quiz q) {
		return find.where().eq("quiz", q).eq("quizParticipant", u).findUnique();
	}

	public Solution(Usuario u, Quiz q) {
		quizParticipant = u;
		quiz = q;
		quiz.popCounter++;
		bestTime = 0L;
		codigoHabilitacaoNerd = 0;
		points = 0;
		firstTime = true;
		firstQuestionShow = true;
		startSolution();
	}
	
	public Quiz getQuiz() {
		return quiz;
	}
	
	public Usuario getQuizParticipant() {
		return quizParticipant;
	}
	
	public int getPoints() {
		return points;
	}
	
	public Long getQuizAnswerTime() {
		return quizAnswerTime;
	}
	
	public void incCurrentQuestion() {
		currentQuestion++;
	}
	
	public void decCurrentQuestion() {
		currentQuestion--;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void setFirstTime(boolean val) {
		firstTime = val;
	}
	
	public void setDone(boolean val) {
		done = val;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	
	public int getCurrentQuestion() {
		return currentQuestion;
	}
	
	public Long getBestTime() {
		return bestTime;
	}
	
	public int getAnsweredCorrectly() {
		return answeredCorreclty;
	}
	public void startSolution() {
		resume();
		quizAnswerTime = 0L;
		done = false;
		currentQuestion = 0;
		answeredCorreclty = 0;
	}
	
	public void resume() {
		paused = false;
		start = new Date();
	}
	public void pause() {
		paused = true;
		Date date = new Date();
		quizAnswerTime += date.getTime() - start.getTime();
	}
	
	public void finish() {
		done = true;
		firstTime = false;
		Date date = new Date();
		quizAnswerTime += date.getTime() - start.getTime();
		update();
		System.out.println("\n\n\n BestTime: " + bestTime + " answerTime: " + quizAnswerTime);
		if(bestTime == 0L)
			bestTime = quizAnswerTime;
		else if(quizAnswerTime < bestTime)
			bestTime = quizAnswerTime;
		System.out.println("\n\n\n BestTime: " + bestTime + " answerTime: " + quizAnswerTime);
	}
	
	public Questao getQuestion(Long idSolutions) {
		Solution sol = Solution.find.byId(idSolutions);
		if(currentQuestion < quiz.getTotalRestante(sol)){
			if(!sol.firstQuestionShow){
				return quiz.findQuestions(sol).get(currentQuestion);
			}
		} else if(currentQuestion >= quiz.getTotalRestante(sol)){
			return quiz.getQuestionsAleatorio(sol).get(currentQuestion);
		 } else {
			 currentQuestion--;
				return quiz.getQuestionsAleatorio(sol).get(currentQuestion);
		 }
		return null;
	}
	
	public Questao getQuestion(Long idQuestion, Solution solution) {
		return quiz.findQuestion(solution, idQuestion);
	}
	
	public Questao getQuestionAletorio(Long idSolutions) { 
		Solution sol = Solution.find.byId(idSolutions);
		int valor = quiz.getTotalRestante(sol);
		valor--;
		if(valor < 0){
			return null;
		}
		return quiz.getQuestionsAleatorio(sol).get(valor); 
	}
	
	public int evaluateAnswer(String answer, Long idSolution, Long idQuestion) {
		int result = -1; //Antwort nicht gueltig
		Solution solution = Solution.find.byId(idSolution);
		Questao q = solution.getQuestion(idQuestion, solution);
		if(q != null) {
			boolean valid;
			Answer copy = null;
			if(q.eixo.id == 1 || q.eixo.id  == 2 || q.eixo.id == 3 || q.eixo.id == 4 || q.eixo.id  == 5 || q.eixo.id == 6) {
				Answer a = Answer.find.byId(Long.parseLong(answer));
				copy = a;
				Eixo eixoNovo = copy.question.eixo;
				if(a == null)
					return result;
				if(a.getQuestion().id != q.id)
					return result;
				valid = a.isValid();
			} else
				valid = q.getAnswers().get(0).getAnswer().equals(answer);
			if(valid) {
				if(firstTime) {
					//points += quiz.difficulty * 10;
					//Estou alterando para aplicar a pontuacao do aluno em cima da resposta pra essa pergunta.
					//points += copy.pontos;
					int pontosResposta=0;
					if(answeredCorreclty==0 ||answeredCorreclty>1){
						pontosResposta = (copy.pontos * 4);
						points =points+pontosResposta;
					}else if(answeredCorreclty==1){
						pontosResposta = (copy.pontos * 3);
						points =points+pontosResposta;
					}
					
					PontuacaoEvasao pontuacao = new PontuacaoEvasao();
					pontuacao.dataCadastro = new Date();
					pontuacao.pergunta = q;
					pontuacao.resposta = copy;
					pontuacao.eixo = q.eixo;
					pontuacao.usuario = InformacoesUsuarioHelper.getUsuarioLogado();
					pontuacao.pontuacaoObtida = pontosResposta;
					pontuacao.save();
					
					//TODO: Fazer esquema do calculo
					/*
					 * Obter a opção marcada pelo aluno no campo: ordem
					 * --> A primeira opção marcada terá peso 4, a segunda opção marcada terá peso 3.
//					 * Ex: 1° opção marcada = ordem * 4
					 *     2° opção marcada = ordem * 3 
					 * 
					 * -> Quando marcar as duas questões de mesmo eixo ou pular é que  
					 */
					
					result = 1; //Antwort richtig und mit Punktzahl bewertet
				} else
					result = 2; //Antwort richtig ohne Punkte
			//	q.incAnsweredCorrectly();
				answeredCorreclty++;
			} else {
				result = 0; //Antwort ist falsch
			//	q.incAnsweredWrongly();
			}
			//answeredCorreclty++;
		}
		q.update();
		return result;
	}
	
		
}
	
	

