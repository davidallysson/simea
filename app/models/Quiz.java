package models;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import play.data.format.*;
import play.data.validation.*;
/**
 * 	A alternativa a) terá 1 ponto, a b) terá 2 pontos, a c) 3 pontos e a d) 4 pontos.
	A primeira opção marcada terá peso 4, a segunda opção marcada terá peso 3.
	O sistema deverá multiplicar o peso pelos pontos no momento de enviar os dados.
	Caso o usuário marque uma alternativa e feche, o sistema deverá gravar a questão como pendente para que na próxima seção ele responda.
	Caso nenhuma alternativa tenha sido marcada o usuário poderá pular a questão.
	O sistema deverá sempre selecionar uma questão aleatoriamente, desde que essa questão não tenha sido respondida.
 * @author Alessandro
 *
 */
@Entity
public class Quiz extends Model {

	@Id
	@Constraints.Min(10)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	@Constraints.Required
	public String name;
	
	public boolean active = false;
	
	// zur Messung der Beliebtheit eines Quiz
	public int popCounter;
	
	@Constraints.Max(3)
	public int difficulty;
	
	@Formats.DateTime(pattern="dd-MM-yyyy")
	public Date start = new Date();
	
	@Formats.DateTime(pattern="dd-MM-yyyy")
	public Date fim = new Date();
	
	@ManyToOne
	public Usuario creator;
	
	/*
	 * Weil eine eigene Entität Wissenskategorie allein aus einem String
	 * bestünde, haben wir darauf verzichtet und verlagern die Kategorie
	 * direkt ins Quiz.
	 */
	@Constraints.Required
	public String category;
	
	public static Finder<Long, Quiz> find = new Finder<Long,Quiz>(Quiz.class);

	public Quiz(String name, int difficulty,Date start, Date end, Usuario creator, String category) {
		super();
		this.name = name;
		this.popCounter = 0;
		this.difficulty = difficulty;
		this.start = start;
		this.fim = end;
		if(hasStarted())
			this.active = true;
		else
			this.active = false;
		this.creator = creator;
		this.category = category;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean val) {
		active = val;
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return fim;
	}
	public boolean hasStarted() {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		return (start.compareTo(today) <= 0);
	}
	
	public Questao findQuestion(Solution solution, Long idQuestion ) {
		//List<Question> s = Question.find.orderBy().asc("id").where().eq("solutions.id", solution.id).findList(); 
	    return Questao.find.where().eq("solutions.id", solution.id).eq("id", idQuestion).findUnique();
	}
	
	public List<Questao> findQuestions(Solution solution) {
		//List<Question> s = Question.find.orderBy().asc("id").where().eq("solutions.id", solution.id).findList(); 
	    return Questao.find.orderBy().asc("id").where().eq("solutions.id", solution.id).findList();
	}
	
	
	public int getTotalQuestions() {
		List<Questao> s = Questao.find.orderBy().asc("pergunta").where().eq("quiz", this).findList();
		return s.size();
	}
	
	public int getTotalRestante(Solution solution) {
		List<Questao> questions = Questao.find.orderBy().asc("id").where().eq("solutions.id", solution.id).findList();
		return questions.size();
	}
	
	public static void removeQuestion(Long solutionId, Long questionId) {
	    List<Solution> solutions = Solution.find.setId(solutionId).fetch("questions").findList();
	    
	    for (Solution solution : solutions) {
	    	if (solution.questions.contains(Questao.find.ref(questionId))){
	    		solution.questions.remove(Questao.find.ref(questionId));
		    	//solution.saveManyToManyAssociations("questions");
		    	Ebean.saveManyToManyAssociations(solution, "questions");
	    	}
		}
	}
	
	
	/*
	public List<Question> getQuestions3(Solution solution, int idQuestion) {
		Solution s = Solution.find.byId(solution.id);
		
		Question aquestio = Question.find.where().eq("solutions.id", solution.id).eq("question_id", idQuestion).findUnique();
		
		for (Question questao : s.questions) {
			if(questao.id == idQuestion)
			{
				s.questions.remove(questao);
			}
		}
		return Question.find.orderBy().asc("question").where().eq("quiz", this).findList();
	}
	*/


	public List<Questao> getQuestionsAleatorio(Solution solution) {
		List<Questao> questions2 = new ArrayList<Questao>();
		Random gerador = new Random();
		Calendar lCDateTime = Calendar.getInstance();
		int aletorio = (int)(lCDateTime.getTimeInMillis() % (questions2.size() + 1));
		     
		List<Questao> questions = Questao.find.orderBy().asc("id").where().eq("solutions.id", solution.id).findList();
		while (questions2.size() < questions.size()) {
			Questao que = questions.get(gerador.nextInt(questions.size()));
	    	if(!questions2.contains(que)){
	    		questions2.add(que);
	    	}
		}
		return  questions2;
	}
	public List<Questao> getQuestions(Long idSolution) {
		Solution s = Solution.find.byId(idSolution);
	    
	    if(s.firstQuestionShow == true){
	    	s.questions =  new HashSet<Questao>();;
	    	List<Questao> questions = Questao.find.orderBy().asc("question").where().eq("quiz", this).findList();
	    	Collections.shuffle(questions, new Random());
	    	s.questions.addAll(questions);
	    	//s.saveManyToManyAssociations("questions");
	    	Ebean.saveManyToManyAssociations(s.questions, "questions");
	    	s.firstQuestionShow = false;
	    	s.update();
	    	return questions;
	    } else {
	    	
	    	
	    	List<Questao> questions2 = new ArrayList<Questao>();
	    	for (Questao subset : s.questions) {
	    		questions2.add(subset);
	    	} 
	    	return questions2;
	    }
	    
		//imprime sequência de 10 números inteiros aleatórios entre 0 e 25
	    
	    /*
	    for (int i = 0; i < 2; i++) {
	    	Question q = questions.get(gerador.nextInt(questions.size()));
	    	if(!questionsFinais.contains(q)){
	    		questionsFinais.add(q);
	    	} else {
	    		i--;
	    	}
		 }
	    */
	    /*
	   
	    
	    while (questionsFinais.size() < 5) {
	    	System.out.println("Soreteado: "+ gerador.nextInt(5));
	    	Question q = questions.get(gerador.nextInt(5));
	    	if(!questionsFinais.contains(q)){
	    		questionsFinais.add(q);
	    	}
		}
	   
	  //Ordenando
	    Collections.sort(questionsFinais, new Comparator<Question>() {
	            @Override
	            public int compare(Question  pessoa1, Question  pessoa2)
	            {
	                return  pessoa1.id.compareTo(pessoa2.id);
	            }
	        });
	    
	    return questionsFinais;
	    */
	}
	
	public static List<Quiz> getQuizByCategoryAndDifficulty(String category, int difficulty) {
		return Quiz.find.where().eq("category", category).eq("difficulty", difficulty).findList();
	}
	
	public Usuario getCreator() {
		return creator;
	}

	public int getDifficulty() {
		return difficulty;
	}
	
}
