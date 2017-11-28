package models;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

/**
 *
 * @author Alessandro
 *
 */
@Entity
public class Usuario extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@Required(message = "O campo deve ser preenchido.")
	public String nome;

	@Required(message = "O campo deve ser preenchido.")
	@MinLength(value=6, message="Matrícula inválida.")
	public String matricula;

	@Required(message="O campo deve ser preenchido.")
	@Column(unique=true)
	@Email(message="Você deve informar um email válido.")
	public String email;

	@Required(message="O campo deve ser preenchido.")
	@MinLength(value=6, message="A senha deve conter no mínimo 6 caracteres.")
	public String password;

	public boolean isAdministrador;

	public boolean isSupervisor;

	public String chaveRedefinicaoSenha;

	public boolean ativo;

	public String entrou;

	public boolean masculino;

	public boolean feminino;

	public int faixaEtaria;

	public int estadoCivil;

	public int raca;

	public int renda;

	public String telefone;

	@OneToMany
	@JsonManagedReference
	public List<Questao> questoes;

	@OneToMany(fetch=FetchType.EAGER, mappedBy="usuario")
	@JsonManagedReference
	public List<PontuacaoEvasao> pontuacaoEvasao;

	@ManyToOne
	public Campus campus;

	@ManyToOne
	public Diretoria diretoria;

	@ManyToOne
	public Curso curso;

	@ManyToOne
	public Turma turma;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdministrador() {
		return isAdministrador;
	}

	public void setAdministrador(boolean isAdministrador) {
		this.isAdministrador = isAdministrador;
	}

	public boolean isSupervisor() {
		return isSupervisor;
	}

	public void setSupervisor(boolean isSupervisor) {
		this.isSupervisor = isSupervisor;
	}

	public String getChaveRedefinicaoSenha() {
		return chaveRedefinicaoSenha;
	}

	public void setChaveRedefinicaoSenha(String chaveRedefinicaoSenha) {
		this.chaveRedefinicaoSenha = chaveRedefinicaoSenha;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getEntrou() {
		return entrou;
	}

	public void setEntrou(String entrou) {
		this.entrou = entrou;
	}

	public boolean isMasculino() {
		return masculino;
	}

	public void setMasculino(boolean masculino) {
		this.masculino = masculino;
	}

	public boolean isFeminino() {
		return feminino;
	}

	public void setFeminino(boolean feminino) {
		this.feminino = feminino;
	}

	public int getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(int faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public int getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(int estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public int getRaca() {
		return raca;
	}

	public void setRaca(int raca) {
		this.raca = raca;
	}

	public int getRenda() {
		return renda;
	}

	public void setRenda(int renda) {
		this.renda = renda;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public List<Questao> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<Questao> questoes) {
		this.questoes = questoes;
	}

	public List<PontuacaoEvasao> getPontuacaoEvasao() {
		return pontuacaoEvasao;
	}

	public void setPontuacaoEvasao(List<PontuacaoEvasao> pontuacaoEvasao) {
		this.pontuacaoEvasao = pontuacaoEvasao;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public static Finder<Long, Usuario> find = new Finder<Long,Usuario>(Usuario.class);

	/**
     * Método responsável por verificar se o usuário e senha conferem no banco de dados.
	 * @throws NoSuchAlgorithmException
     */
    public static Usuario autenticar(String login, String senha) throws NoSuchAlgorithmException {
        return find.where()
            .eq("email", login)
            .eq("password", senha)
            .findUnique();
    }

}
