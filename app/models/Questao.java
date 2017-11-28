package models;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Alessandro
 *
 */
@Entity
public class Questao extends Model{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	public String pergunta;

	// public boolean status;

	@Transient
	public String alternativa1;

	@Transient
	public String alternativa2;

	@Transient
	public String alternativa3;

	@Transient
	public String alternativa4;

	@ManyToOne
	@JsonBackReference
	public Usuario usuario;

	@ManyToOne
	@JsonBackReference
	public Eixo eixo;

	@ManyToOne
	@JsonBackReference
	public Quiz quiz;

	@Basic(fetch=FetchType.EAGER)
	@ManyToMany(mappedBy="questions")
	public List<Solution> solutions;

	public Questao() {
	}

	public Questao(String question, Quiz quiz, Eixo eixo, Usuario usuario) {
		this.pergunta = question;
		this.quiz = quiz;
		this.eixo = eixo;
		this.usuario = usuario;
	}

	public String getPergunta() {
		return this.pergunta;
	}

	public static Finder<Long, Questao> find = new Finder<Long,Questao>(Questao.class);

	public List<Answer> getAnswers() {
	    return Answer.find.where().eq("question", this).findList();
	}

}
