package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
/**
 * 
 * @author Alessandro
 *
 */
@Entity
public class Curso extends Model {

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
	
	@OneToMany
	public List<Turma> turmas;
	
	public static Finder<Long, Curso> find = new Finder<Long,Curso>(Curso.class);
}
