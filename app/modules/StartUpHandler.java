package modules;

import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.Answer;
import models.Campus;
import models.Curso;
import models.Diretoria;
import models.Eixo;
import models.Questao;
import models.Quiz;
import models.Turma;
import models.Usuario;
import play.Logger;

/**
 * Substitui subtitui o {@code Global.onStart()}
 * Cria os registros na base de dados. Logo que inicia a aplicação
 * @author Alessandro
 *
 */
@Singleton
public class StartUpHandler {

	@Inject
    public StartUpHandler() {  
		
		if (Campus.find.all().size() == 0) {
			Campus natalCentral = new Campus();
			natalCentral.nome = "Natal-Central";
			natalCentral.save();
			Logger.info("Cadastrando o Campus: "+natalCentral.nome);
		}
		
		if (Diretoria.find.all().size() == 0) {
			Diretoria diaren = new Diretoria();
			diaren.campus=Campus.find.all().get(0);
			diaren.nome="Diaren";
			diaren.save();
			Logger.info("Cadastrando a diretoria: "+diaren.nome);
		}
		
		if (Curso.find.all().size() == 0) {
			Curso curso = new Curso();
			curso.nome="Controle Ambiental";
			curso.campus=Campus.find.all().get(0);
			curso.diretoria=Diretoria.find.all().get(0);
			curso.save();
			Logger.info("Cadastrando o curso: "+curso.nome);
		}
		
		if (Turma.find.all().size() == 0) {
			Turma turma = new Turma();
			turma.nome="Controle 2017.1";
			turma.campus=Campus.find.all().get(0);
			turma.diretoria=Diretoria.find.all().get(0);
			turma.curso=Curso.find.all().get(0);
			turma.save();
			Logger.info("Cadastrando a turma: "+turma.nome);
		}
		
		if (Usuario.find.all().size() == 0) {
			Usuario administrador = new Usuario();
			administrador.nome = "Admin";
			administrador.email = "admin@gmail.com";
			administrador.password = "admin";
			administrador.isAdministrador = true;
			administrador.ativo=true;
			administrador.masculino = true;
			administrador.feminino = false;
			administrador.faixaEtaria = 1;
			administrador.estadoCivil = 1;
			administrador.raca = 1;
			administrador.renda = 1;
			administrador.matricula = "201714040444";
			administrador.campus=Campus.find.all().get(0);
			administrador.turma=Turma.find.all().get(0);
			administrador.diretoria=Diretoria.find.all().get(0);
			administrador.curso=Curso.find.all().get(0);
			administrador.save();
			Logger.info("Cadastrando os Administradores do Sistema.");
			
			Usuario aluno = new Usuario();
			aluno.nome = "Alessandro Anjos";
			aluno.email = "sandro.adsc@gmail.com";
			aluno.password = "admin";
			aluno.isAdministrador = false;
			aluno.ativo=true;
			aluno.masculino = true;
			aluno.feminino = false;
			aluno.faixaEtaria = 1;
			aluno.estadoCivil = 1;
			aluno.raca = 1;
			aluno.renda = 1;
			aluno.matricula = "201114040444";
			aluno.campus=Campus.find.all().get(0);
			aluno.turma=Turma.find.all().get(0);
			aluno.diretoria=Diretoria.find.all().get(0);
			aluno.curso=Curso.find.all().get(0);
			aluno.save();
			Logger.info("Cadastrando os Alunos do Sistema.");
		}
		
		if(Eixo.find.all().size()==0){
			Eixo individual  = new Eixo();
			individual.id=1L;
			individual.nome="Individual";
			individual.save();
			
			Eixo familiar  = new Eixo();
			familiar.id=2L;
			familiar.nome="Familiar";
			familiar.save();
			
			Eixo intraEscolar  = new Eixo();
			intraEscolar.id=3L;
			intraEscolar.nome="IntraEscolar";
			intraEscolar.save();
			
			Eixo profissional  = new Eixo();
			profissional.id=4L;
			profissional.nome="Carreira Profissional";
			profissional.save();
			
			Eixo areaFormacao  = new Eixo();
			areaFormacao.id=5L;
			areaFormacao.nome="Área de Formação";
			areaFormacao.save();
			
			Eixo institucional  = new Eixo();
			institucional.id=6L;
			institucional.nome="Institucional";
			institucional.save();
			Logger.info("Cadastrando os Eixos do Sistema.");
		}
		
		if (Quiz.find.all().size() == 0) {
			  SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			  Quiz q;
			try {
				Usuario usuario = Usuario.find.where().eq("email", "admin@gmail.com").findUnique();
				//Questionário 01
				  q = new Quiz("Análise e Avaliação dos Motivos da Reprovação Escolar nos Cursos Técnicos do IFRN - CNAT", 2, format.parse("12-12-2012"), format.parse("15-07-2030"), usuario, "Individual");
				  Questao q1 = new Questao("Sobre a escolha do curso, qual das seguintes opções foi mais decisiva?", q, Eixo.find.all().get(0), Usuario.find.all().get(0));
				  Questao q2 = new Questao("Dentre alternativas que restaram, qual foi o fator mais decisivo na hora de escolher o seu curso?", q, Eixo.find.all().get(1),Usuario.find.all().get(0));
				  
				  Answer a1 = new Answer("Sempre quis fazer o curso escolhido.", true, q1, 1, 1);
				  Answer a2 = new Answer("Tenho afinidade com o assunto ou área", true, q1, 2, 2);
				  Answer a3 = new Answer("Houve pressão familiar no momento de escolher o curso", true, q1, 3, 3);
				  Answer a4 = new Answer("Gostaria de fazer outro curso, mas tive que me submeter ao que minha nota do exame de admissão permitia.", true, q1, 4, 4);
				  
				  Answer b1 = new Answer("Sempre quis fazer o curso escolhido.", true, q2, 1, 1);
				  Answer b2 = new Answer("Tenho afinidade com o assunto ou área", true, q2, 2, 2);
				  Answer b3 = new Answer("Houve pressão familiar no momento de escolher o curso", true, q2, 3, 3);
				  Answer b4 = new Answer("Gostaria de fazer outro curso, mas tive que me submeter ao que minha nota do exame de admissão permitia.", true, q2, 4, 4);

				  q.save();
				  q1.save();
				  q2.save();
				  a1.save();
				  a2.save();
				  a3.save();
				  a4.save();
				  b1.save();
				  b2.save();
				  b3.save();
				  b4.save();
				  
				  Logger.info("Cadastrando os Questionários, Questões e Alternativas do Sistema.");
				  
			}catch (Exception e) {
			}
		}
    } 
}
