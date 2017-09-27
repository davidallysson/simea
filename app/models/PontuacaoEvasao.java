package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.avaje.ebean.Model;
/**
 * 
 * @author alessandroanjos
 *
 */
@Entity
public class PontuacaoEvasao extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	public int pontuacaoObtida;
	
	@Temporal(TemporalType.DATE)
	public Date	dataCadastro;
	
	@ManyToOne
	@JsonBackReference
	public Usuario usuario;
	
	@ManyToOne
	public Answer resposta;
	
	@ManyToOne
	public Questao pergunta;
	
	@ManyToOne
	public Eixo eixo;
	
	@ManyToOne
	public Quiz quiz;
	
	public static Finder<Long, PontuacaoEvasao> find = new Finder<Long,PontuacaoEvasao>(PontuacaoEvasao.class);
}
