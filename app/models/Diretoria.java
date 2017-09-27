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
public class Diretoria extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	public String nome;
	
	@ManyToOne
	@JsonBackReference
	public Campus campus;
	
	@OneToMany
	public List<Curso> cursos;
	
	@OneToMany
	public List<Turma> turmas;
	
	public static Finder<Long, Diretoria> find = new Finder<Long,Diretoria>(Diretoria.class);
	
}
