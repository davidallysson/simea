package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.Constraints;

/**
 * Answer são as respostas( opcoes do questionário), ou seja as altenativas disponiveis para ser marcada.
 * 
 * @author Alessandro
 *
 */
@Entity
public class Answer extends Model {
	
	@Id
	@Constraints.Min(10)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	@Constraints.Required
	public String answer;
	
	@Constraints.Required
	public boolean valid;
	
	@Constraints.Required
	public int ordem;
	
	@Constraints.Required
	public int pontos;
	
	@ManyToOne
	@JsonBackReference
	public Questao question;
	
	public static Finder<Long, Answer> find = new Finder<Long,Answer>(Answer.class);
	
	public Answer(String answer, boolean valid, Questao question, int ordem, int pontos) {
		this.answer = answer;
		this.valid = valid;
		this.question = question;
		this.ordem = ordem;
		this.pontos = pontos;
	}
	
	public Long getId() {
		return id;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public Questao getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}
	
}
