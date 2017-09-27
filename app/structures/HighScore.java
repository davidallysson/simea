package structures;

import models.Usuario;


public class HighScore {
	
	public Usuario user;
	public int score;
	public int position;
	
	public HighScore(Usuario u, int s, int p) {
		user = u;
		score = s;
		position = p;
	}

	public int getScore() {
		return score;
	}
	
	public Usuario getUser() {
		return user;
	}
}
