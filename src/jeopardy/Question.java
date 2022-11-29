package jeopardy;

public class Question {
	
	private int    questionID, value;
	private String category, clue, answer;
	private String type;
	
	public Question(int questionID, String category, String clue, String answer, int value, String type) {
		
		this.questionID = questionID;
		this.category   = category;
		this.clue       = clue;
		this.answer     = answer;
		this.value      = value;
		this.type       = type;
	}

	public int getQuestionID() { return questionID; }

	public void setQuestionID(int questionID) { this.questionID = questionID; }
	
	public String getCategory() { return category; }

	public void setCategory(String category) { this.category = category; }
	
	public String getClue() { return clue; }

	public void setClue(String clue) { this.clue = clue; }
	
	public String getAnswer() { return answer; }

	public void setAnswer(String answer) { this.answer = answer; }

	public int getValue() { return value; }

	public void setValue(int value) { this.value = value; }

	public String getType() { return type; }

	public void setType(String type) { this.type = type; }

}
