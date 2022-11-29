package jeopardy;

public class Player {
	
	private String username;
	private int    playerID, highScore, numGamesPlayed, numQuestionsCorrect, currentScore;
	
	public Player(int playerID, String username, int highScore, int numGamesPlayed, int numQuestionsCorrect) {
		
		this.playerID            = playerID;
		this.username            = username;
		this.highScore           = highScore;
		this.numGamesPlayed      = numGamesPlayed;
		this.numQuestionsCorrect = numQuestionsCorrect;
		currentScore             = 0;
	}

	public int getPlayerID() { return playerID; }

	public void setPlayerID(int playerID) { this.playerID = playerID; }
	
	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }

	public int getHighScore() { return highScore; }

	public void setHighScore(int highScore) { this.highScore = highScore; }

	public int getNumGamesPlayed() { return numGamesPlayed; }

	public void setNumGamesPlayed(int numGamesPlayed) { this.numGamesPlayed = numGamesPlayed; }

	public int getNumQuestionsCorrect() { return numQuestionsCorrect; }

	public void setNumQuestionsCorrect(int numQuestionsCorrect) { this.numQuestionsCorrect = numQuestionsCorrect; }
	
	public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }
	
	public int getCurrentScore() { return currentScore; }
	
}
