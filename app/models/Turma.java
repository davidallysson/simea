package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 
 * @author Alessandro
 *
 */
@Entity
public class Turma extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	public String nome;
	
	@ManyToOne
	@JsonBackReference
	public Campus campus;
	
	@ManyToOne
	@JsonBackReference
	public Diretoria diretoria;
	
	@ManyToOne
	@JsonBackReference
	public Curso curso;
	
	public static Finder<Long, Turma> find = new Finder<Long,Turma>(Turma.class);
	
}
