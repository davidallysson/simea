package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

/**
 * Classe que representa um resultado que apresenta nos graficos 
 * @author Alessandro
 *
 */
@Entity
public class Resultado extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
		
	public int valor;
	
	public String opcoesEscolhidas;
	
	public int valorEixoIndividual;
	
	public int valorEixoFamiliar;
	
	public int valorEixoIntraEscolar;
	
	public int valorEixoCarreiraProfissional;
	
	public int valorEixoAreaFormacao;
	
	public int valorEixoInstitucional;
	
	public int valorFaixaEtaria15a19=0;
	public int valorFaixaEtaria20a24=0;
	public int valorFaixaEtaria25a29=0;
	public int valorFaixaEtaria30a34=0;
	public int valorFaixaEtaria35a39=0;
	public int valorFaixaEtaria40ouMais=0;
	
	public int solteiro=0;
	public int casado=0;
	public int divorciado=0;
	public int separado=0;
	public int viuvo=0;
	
	public int branca=0;
	public int preta =0;
	public int parda=0;
	public int amarela=0;
	public int indigina=0;
	public int semDeclaracao=0;
	
	public int ate1salario=0;
	public int ate3salarios =0;
	public int ate6salarios=0;
	public int ate9salarios=0;
	public int ate10ouMais=0;
	
	public int masculino=0;
	public int feminino=0;
	
	
	public int valorCompara;
	
	public String opcoesEscolhidasCompara;
	
	public int valorEixoIndividualCompara;
	
	public int valorEixoFamiliarCompara;
	
	public int valorEixoIntraEscolarCompara;
	
	public int valorEixoCarreiraProfissionalCompara;
	
	public int valorEixoAreaFormacaoCompara;
	
	public int valorEixoInstitucionalCompara;
	
	public int valorFaixaEtaria15a19Compara=0;
	public int valorFaixaEtaria20a24Compara=0;
	public int valorFaixaEtaria25a29Compara=0;
	public int valorFaixaEtaria30a34Compara=0;
	public int valorFaixaEtaria35a39Compara=0;
	public int valorFaixaEtaria40ouMaisCompara=0;
	
	public int solteiroCompara=0;
	public int casadoCompara=0;
	public int divorciadoCompara=0;
	public int separadoCompara=0;
	public int viuvoCompara=0;
	
	public int brancaCompara=0;
	public int pretaCompara =0;
	public int pardaCompara=0;
	public int amarelaCompara=0;
	public int indiginaCompara=0;
	public int semDeclaracaoCompara=0;
	
	public int ate1salarioCompara=0;
	public int ate3salariosCompara =0;
	public int ate6salariosCompara=0;
	public int ate9salariosCompara=0;
	public int ate10ouMaisCompara=0;
	
	public int masculinoCompara=0;
	public int femininoCompara=0;
	
	public static Finder<Long, Resultado> find = new Finder<Long,Resultado>(Resultado.class);
}
