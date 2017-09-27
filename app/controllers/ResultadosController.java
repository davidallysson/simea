package controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import forms.ResultadosForm;
import helpers.Seguranca.InformacoesUsuarioHelper;
import models.Campus;
import models.Curso;
import models.Diretoria;
import models.Resultado;
import models.Turma;
import models.Usuario;
import play.api.libs.json.Json;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.CSV;
import util.CSVUtils;


@Singleton
public class ResultadosController extends Controller {

	private FormFactory formFactory;

    @Inject
    public ResultadosController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }
    
    public Result consultar() {
    	ResultadosForm resultado = new ResultadosForm();
    	List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		List<Usuario> alunos = Usuario.find.where().eq("is_administrador",false).eq("is_supervisor",false).findList();
        return ok(views.html.Resultados.consultar.render(resultado, alunos, turmas, cursos, diretorias, campus));
	}
    
    
    public Result consultarDados(){
    	List<Integer> resultados = new ArrayList<Integer>();
    	List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		List<Usuario> alunos = Usuario.find.where().eq("is_administrador",false).eq("is_supervisor",false).findList();
    	boolean processouPeloMenos1Resultado=false;
		Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
		
		String id = resultadoForm.data().get("idStatus");
		String idTurmaString = resultadoForm.data().get("idTurma");
		String idCursoString = resultadoForm.data().get("idCurso");
		String idDiretoriaString = resultadoForm.data().get("idDiretoria");
		String idCampusString = resultadoForm.data().get("idCampus");
		String idAlunoString = resultadoForm.data().get("idAluno");
		
		String idEixoString = resultadoForm.data().get("idEixo");
		boolean eixoTodos=false;
		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
			eixoTodos=true;
		}

		StringBuilder sb = new StringBuilder();
        int resultadoEvasometro = 0;
        int resultadoFaixaEtaria = 0;
        if((idAlunoString!=null)&&!(idEixoString.trim().length()>10)&&(!idEixoString.equalsIgnoreCase("0"))&&!eixoTodos){
        	Long idEixo = Long.valueOf(resultadoForm.data().get("idEixo"));
        	Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
        	resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(idEixo, idAluno, null, null, null, null, false, false, false, false,false, eixoTodos, true);
        	resultadoEvasometro = resultados.get(0);
        	processouPeloMenos1Resultado=true;
        	//resultadoFaixaEtaria
        }else if(idAlunoString!=null&&!(idAlunoString.trim().length()>10)&& (!idAlunoString.equalsIgnoreCase("0"))){
        	Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
        	Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
        	Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
        	Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
            Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
            resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
            resultadoEvasometro = resultados.get(0);
            processouPeloMenos1Resultado=true;
        }else if(idTurmaString!=null&&!(idTurmaString.trim().length()>10)&& (!idTurmaString.equalsIgnoreCase("0"))){
        	Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
        	Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
        	Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
            Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
            //User user = User.findById(id);
            String nome=Turma.find.byId(idTurma).nome;
            sb.append(nome);
            resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, idTurma, idDiretoria, idCurso, idCampus, true, false, false, false, false,eixoTodos,false);
            resultadoEvasometro = resultados.get(0);
            processouPeloMenos1Resultado=true;
        }else if((idCursoString!=null) &&!(idCursoString.trim().length()>10)&& (!idCursoString.equalsIgnoreCase("0"))){
        	Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
        	Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
            Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
            String nome=Curso.find.byId(idCurso).nome;
            sb.append(" - "+nome);
            resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, idDiretoria, idCurso, idCampus, false, true, false, false,false, eixoTodos,false);
            resultadoEvasometro = resultados.get(0);
            processouPeloMenos1Resultado=true;
        } else if((idDiretoriaString!=null) &&!(idDiretoriaString.trim().length()>10)&&(!idDiretoriaString.equalsIgnoreCase("0"))){
        	Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
        	Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
        	String nome=Diretoria.find.byId(idDiretoria).nome;
            sb.append(" - "+nome);
            resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, idDiretoria, idCampus, false, false, true, false,false,eixoTodos,false);
            resultadoEvasometro = resultados.get(0);
            processouPeloMenos1Resultado=true;
        } else if(idCampusString!=null&& !(idCampusString.trim().length()>10)&&(!idCampusString.equalsIgnoreCase("0"))){
        	Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
        	String nome=Campus.find.byId(idCampus).nome;
            sb.append(" - "+nome);
            resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, null, idCampus, false, false, false, true,false,eixoTodos,false);
            resultadoEvasometro = resultados.get(0);
            processouPeloMenos1Resultado=true;
        }
        
        ResultadosForm resultForm = new ResultadosForm();
        resultForm.valor = resultadoEvasometro; 
        if(processouPeloMenos1Resultado){
        	resultForm.valorFaixaEtaria15a19 = resultadoEvasometro = resultados.get(1);
        	resultForm.valorFaixaEtaria20a24 = resultadoEvasometro = resultados.get(2);
        	resultForm.valorFaixaEtaria25a29 = resultadoEvasometro = resultados.get(3);
        	resultForm.valorFaixaEtaria30a34 = resultadoEvasometro = resultados.get(4);
        	resultForm.valorFaixaEtaria35a39 = resultadoEvasometro = resultados.get(5);
        	resultForm.valorFaixaEtaria40ouMais = resultadoEvasometro = resultados.get(6);
        	
        	resultForm.solteiro = resultadoEvasometro = resultados.get(7);
        	resultForm.casado = resultadoEvasometro = resultados.get(8);
        	resultForm.divorciado = resultadoEvasometro = resultados.get(9);
        	resultForm.separado = resultadoEvasometro = resultados.get(10);
        	resultForm.viuvo = resultadoEvasometro = resultados.get(11);
        	
        	resultForm.branca = resultadoEvasometro = resultados.get(12);
        	resultForm.preta = resultadoEvasometro = resultados.get(13);
        	resultForm.parda = resultadoEvasometro = resultados.get(14);
        	resultForm.amarela = resultadoEvasometro = resultados.get(15);
        	resultForm.indigina = resultadoEvasometro = resultados.get(16);
        	resultForm.semDeclaracao = resultadoEvasometro = resultados.get(17);
        	
        	resultForm.ate1salario = resultadoEvasometro = resultados.get(18);
        	resultForm.ate3salarios = resultadoEvasometro = resultados.get(19);
        	resultForm.ate6salarios = resultadoEvasometro = resultados.get(20);
        	resultForm.ate9salarios = resultadoEvasometro = resultados.get(21);
        	resultForm.ate10ouMais = resultadoEvasometro = resultados.get(22);
        	
        	resultForm.masculino = resultadoEvasometro = resultados.get(23);
        	resultForm.feminino = resultadoEvasometro = resultados.get(24);
        	
        }
        resultForm.opcoesEscolhidas = sb.toString();
        
        if(eixoTodos){
        	Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
        	Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
        	Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
        	Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
            Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
        	resultForm.valorEixoIndividual = InformacoesUsuarioHelper.pontuacaoEvasometroEixoIndividual(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
        	resultForm.valorEixoFamiliar = InformacoesUsuarioHelper.pontuacaoEvasometroEixoFamiliar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
        	resultForm.valorEixoIntraEscolar = InformacoesUsuarioHelper.pontuacaoEvasometroIntraEscolar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
        	resultForm.valorEixoCarreiraProfissional = InformacoesUsuarioHelper.pontuacaoEvasometroCarreiraProfissional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
        	resultForm.valorEixoAreaFormacao = InformacoesUsuarioHelper.pontuacaoEvasometroAreaFormacao(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
        	resultForm.valorEixoInstitucional = InformacoesUsuarioHelper.pontuacaoEvasometroInstitucional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
        }
        return badRequest(views.html.Resultados.consultar.render(resultForm, alunos, turmas, cursos, diretorias, campus));
       // return ok(views.html.Resultados.comparar.render(resultForm, resultadoComparaForm, alunos, turmas, cursos, diretorias, campus));
    }
    
    
    /**
     * Compara dois resultados de relatorios
     * @return
     */
    public Result comparar() {
    	ResultadosForm resultado = new ResultadosForm();
    	List<Curso> cursos = Curso.find.findList();
		List<Campus> campus = Campus.find.findList();
		List<Diretoria> diretorias = Diretoria.find.findList();
		List<Turma> turmas = Turma.find.findList();
		List<Usuario> alunos = Usuario.find.where().eq("is_administrador",false).eq("is_supervisor",false).findList();
		return ok(views.html.Resultados.comparar.render(resultado, alunos, turmas, cursos, diretorias, campus, alunos, turmas, cursos, diretorias, campus));
	}
    
    public Result testeOk(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	System.out.println("Teste ok"+play.libs.Json.toJson(10));
    	return ok(play.libs.Json.toJson(10));
    }
    
    public Result consultarEvasometro1(){
    	List<Integer> resultados = new ArrayList<Integer>();
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	int resultadoEvasometro = processarResultadoParametro(resultadoForm);
    	System.out.println("consultarEvasometro1 - "+play.libs.Json.toJson(resultadoEvasometro));
    	return ok(play.libs.Json.toJson(resultadoEvasometro));
    }
    
    public Result consultarEvasometro2(){
    	JsonArray array = new JsonArray();
    	JsonObject obj = new JsonObject();
        JsonArray list = new JsonArray();
        list.add("msg 1");
        list.add("msg 2");
        list.add("msg 3");
        obj.add("valores", list);
        System.out.println(obj);
    	List<Integer> resultados = new ArrayList<Integer>();
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	int resultadoEvasometro = processarResultadoParametro2(resultadoForm);
    	System.out.println("consultarEvasometro2 - "+play.libs.Json.toJson(resultadoEvasometro));
    	return ok(play.libs.Json.toJson(resultadoEvasometro));
    }
    
    
    public Result consultarEixo1(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadoEixo(resultadoForm);
        List<Integer> lista= new ArrayList<Integer>();
        lista.add(resultado.valorEixoIndividual);
        lista.add(resultado.valorEixoFamiliar);
        lista.add(resultado.valorEixoIntraEscolar);
        lista.add(resultado.valorEixoCarreiraProfissional);
        lista.add(resultado.valorEixoAreaFormacao);
        lista.add(resultado.valorEixoInstitucional);
    	System.out.println("consultarEixo1 - "+play.libs.Json.toJson(lista));
    	return ok(play.libs.Json.toJson(lista));
    }
    
    public Result consultarEixo2(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadoEixo2(resultadoForm);
        List<Integer> lista= new ArrayList<Integer>();
        lista.add(resultado.valorEixoIndividualCompara);
        lista.add(resultado.valorEixoFamiliarCompara);
        lista.add(resultado.valorEixoIntraEscolarCompara);
        lista.add(resultado.valorEixoCarreiraProfissionalCompara);
        lista.add(resultado.valorEixoAreaFormacaoCompara);
        lista.add(resultado.valorEixoInstitucionalCompara);
    	System.out.println("consultarEixo2 - "+play.libs.Json.toJson(lista));
    	return ok(play.libs.Json.toJson(lista));
    }
    
    public Result consultarSexo1(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,false);
    	System.out.println("consultarSexo1 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarSexo2(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,true);
    	System.out.println("consultarSexo2 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarFaixaEtaria1(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,false);
    	System.out.println("consultarFaixaEtaria1 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarFaixaEtaria2(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,true);
    	System.out.println("consultarFaixaEtaria2 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarEstadoCivil1(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,false);
    	System.out.println("consultarEstadoCivil1 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarEstadoCivil2(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,true);
    	System.out.println("consultarEstadoCivil2 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarRaca1(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,false);
    	System.out.println("consultarRaca1 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarRaca2(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,true);
    	System.out.println("consultarRaca2 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarRenda1(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,false);
    	System.out.println("consultarRenda1 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    public Result consultarRenda2(){
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,true);
    	System.out.println("consultarRenda1 - "+play.libs.Json.toJson(resultado));
    	return ok(play.libs.Json.toJson(resultado));
    }
    
    
    public ResultadosForm processarResultadosForm(Form<ResultadosForm> resultadoForm, boolean compara){
    	List<Integer> resultados = new ArrayList<Integer>();
    	ResultadosForm resultForm = new ResultadosForm();
    	String idTurmaString="", idCursoString="",idDiretoriaString="",idCampusString="",idAlunoString="",idEixoString="";
    	if(compara){
    		idTurmaString = resultadoForm.data().get("idTurmaComparacao");
    		idCursoString = resultadoForm.data().get("idCursoComparacao");
    		idDiretoriaString = resultadoForm.data().get("idDiretoriaComparacao");
    		idCampusString = resultadoForm.data().get("idCampusComparacao");
    		idAlunoString = resultadoForm.data().get("idAlunoComparacao");
    		idEixoString = resultadoForm.data().get("idEixoComparacao");
    	}else{
    		idTurmaString = resultadoForm.data().get("idTurma");
    		idCursoString = resultadoForm.data().get("idCurso");
    		idDiretoriaString = resultadoForm.data().get("idDiretoria");
    		idCampusString = resultadoForm.data().get("idCampus");
    		idAlunoString = resultadoForm.data().get("idAluno");
    		idEixoString = resultadoForm.data().get("idEixo");
    	}
    	
		boolean eixoTodos=false;
		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
			eixoTodos=true;
		}
		StringBuilder sb = new StringBuilder();
		int resultadoEvasometro = 0;
		int resultadoFaixaEtaria = 0;
		boolean processouPeloMenos1Resultado=false;
		if((idAlunoString!=null)&&!(idEixoString.trim().length()>10)&&(!idEixoString.equalsIgnoreCase("0"))&&!eixoTodos){
			Long idEixo = Long.valueOf(idEixoString);
			Long idAluno = Long.valueOf(idAlunoString);
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(idEixo, idAluno, null, null, null, null, false, false, false, false,false, eixoTodos, true);
			resultadoEvasometro = resultados.get(0);
			processouPeloMenos1Resultado=true;
			//resultadoFaixaEtaria
		}else if(idAlunoString!=null&&!(idAlunoString.trim().length()>10)&& (!idAlunoString.equalsIgnoreCase("0"))){
			Long idAluno = Long.valueOf(idAlunoString);
			Long idTurma = Long.valueOf(idTurmaString);
			Long idCurso = Long.valueOf(idCursoString);
			Long idCampus = Long.valueOf(idCampusString);
			Long idDiretoria = Long.valueOf(idDiretoriaString);
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
			processouPeloMenos1Resultado=true;
		}else if(idTurmaString!=null&&!(idTurmaString.trim().length()>10)&& (!idTurmaString.equalsIgnoreCase("0"))){
			Long idTurma = Long.valueOf(idTurmaString);
			Long idCurso = Long.valueOf(idCursoString);
			Long idCampus = Long.valueOf(idCampusString);
			Long idDiretoria = Long.valueOf(idDiretoriaString);
			//User user = User.findById(id);
			String nome=Turma.find.byId(idTurma).nome;
			sb.append(nome);
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, idTurma, idDiretoria, idCurso, idCampus, true, false, false, false, false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
			processouPeloMenos1Resultado=true;
		}else if((idCursoString!=null) &&!(idCursoString.trim().length()>10)&& (!idCursoString.equalsIgnoreCase("0"))){
			Long idCurso = Long.valueOf(idCursoString);
			Long idCampus = Long.valueOf(idCampusString);
			Long idDiretoria = Long.valueOf(idDiretoriaString);
			String nome=Curso.find.byId(idCurso).nome;
			sb.append(" - "+nome);
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, idDiretoria, idCurso, idCampus, false, true, false, false,false, eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
			processouPeloMenos1Resultado=true;
		} else if((idDiretoriaString!=null) &&!(idDiretoriaString.trim().length()>10)&&(!idDiretoriaString.equalsIgnoreCase("0"))){
			Long idCampus = Long.valueOf(idCampusString);
			Long idDiretoria = Long.valueOf(idDiretoriaString);
			String nome=Diretoria.find.byId(idDiretoria).nome;
			sb.append(" - "+nome);
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, idDiretoria, idCampus, false, false, true, false,false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
			processouPeloMenos1Resultado=true;
		} else if(idCampusString!=null&& !(idCampusString.trim().length()>10)&&(!idCampusString.equalsIgnoreCase("0"))){
			Long idCampus = Long.valueOf(idCampusString);
			String nome=Campus.find.byId(idCampus).nome;
			sb.append(" - "+nome);
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, null, idCampus, false, false, false, true,false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
			processouPeloMenos1Resultado=true;
		}
		resultForm.valor = resultadoEvasometro; 
		if(processouPeloMenos1Resultado){
			resultForm.valorFaixaEtaria15a19 = resultados.get(1);
			resultForm.valorFaixaEtaria20a24 = resultados.get(2);
			resultForm.valorFaixaEtaria25a29 = resultados.get(3);
			resultForm.valorFaixaEtaria30a34 = resultados.get(4);
			resultForm.valorFaixaEtaria35a39 = resultados.get(5);
			resultForm.valorFaixaEtaria40ouMais = resultados.get(6);
			
			resultForm.solteiro = resultados.get(7);
			resultForm.casado = resultados.get(8);
			resultForm.divorciado =  resultados.get(9);
			resultForm.separado = resultados.get(10);
			resultForm.viuvo = resultados.get(11);
			
			resultForm.branca = resultados.get(12);
			resultForm.preta = resultados.get(13);
			resultForm.parda = resultados.get(14);
			resultForm.amarela = resultados.get(15);
			resultForm.indigina = resultados.get(16);
			resultForm.semDeclaracao =  resultados.get(17);
			
			resultForm.ate1salario = resultados.get(18);
			resultForm.ate3salarios = resultados.get(19);
			resultForm.ate6salarios = resultados.get(20);
			resultForm.ate9salarios = resultados.get(21);
			resultForm.ate10ouMais = resultados.get(22);
			
			resultForm.masculino = resultados.get(23);
			resultForm.feminino = resultados.get(24);
			
		}
    	return resultForm;
    }
    
    public int processarResultadoParametro(Form<ResultadosForm> resultadoForm){
    	List<Integer> resultados = new ArrayList<Integer>();
    	String idTurmaString = resultadoForm.data().get("idTurma");
		String idCursoString = resultadoForm.data().get("idCurso");
		String idDiretoriaString = resultadoForm.data().get("idDiretoria");
		String idCampusString = resultadoForm.data().get("idCampus");
		String idAlunoString = resultadoForm.data().get("idAluno");
		String idEixoString = resultadoForm.data().get("idEixo");
		boolean eixoTodos=false;
		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
			eixoTodos=true;
		}
		int resultadoEvasometro = 0;
		int resultadoFaixaEtaria = 0;
    	if((idAlunoString!=null)&&!(idEixoString.trim().length()>10)&&(!idEixoString.equalsIgnoreCase("0"))&&!eixoTodos){
			Long idEixo = Long.valueOf(resultadoForm.data().get("idEixo"));
			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(idEixo, idAluno, null, null, null, null, false, false, false, false,false, eixoTodos, true);
			resultadoEvasometro = resultados.get(0);
			//resultadoFaixaEtaria
		}else if(idAlunoString!=null&&!(idAlunoString.trim().length()>10)&& (!idAlunoString.equalsIgnoreCase("0"))){
			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		}else if(idTurmaString!=null&&!(idTurmaString.trim().length()>10)&& (!idTurmaString.equalsIgnoreCase("0"))){
			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
			//User user = User.findById(id);
			String nome=Turma.find.byId(idTurma).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, idTurma, idDiretoria, idCurso, idCampus, true, false, false, false, false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		}else if((idCursoString!=null) &&!(idCursoString.trim().length()>10)&& (!idCursoString.equalsIgnoreCase("0"))){
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
			String nome=Curso.find.byId(idCurso).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, idDiretoria, idCurso, idCampus, false, true, false, false,false, eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		} else if((idDiretoriaString!=null) &&!(idDiretoriaString.trim().length()>10)&&(!idDiretoriaString.equalsIgnoreCase("0"))){
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
			String nome=Diretoria.find.byId(idDiretoria).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, idDiretoria, idCampus, false, false, true, false,false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		} else if(idCampusString!=null&& !(idCampusString.trim().length()>10)&&(!idCampusString.equalsIgnoreCase("0"))){
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
			String nome=Campus.find.byId(idCampus).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, null, idCampus, false, false, false, true,false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		}
    	return resultadoEvasometro;
    }
    
    
    public int processarResultadoParametro2(Form<ResultadosForm> resultadoForm){
    	List<Integer> resultados = new ArrayList<Integer>();
    	String idTurmaString = resultadoForm.data().get("idTurmaComparacao");
		String idCursoString = resultadoForm.data().get("idCursoComparacao");
		String idDiretoriaString = resultadoForm.data().get("idDiretoriaComparacao");
		String idCampusString = resultadoForm.data().get("idCampusComparacao");
		String idAlunoString = resultadoForm.data().get("idAlunoComparacao");
		String idEixoString = resultadoForm.data().get("idEixoComparacao");
		boolean eixoTodos=false;
		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
			eixoTodos=true;
		}
		int resultadoEvasometro = 0;
		int resultadoFaixaEtaria = 0;
    	if((idAlunoString!=null)&&!(idEixoString.trim().length()>10)&&(!idEixoString.equalsIgnoreCase("0"))&&!eixoTodos){
			Long idEixo = Long.valueOf(resultadoForm.data().get("idEixoComparacao"));
			Long idAluno = Long.valueOf(resultadoForm.data().get("idAlunoComparacao"));
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(idEixo, idAluno, null, null, null, null, false, false, false, false,false, eixoTodos, true);
			resultadoEvasometro = resultados.get(0);
			//resultadoFaixaEtaria
		}else if(idAlunoString!=null&&!(idAlunoString.trim().length()>10)&& (!idAlunoString.equalsIgnoreCase("0"))){
			Long idAluno = Long.valueOf(resultadoForm.data().get("idAlunoComparacao"));
			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurmaComparacao"));
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		}else if(idTurmaString!=null&&!(idTurmaString.trim().length()>10)&& (!idTurmaString.equalsIgnoreCase("0"))){
			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurmaComparacao"));
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
			//User user = User.findById(id);
			String nome=Turma.find.byId(idTurma).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, idTurma, idDiretoria, idCurso, idCampus, true, false, false, false, false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		}else if((idCursoString!=null) &&!(idCursoString.trim().length()>10)&& (!idCursoString.equalsIgnoreCase("0"))){
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
			String nome=Curso.find.byId(idCurso).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, idDiretoria, idCurso, idCampus, false, true, false, false,false, eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		} else if((idDiretoriaString!=null) &&!(idDiretoriaString.trim().length()>10)&&(!idDiretoriaString.equalsIgnoreCase("0"))){
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
			String nome=Diretoria.find.byId(idDiretoria).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, idDiretoria, idCampus, false, false, true, false,false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		} else if(idCampusString!=null&& !(idCampusString.trim().length()>10)&&(!idCampusString.equalsIgnoreCase("0"))){
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
			String nome=Campus.find.byId(idCampus).nome;
			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, null, idCampus, false, false, false, true,false,eixoTodos,false);
			resultadoEvasometro = resultados.get(0);
		}
    	return resultadoEvasometro;
    }
    
    public ResultadosForm processarResultadoEixo(Form<ResultadosForm> resultadoForm){
    	List<Integer> resultados = new ArrayList<Integer>();
    	ResultadosForm resultForm = new ResultadosForm();
		String idEixoString = resultadoForm.data().get("idEixo");
		boolean eixoTodos=false;
		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
			eixoTodos=true;
		}
		if(eixoTodos){
			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
			resultForm.valorEixoIndividual = InformacoesUsuarioHelper.pontuacaoEvasometroEixoIndividual(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoFamiliar = InformacoesUsuarioHelper.pontuacaoEvasometroEixoFamiliar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoIntraEscolar = InformacoesUsuarioHelper.pontuacaoEvasometroIntraEscolar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoCarreiraProfissional = InformacoesUsuarioHelper.pontuacaoEvasometroCarreiraProfissional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoAreaFormacao = InformacoesUsuarioHelper.pontuacaoEvasometroAreaFormacao(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoInstitucional = InformacoesUsuarioHelper.pontuacaoEvasometroInstitucional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
		}
    	return resultForm;
    }
    
    public ResultadosForm processarResultadoEixo2(Form<ResultadosForm> resultadoForm){
    	ResultadosForm resultForm = new ResultadosForm();
		String idEixoString = resultadoForm.data().get("idEixoComparacao");
		boolean eixoTodos=false;
		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
			eixoTodos=true;
		}
		if(eixoTodos){
			Long idAluno = Long.valueOf(resultadoForm.data().get("idAlunoComparacao"));
			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurmaComparacao"));
			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
			resultForm.valorEixoIndividualCompara = InformacoesUsuarioHelper.pontuacaoEvasometroEixoIndividual(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoFamiliarCompara = InformacoesUsuarioHelper.pontuacaoEvasometroEixoFamiliar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoIntraEscolarCompara = InformacoesUsuarioHelper.pontuacaoEvasometroIntraEscolar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoCarreiraProfissionalCompara = InformacoesUsuarioHelper.pontuacaoEvasometroCarreiraProfissional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoAreaFormacaoCompara = InformacoesUsuarioHelper.pontuacaoEvasometroAreaFormacao(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
			resultForm.valorEixoInstitucionalCompara = InformacoesUsuarioHelper.pontuacaoEvasometroInstitucional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
		}
    	return resultForm;
    }
    
    public Result consultarCompararDados(boolean consultarBD){
    	 ResultadosForm resultForm = new ResultadosForm();
    	 List<Curso> cursos = Curso.find.findList();
    	 List<Campus> campus = Campus.find.findList();
    	 List<Diretoria> diretorias = Diretoria.find.findList();
    	 List<Turma> turmas = Turma.find.findList();
    	 List<Usuario> alunos = Usuario.find.where().eq("is_administrador",false).eq("is_supervisor",false).findList();
    	 List<Integer> resultados = new ArrayList<Integer>();

    	 if(consultarBD){
    		List<Resultado> resultado= Resultado.find.where().findList();
    		if(resultado.size()>0){
    			resultForm.valor = resultado.get(resultado.size()-1).valor;
    			resultForm.valorCompara = resultado.get(resultado.size()-1).valorCompara;
    			resultado.get(resultado.size()-1).delete();
    			for (Resultado resultado2 : resultado) {
    				resultado2.delete();
    			}
    		}
    	}else{
    		
    		boolean processouPeloMenos1Resultado=false;
    		//TODO: Criar os outros campos na mesma classe ResultadosForm.
    		//TODO: Remover ResultadosComparaForm
    		Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    		
    		//String id = resultadoForm.data().get("idStatus");
    		String idTurmaString = resultadoForm.data().get("idTurma");
    		String idCursoString = resultadoForm.data().get("idCurso");
    		String idDiretoriaString = resultadoForm.data().get("idDiretoria");
    		String idCampusString = resultadoForm.data().get("idCampus");
    		String idAlunoString = resultadoForm.data().get("idAluno");
    		
    		String idEixoString = resultadoForm.data().get("idEixo");
    		boolean eixoTodos=false;
//		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
//			eixoTodos=true;
//		}
    		
    		StringBuilder sb = new StringBuilder();
    		int resultadoEvasometro = 0;
    		int resultadoFaixaEtaria = 0;
    		if((idAlunoString!=null)&&!(idEixoString.trim().length()>10)&&(!idEixoString.equalsIgnoreCase("0"))&&!eixoTodos){
    			Long idEixo = Long.valueOf(resultadoForm.data().get("idEixo"));
    			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(idEixo, idAluno, null, null, null, null, false, false, false, false,false, eixoTodos, true);
    			resultadoEvasometro = resultados.get(0);
    			processouPeloMenos1Resultado=true;
    			//resultadoFaixaEtaria
    		}else if(idAlunoString!=null&&!(idAlunoString.trim().length()>10)&& (!idAlunoString.equalsIgnoreCase("0"))){
    			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
    			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    			resultadoEvasometro = resultados.get(0);
    			processouPeloMenos1Resultado=true;
    		}else if(idTurmaString!=null&&!(idTurmaString.trim().length()>10)&& (!idTurmaString.equalsIgnoreCase("0"))){
    			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
    			//User user = User.findById(id);
    			String nome=Turma.find.byId(idTurma).nome;
    			sb.append(nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, idTurma, idDiretoria, idCurso, idCampus, true, false, false, false, false,eixoTodos,false);
    			resultadoEvasometro = resultados.get(0);
    			processouPeloMenos1Resultado=true;
    		}else if((idCursoString!=null) &&!(idCursoString.trim().length()>10)&& (!idCursoString.equalsIgnoreCase("0"))){
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
    			String nome=Curso.find.byId(idCurso).nome;
    			sb.append(" - "+nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, idDiretoria, idCurso, idCampus, false, true, false, false,false, eixoTodos,false);
    			resultadoEvasometro = resultados.get(0);
    			processouPeloMenos1Resultado=true;
    		} else if((idDiretoriaString!=null) &&!(idDiretoriaString.trim().length()>10)&&(!idDiretoriaString.equalsIgnoreCase("0"))){
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
    			String nome=Diretoria.find.byId(idDiretoria).nome;
    			sb.append(" - "+nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, idDiretoria, idCampus, false, false, true, false,false,eixoTodos,false);
    			resultadoEvasometro = resultados.get(0);
    			processouPeloMenos1Resultado=true;
    		} else if(idCampusString!=null&& !(idCampusString.trim().length()>10)&&(!idCampusString.equalsIgnoreCase("0"))){
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
    			String nome=Campus.find.byId(idCampus).nome;
    			sb.append(" - "+nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, null, idCampus, false, false, false, true,false,eixoTodos,false);
    			resultadoEvasometro = resultados.get(0);
    			processouPeloMenos1Resultado=true;
    		}
    		
    		
    		resultForm.valor = resultadoEvasometro; 
    		if(processouPeloMenos1Resultado){
    			resultForm.valorFaixaEtaria15a19 = resultados.get(1);
    			resultForm.valorFaixaEtaria20a24 = resultados.get(2);
    			resultForm.valorFaixaEtaria25a29 = resultados.get(3);
    			resultForm.valorFaixaEtaria30a34 = resultados.get(4);
    			resultForm.valorFaixaEtaria35a39 = resultados.get(5);
    			resultForm.valorFaixaEtaria40ouMais = resultados.get(6);
    			
    			resultForm.solteiro = resultados.get(7);
    			resultForm.casado = resultados.get(8);
    			resultForm.divorciado =  resultados.get(9);
    			resultForm.separado = resultados.get(10);
    			resultForm.viuvo = resultados.get(11);
    			
    			resultForm.branca = resultados.get(12);
    			resultForm.preta = resultados.get(13);
    			resultForm.parda = resultados.get(14);
    			resultForm.amarela = resultados.get(15);
    			resultForm.indigina = resultados.get(16);
    			resultForm.semDeclaracao =  resultados.get(17);
    			
    			resultForm.ate1salario = resultados.get(18);
    			resultForm.ate3salarios = resultados.get(19);
    			resultForm.ate6salarios = resultados.get(20);
    			resultForm.ate9salarios = resultados.get(21);
    			resultForm.ate10ouMais = resultados.get(22);
    			
    			resultForm.masculino = resultados.get(23);
    			resultForm.feminino = resultados.get(24);
    			
    		}
    		resultForm.opcoesEscolhidas = sb.toString();
    		
    		if(eixoTodos){
    			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
    			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurma"));
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCurso"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampus"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoria"));
    			resultForm.valorEixoIndividual = InformacoesUsuarioHelper.pontuacaoEvasometroEixoIndividual(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    			resultForm.valorEixoFamiliar = InformacoesUsuarioHelper.pontuacaoEvasometroEixoFamiliar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    			resultForm.valorEixoIntraEscolar = InformacoesUsuarioHelper.pontuacaoEvasometroIntraEscolar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    			resultForm.valorEixoCarreiraProfissional = InformacoesUsuarioHelper.pontuacaoEvasometroCarreiraProfissional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    			resultForm.valorEixoAreaFormacao = InformacoesUsuarioHelper.pontuacaoEvasometroAreaFormacao(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    			resultForm.valorEixoInstitucional = InformacoesUsuarioHelper.pontuacaoEvasometroInstitucional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodos,false);
    		}
    		
    		
    		
    		
    		////----------------------------
    		
    		resultados = new ArrayList<Integer>();
    		boolean processouPeloMenos1ResultadoCompara=false;
    		
    		String idTurmaStringCompara = resultadoForm.data().get("idTurmaComparacao");
    		String idCursoStringCompara = resultadoForm.data().get("idCursoComparacao");
    		String idDiretoriaStringCompara = resultadoForm.data().get("idDiretoriaComparacao");
    		String idCampusStringCompara = resultadoForm.data().get("idCampusComparacao");
    		String idAlunoStringCompara = resultadoForm.data().get("idAlunoComparacao");
    		
    		String idEixoStringCompara = resultadoForm.data().get("idEixoComparacao");
    		boolean eixoTodosCompara=false;
//		if(idEixoString!=null&&(idEixoString.equalsIgnoreCase("0")||idEixoString.equalsIgnoreCase("TODOS"))){
//			eixoTodos=true;
//		}
    		
    		StringBuilder sbCompara = new StringBuilder();
    		int resultadoEvasometroCompara = 0;
    		int resultadoFaixaEtariaCompara = 0;
    		if((idAlunoString!=null)&&!(idEixoString.trim().length()>10)&&(!idEixoString.equalsIgnoreCase("0"))&&!eixoTodos){
    			Long idEixo = Long.valueOf(resultadoForm.data().get("idEixo"));
    			Long idAluno = Long.valueOf(resultadoForm.data().get("idAluno"));
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(idEixo, idAluno, null, null, null, null, false, false, false, false,false, eixoTodos, true);
    			resultadoEvasometroCompara = resultados.get(0);
    			processouPeloMenos1ResultadoCompara=true;
    			//resultadoFaixaEtaria
    		}else if(idAlunoStringCompara!=null&&!(idAlunoStringCompara.trim().length()>10)&& (!idAlunoStringCompara.equalsIgnoreCase("0"))){
    			Long idAluno = Long.valueOf(resultadoForm.data().get("idAlunoComparacao"));
    			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurmaComparacao"));
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    			resultadoEvasometroCompara = resultados.get(0);
    			processouPeloMenos1ResultadoCompara=true;
    		}else if(idTurmaStringCompara!=null&&!(idTurmaStringCompara.trim().length()>10)&& (!idTurmaStringCompara.equalsIgnoreCase("0"))){
    			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurmaComparacao"));
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
    			//User user = User.findById(id);
    			String nome=Turma.find.byId(idTurma).nome;
    			sb.append(nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, idTurma, idDiretoria, idCurso, idCampus, true, false, false, false, false,eixoTodosCompara,false);
    			resultadoEvasometroCompara = resultados.get(0);
    			processouPeloMenos1ResultadoCompara=true;
    		}else if((idCursoStringCompara!=null) &&!(idCursoStringCompara.trim().length()>10)&& (!idCursoStringCompara.equalsIgnoreCase("0"))){
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
    			String nome=Curso.find.byId(idCurso).nome;
    			sb.append(" - "+nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, idDiretoria, idCurso, idCampus, false, true, false, false,false, eixoTodosCompara,false);
    			resultadoEvasometroCompara = resultados.get(0);
    			processouPeloMenos1ResultadoCompara=true;
    		} else if((idDiretoriaStringCompara!=null) &&!(idDiretoriaStringCompara.trim().length()>10)&&(!idDiretoriaStringCompara.equalsIgnoreCase("0"))){
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
    			String nome=Diretoria.find.byId(idDiretoria).nome;
    			sb.append(" - "+nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, idDiretoria, idCampus, false, false, true, false,false,eixoTodosCompara,false);
    			resultadoEvasometroCompara = resultados.get(0);
    			processouPeloMenos1ResultadoCompara=true;
    		} else if(idCampusStringCompara!=null&& !(idCampusStringCompara.trim().length()>10)&&(!idCampusStringCompara.equalsIgnoreCase("0"))){
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
    			String nome=Campus.find.byId(idCampus).nome;
    			sb.append(" - "+nome);
    			resultados = InformacoesUsuarioHelper.pontuacaoEvasometro(null, null, null, null, null, idCampus, false, false, false, true,false,eixoTodosCompara,false);
    			resultadoEvasometroCompara = resultados.get(0);
    			processouPeloMenos1ResultadoCompara=true;
    		}
    		
    		resultForm.valorCompara = resultadoEvasometroCompara; 
    		if(processouPeloMenos1ResultadoCompara){
    			resultForm.valorFaixaEtaria15a19Compara = resultados.get(1);
    			resultForm.valorFaixaEtaria20a24Compara =  resultados.get(2);
    			resultForm.valorFaixaEtaria25a29Compara = resultados.get(3);
    			resultForm.valorFaixaEtaria30a34Compara = resultados.get(4);
    			resultForm.valorFaixaEtaria35a39Compara = resultados.get(5);
    			resultForm.valorFaixaEtaria40ouMaisCompara = resultados.get(6);
    			
    			resultForm.solteiroCompara = resultados.get(7);
    			resultForm.casadoCompara = resultados.get(8);
    			resultForm.divorciadoCompara = resultados.get(9);
    			resultForm.separadoCompara = resultados.get(10);
    			resultForm.viuvoCompara = resultados.get(11);
    			
    			resultForm.brancaCompara =  resultados.get(12);
    			resultForm.pretaCompara = resultados.get(13);
    			resultForm.pardaCompara = resultados.get(14);
    			resultForm.amarelaCompara = resultados.get(15);
    			resultForm.indiginaCompara = resultados.get(16);
    			resultForm.semDeclaracaoCompara = resultados.get(17);
    			
    			resultForm.ate1salarioCompara = resultados.get(18);
    			resultForm.ate3salariosCompara  = resultados.get(19);
    			resultForm.ate6salariosCompara = resultados.get(20);
    			resultForm.ate9salariosCompara  = resultados.get(21);
    			resultForm.ate10ouMaisCompara = resultados.get(22);
    			
    			resultForm.masculinoCompara = resultados.get(23);
    			resultForm.femininoCompara = resultados.get(24);
    			
    		}
    		
    		resultForm.opcoesEscolhidasCompara = sbCompara.toString();
    		
    		if(eixoTodosCompara){
    			Long idAluno = Long.valueOf(resultadoForm.data().get("idAlunoComparacao"));
    			Long idTurma = Long.valueOf(resultadoForm.data().get("idTurmaComparacao"));
    			Long idCurso = Long.valueOf(resultadoForm.data().get("idCursoComparacao"));
    			Long idCampus = Long.valueOf(resultadoForm.data().get("idCampusComparacao"));
    			Long idDiretoria = Long.valueOf(resultadoForm.data().get("idDiretoriaComparacao"));
    			resultForm.valorEixoIndividualCompara = InformacoesUsuarioHelper.pontuacaoEvasometroEixoIndividual(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    			resultForm.valorEixoFamiliarCompara = InformacoesUsuarioHelper.pontuacaoEvasometroEixoFamiliar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    			resultForm.valorEixoIntraEscolarCompara = InformacoesUsuarioHelper.pontuacaoEvasometroIntraEscolar(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    			resultForm.valorEixoCarreiraProfissionalCompara = InformacoesUsuarioHelper.pontuacaoEvasometroCarreiraProfissional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    			resultForm.valorEixoAreaFormacaoCompara = InformacoesUsuarioHelper.pontuacaoEvasometroAreaFormacao(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    			resultForm.valorEixoInstitucionalCompara = InformacoesUsuarioHelper.pontuacaoEvasometroInstitucional(null, idAluno, idTurma, idDiretoria, idCurso, idCampus, false, false, false, false, true, eixoTodosCompara,false);
    		}
    		
    		Resultado result = new Resultado();
    		result.valor = resultadoEvasometro;
    		result.valorCompara = resultadoEvasometroCompara;
    		result.save();
    	}
        
        return ok(views.html.Resultados.comparar.render(resultForm, alunos, turmas, cursos, diretorias, campus, alunos, turmas, cursos, diretorias, campus));
    }
    
    @SuppressWarnings("deprecation")
	public Result exportCSV() throws Exception{
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	Controller.response().setContentType("text/csv");
    	Controller.response().setHeader("Content-Disposition", "attachment;filename=simea_exportacao.csv");
    	Controller.response().setHeader("Cache-control", "private");
    	//pego todos os dados
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,false);
    	
    	StringBuilder sb = new StringBuilder();
    	String evasometro = CSVUtils.writeLine(Arrays.asList("Evasometro"));
    	String evasometroValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.valor)));
    	String eixosNome = CSVUtils.writeLine(Arrays.asList("Individual","Familiar", "IntraEscolar", "Carreira Profissional", "Area Formacao", "Institucional"));
    	String eixosValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.valorEixoIndividual), String.valueOf(resultado.valorEixoFamiliar),
    			String.valueOf(resultado.valorEixoIntraEscolar),String.valueOf(resultado.valorEixoCarreiraProfissional),
    			String.valueOf(resultado.valorEixoAreaFormacao),String.valueOf(resultado.valorEixoInstitucional)));
    	String sexoNome = CSVUtils.writeLine(Arrays.asList("Masculino","Feminino"));
    	String sexoValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.masculino), String.valueOf(resultado.feminino)));
    	String faixaEtariaNome = CSVUtils.writeLine(Arrays.asList("15 a 19 anos","20 a 24 anos", "25 a 29 anos", "30 a 34 anos", "35 a 39 anos", "40 anos ou mais"));
    	String faixaEtariaValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.valorFaixaEtaria15a19), String.valueOf(resultado.valorFaixaEtaria20a24),
    			String.valueOf(resultado.valorFaixaEtaria25a29),String.valueOf(resultado.valorFaixaEtaria30a34),String.valueOf(resultado.valorFaixaEtaria35a39),
    			String.valueOf(resultado.valorFaixaEtaria40ouMais)));
    	String estadoCivilNome= CSVUtils.writeLine(Arrays.asList("Solteiro","Casado", "Divorciado", "Separado", "Viúvo"));
    	String estadoCivilValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.solteiro), String.valueOf(resultado.casado), String.valueOf(resultado.divorciado),
    			 String.valueOf(resultado.separado),  String.valueOf(resultado.viuvo)));
    	String racaCor = CSVUtils.writeLine(Arrays.asList("Branca","Preta", "Parda", "Amarela", "Indígina", "Sem Declaração"));
    	String racaValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.branca), String.valueOf(resultado.preta), String.valueOf(resultado.parda),
    			String.valueOf(resultado.amarela), String.valueOf(resultado.indigina),String.valueOf(resultado.semDeclaracao)));
    	String rendaNome = CSVUtils.writeLine(Arrays.asList("Até 1 Salário mínimo","1 a 3 salários mínimos","4 a 6 salários mínimos","7 a 9 salários mínimos","10 ou mais salários mínimos"));
    	String rendaValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.ate1salario), String.valueOf(resultado.ate3salarios),String.valueOf(resultado.ate6salarios),
    			String.valueOf(resultado.ate9salarios), String.valueOf(resultado.ate10ouMais)));
    	
    	sb.append(evasometro+"\r\n");
    	sb.append(evasometroValor+"\r\n");
    	sb.append(eixosNome+"\r\n");
    	sb.append(eixosValor+"\r\n");
    	sb.append(sexoNome+"\r\n");
    	sb.append(sexoValor+"\r\n");
    	sb.append(faixaEtariaNome+"\r\n");
    	sb.append(faixaEtariaValor+"\r\n");
    	sb.append(estadoCivilNome+"\r\n");
    	sb.append(estadoCivilValor+"\r\n");
    	sb.append(racaCor+"\r\n");
    	sb.append(racaValor+"\r\n");
    	sb.append(rendaNome+"\r\n");
    	sb.append(rendaValor+"\r\n");
    	
    	return ok(sb.toString());
    }
    
    
    @SuppressWarnings("deprecation")
	public Result exportCSV2() throws Exception{
    	Form<ResultadosForm> resultadoForm = formFactory.form(ResultadosForm.class).bindFromRequest();
    	Controller.response().setContentType("text/csv");
    	Controller.response().setHeader("Content-Disposition", "attachment;filename=simea_exportacao.csv");
    	Controller.response().setHeader("Cache-control", "private");
    	
    	//pego todos os dados
    	ResultadosForm resultado = processarResultadosForm(resultadoForm,true);
    	
    	StringBuilder sb = new StringBuilder();
    	String evasometro = CSVUtils.writeLine(Arrays.asList("Evasometro"));
    	String evasometroValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.valor)));
    	String eixosNome = CSVUtils.writeLine(Arrays.asList("Individual","Familiar", "IntraEscolar", "Carreira Profissional", "Area Formacao", "Institucional"));
    	String eixosValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.valorEixoIndividual), String.valueOf(resultado.valorEixoFamiliar),
    			String.valueOf(resultado.valorEixoIntraEscolar),String.valueOf(resultado.valorEixoCarreiraProfissional),
    			String.valueOf(resultado.valorEixoAreaFormacao),String.valueOf(resultado.valorEixoInstitucional)));
    	String sexoNome = CSVUtils.writeLine(Arrays.asList("Masculino","Feminino"));
    	String sexoValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.masculino), String.valueOf(resultado.feminino)));
    	String faixaEtariaNome = CSVUtils.writeLine(Arrays.asList("15 a 19 anos","20 a 24 anos", "25 a 29 anos", "30 a 34 anos", "35 a 39 anos", "40 anos ou mais"));
    	String faixaEtariaValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.valorFaixaEtaria15a19), String.valueOf(resultado.valorFaixaEtaria20a24),
    			String.valueOf(resultado.valorFaixaEtaria25a29),String.valueOf(resultado.valorFaixaEtaria30a34),String.valueOf(resultado.valorFaixaEtaria35a39),
    			String.valueOf(resultado.valorFaixaEtaria40ouMais)));
    	String estadoCivilNome= CSVUtils.writeLine(Arrays.asList("Solteiro","Casado", "Divorciado", "Separado", "Viúvo"));
    	String estadoCivilValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.solteiro), String.valueOf(resultado.casado), String.valueOf(resultado.divorciado),
    			 String.valueOf(resultado.separado),  String.valueOf(resultado.viuvo)));
    	String racaCor = CSVUtils.writeLine(Arrays.asList("Branca","Preta", "Parda", "Amarela", "Indígina", "Sem Declaração"));
    	String racaValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.branca), String.valueOf(resultado.preta), String.valueOf(resultado.parda),
    			String.valueOf(resultado.amarela), String.valueOf(resultado.indigina),String.valueOf(resultado.semDeclaracao)));
    	String rendaNome = CSVUtils.writeLine(Arrays.asList("Até 1 Salário mínimo","1 a 3 salários mínimos","4 a 6 salários mínimos","7 a 9 salários mínimos","10 ou mais salários mínimos"));
    	String rendaValor = CSVUtils.writeLine(Arrays.asList(String.valueOf(resultado.ate1salario), String.valueOf(resultado.ate3salarios),String.valueOf(resultado.ate6salarios),
    			String.valueOf(resultado.ate9salarios), String.valueOf(resultado.ate10ouMais)));
    	
    	sb.append(evasometro+"\r\n");
    	sb.append(evasometroValor+"\r\n");
    	sb.append(eixosNome+"\r\n");
    	sb.append(eixosValor+"\r\n");
    	sb.append(sexoNome+"\r\n");
    	sb.append(sexoValor+"\r\n");
    	sb.append(faixaEtariaNome+"\r\n");
    	sb.append(faixaEtariaValor+"\r\n");
    	sb.append(estadoCivilNome+"\r\n");
    	sb.append(estadoCivilValor+"\r\n");
    	sb.append(racaCor+"\r\n");
    	sb.append(racaValor+"\r\n");
    	sb.append(rendaNome+"\r\n");
    	sb.append(rendaValor+"\r\n");
    	
    	return ok(sb.toString());
    }
}
