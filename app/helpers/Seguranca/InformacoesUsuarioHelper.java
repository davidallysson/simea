package helpers.Seguranca;

import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

import models.Eixo;
import models.PontuacaoEvasao;
import models.Solution;
import models.Usuario;

/**
 * 
 * @author Alessandro
 *
 */
public class InformacoesUsuarioHelper extends Controller {
	
	public static String tituloSistema="SIMEA - Sistema de Monitoramento de Evasão de Alunos";
	
	public static Usuario getUsuarioLogado() {
		Long idUsuario = Long.parseLong(session("usuarioLogadoID"));
		return Usuario.find.byId(idUsuario);
	}

	public static Boolean isLogado() {
		return session().get("usuarioLogadoID") == null ? false : true;
	}
	
	public static int pontuacaoEvasometroTodos(){
		List<Usuario> alunos = Usuario.find.where().eq("isAdministrador", false).findList();
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
				pontuacao += pe.pontuacaoObtida;
			}
		}
		pontuacao = pontuacao/alunos.size();
		return pontuacao;
	}
	
	public static List<Integer> pontuacaoEvasometro(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = null;
		List<Integer> resultados = new ArrayList<Integer>();
		// = Usuario.find.where().eq("isAdministrador", false).findList();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}else if(turma){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}else if(curso){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}else if(diretoria){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		} else if(campus){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("campus_id", idCampus).findList();
		} else if(eixo){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).findList();
		}
		int pontuacao=0;
		int valorFeixaEtaria15a19=0;
		int valorFeixaEtaria20a24=0;
		int valorFeixaEtaria25a29=0;
		int valorFeixaEtaria30a34=0;
		int valorFeixaEtaria35a39=0;
		int valorFeixaEtaria40ouMais=0;
		
		int solteiro=0;
		int casado=0;
		int divorciado=0;
		int separado=0;
		int viuvo=0;
		
		int branca=0;
		int preta =0;
		int parda=0;
		int amarela=0;
		int indigina=0;
		int semDeclaracao=0;
		
		
		int ate1salario=0;
		int ate3salarios =0;
		int ate6salarios=0;
		int ate9salarios=0;
		int ate10ouMais=0;
		
		int masculino=0;
		int feminino=0;
		
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
				if(eixo){
					if(pe.eixo.id==idEixo){
						pontuacao += pe.pontuacaoObtida;
					}
				}else{
					pontuacao += pe.pontuacaoObtida;
				}
				
			}
			if(usuario.faixaEtaria==1){
				valorFeixaEtaria15a19 = valorFeixaEtaria15a19+1;
			}
			if(usuario.faixaEtaria==2){
				valorFeixaEtaria20a24 = valorFeixaEtaria20a24+1;
			}
			if(usuario.faixaEtaria==3){
				valorFeixaEtaria25a29 = valorFeixaEtaria25a29+1;
			}
			if(usuario.faixaEtaria==4){
				valorFeixaEtaria30a34 = valorFeixaEtaria30a34+1;
			}
			if(usuario.faixaEtaria==5){
				valorFeixaEtaria35a39 = valorFeixaEtaria35a39+1;
			}
			if(usuario.faixaEtaria==6){
				valorFeixaEtaria40ouMais = valorFeixaEtaria40ouMais+1;
			}
			
			if(usuario.estadoCivil==1){
				solteiro = solteiro+1;
			}
			if(usuario.estadoCivil==2){
				casado = casado+1;
			}
			if(usuario.estadoCivil==3){
				divorciado = divorciado+1;
			}
			if(usuario.estadoCivil==4){
				separado = separado+1;
			}
			if(usuario.estadoCivil==5){
				viuvo = viuvo+1;
			}
			
			
			if(usuario.raca==1){
				branca = branca+1;
			}
			if(usuario.raca==2){
				preta = preta+1;
			}
			if(usuario.raca==3){
				parda = parda+1;
			}
			if(usuario.raca==4){
				amarela = amarela+1;
			}
			if(usuario.raca==5){
				indigina = indigina+1;
			}
			if(usuario.raca==6){
				semDeclaracao = semDeclaracao+1;
			}
			
			
			if(usuario.renda==1){
				ate1salario = ate1salario+1;
			}
			if(usuario.renda==2){
				ate3salarios = ate3salarios+1;
			}
			if(usuario.renda==3){
				ate6salarios = ate6salarios+1;
			}
			if(usuario.renda==4){
				ate9salarios = ate9salarios+1;
			}
			if(usuario.renda==5){
				ate10ouMais = ate10ouMais+1;
			}
			
			if(usuario.masculino==true){
				masculino = masculino+1;
			}
			if(usuario.feminino==true){
				feminino = feminino+1;
			}
			
		}
		if(alunos.size()>0){
			pontuacao = pontuacao/alunos.size();
		}
		
		resultados.add(pontuacao); //0
		
		//Faixa Etaria
		resultados.add(valorFeixaEtaria15a19);//1
		resultados.add(valorFeixaEtaria20a24);//2
		resultados.add(valorFeixaEtaria25a29);//3
		resultados.add(valorFeixaEtaria30a34);//4
		resultados.add(valorFeixaEtaria35a39);//5
		resultados.add(valorFeixaEtaria40ouMais);//6
		
		//Estado civil
		resultados.add(solteiro);//7
		resultados.add(casado);//8
		resultados.add(divorciado);//9
		resultados.add(separado);//10
		resultados.add(viuvo);//11
		
		//Raça/cor
		resultados.add(branca);//12
		resultados.add(preta);//13
		resultados.add(parda);//14
		resultados.add(amarela);//15
		resultados.add(indigina);//16
		resultados.add(semDeclaracao);//17
		
		//renda familiar mensal
		resultados.add(ate1salario);//18
		resultados.add(ate3salarios);//19
		resultados.add(ate6salarios);//20
		resultados.add(ate9salarios);//21
		resultados.add(ate10ouMais);//22
		
		//Genero
		resultados.add(masculino);//23
		resultados.add(feminino);//24
		
		return resultados;
	}
	
	public static int pontuacaoEvasometroEixoIndividual(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = new ArrayList<Usuario>();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
					if(pe.eixo.id==1L){
						pontuacao += pe.pontuacaoObtida;
					}
			}
		}
		return pontuacao;
	}
	
	public static int pontuacaoEvasometroEixoFamiliar(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = new ArrayList<Usuario>();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
					if(pe.eixo.id==2L){
						pontuacao += pe.pontuacaoObtida;
					}
			}
		}
		return pontuacao;
	}
	
	public static int pontuacaoEvasometroIntraEscolar(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = new ArrayList<Usuario>();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
					if(pe.eixo.id==3L){
						pontuacao += pe.pontuacaoObtida;
					}
			}
		}
		return pontuacao;
	}
	
	public static int pontuacaoEvasometroCarreiraProfissional(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = new ArrayList<Usuario>();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
					if(pe.eixo.id==4L){
						pontuacao += pe.pontuacaoObtida;
					}
			}
		}
		return pontuacao;
	}
	
	public static int pontuacaoEvasometroAreaFormacao(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = new ArrayList<Usuario>();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
					if(pe.eixo.id==5L){
						pontuacao += pe.pontuacaoObtida;
					}
			}
		}
		return pontuacao;
	}
	
	
	public static int pontuacaoEvasometroInstitucional(Long idEixo, Long idAluno, Long idTurma, Long idCurso, Long idDiretoria, Long idCampus, boolean turma, boolean curso, boolean diretoria, boolean campus, boolean aluno, boolean eixoTodos, boolean eixo){
		List<Usuario> alunos = new ArrayList<Usuario>();
		if(aluno){
			alunos = Usuario.find.where().eq("isAdministrador", false).eq("id", idAluno).eq("turma_id", idTurma).eq("curso_id", idCurso).eq("diretoria_id", idDiretoria).eq("campus_id", idCampus).findList();
		}
		int pontuacao=0;
		for (Usuario usuario : alunos) {
			for (PontuacaoEvasao pe : usuario.pontuacaoEvasao) {
					if(pe.eixo.id==6L){
						pontuacao += pe.pontuacaoObtida;
					}
			}
		}
		return pontuacao;
	}
	
}
