package structures;

import models.Usuario;

public class Score {
	public String quiz;
	public Usuario quizParticipant;
	public int difficulty;
	public int points;
	public int allQuestions;
	public int numberOfCorrectAnswers;
	public Time quizAnswerTime;
	public Time bestTime;	
	
	public Score(int difficulty, int points, int allQuestions, int numberOfCorrectAnswers, Long quizAnswerTime,
			Long bestTime, String quiz, Usuario quizParticipant) {
		this.difficulty = difficulty;
		this.points = points;
		this.allQuestions = allQuestions;
		this.numberOfCorrectAnswers = numberOfCorrectAnswers;
		this.quizAnswerTime = new Time(quizAnswerTime);
		this.bestTime = new Time(bestTime);	
		this.quiz = quiz;
		this.quizParticipant = quizParticipant;
	}
}