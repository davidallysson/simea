package forms;

import java.security.NoSuchAlgorithmException;

import models.Usuario;

public class LoginForm {

	public String email;
	public String senha;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String validate() throws NoSuchAlgorithmException {
        if (Usuario.autenticar(email, senha) == null) {
        	return "Login ou Senha Inválida(s). Tente novamente!";
        }
        return null;
    }
}
