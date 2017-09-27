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
public class Eixo extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	
	public String nome;
	
	@OneToMany
	public List<Questao> questoes;
	
	public static Finder<Long, Eixo> find = new Finder<Long,Eixo>(Eixo.class);
}
