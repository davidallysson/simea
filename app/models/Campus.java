package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

/**
 * 
 * @author Alessandro
 *
 */
@Entity
public class Campus extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	public String nome;
	
	@OneToMany
	public List<Curso> cursos;
	
	@OneToMany
	public List<Diretoria> diretorias;
	
	@OneToMany
	public List<Turma> turmas;
	
	public static Finder<Long, Campus> find = new Finder<Long,Campus>(Campus.class);
}
