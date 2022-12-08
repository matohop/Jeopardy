package jeopardy;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jeopardy.GameboardWindow.PlayerUsernameAndScore;

public class QuestionWindow extends VBox implements Initializer {
	
	public  Question    question;
	public  Text        txtClue, txtPlayerBuzzed;
	public  TextField   tfAnswerField;
	public  Button      btnOK;
	public  Image       imgHappy, imgSad;
	public  ImageView   imgViewQuestion;
	public  ProgressBar progressBar;
	public  Timeline    timer;
	private Scene       questionScene;
	
	public ArrayList<Player> plyrsNotAnswered;
	
	QuestionWindow(Question q) {

		question         = q;
		txtClue          = new Text(question.getClue());
		txtPlayerBuzzed  = new Text();
		tfAnswerField    = new TextField();
		btnOK            = new Button("OK");
		imgHappy         = new Image("resources/images/trebek_happy.jpg");
		imgSad           = new Image("resources/images/trebek_sad.jpg");
		imgViewQuestion  = new ImageView(imgHappy);
		progressBar      = new ProgressBar();
		timer            = new Timeline();
		questionScene    = new Scene(this);
		plyrsNotAnswered = new ArrayList<>(GameboardWindow._players);
		
		txtClue.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 15));
		txtPlayerBuzzed.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14));

		this.setPadding(new Insets(5));
		
		init();
	}

	@Override
	public void init() {
		
		// disable these nodes
		btnOK.setDisable(true);
		tfAnswerField.setDisable(true);
		txtPlayerBuzzed.setVisible(false);
		
		// add nodes to VBox
		this.getChildren().addAll(imgViewQuestion, txtClue, tfAnswerField, btnOK, txtPlayerBuzzed, progressBar);
		
		// set ProgressBar width
		progressBar.prefWidthProperty().bind(this.widthProperty());
		
		// start countdown
		startTimer();
		

		// Actions/Listeners -------------------------------------------------------------
		
		// -----------------------------------------------------------------------
		// When a player buzzes in
		// -----------------------------------------------------------------------
		questionScene.setOnKeyPressed(e -> {
			
			try {
				
				if (GameboardWindow.numPlayers == 1 && e.getCode() == KeyCode.SHIFT) {

					enableNodes();

				} else if (GameboardWindow.numPlayers == 2) {
					
					switch(e.getCode()) {

						case SHIFT:
							
							if (plyrsNotAnswered.contains(GameboardWindow._players.get(0))) {
								
								imgViewQuestion.setImage(imgHappy);
								enableNodes();
								plyrsNotAnswered.remove(GameboardWindow._players.get(0));
								txtPlayerBuzzed.setText(GameboardWindow._players.get(0).getUsername() + " answered!");
								GameboardWindow.currentPlayerIndex = 0;
								break;
							}

						case SPACE:
							
							if (plyrsNotAnswered.contains(GameboardWindow._players.get(1))) {
								
								imgViewQuestion.setImage(imgHappy);
								enableNodes();
								plyrsNotAnswered.remove(GameboardWindow._players.get(1));
								txtPlayerBuzzed.setText(GameboardWindow._players.get(1).getUsername() + " answered!");
								GameboardWindow.currentPlayerIndex = 1;
								break;
							}

						default: break;
					}
					
				} else {
					
					enableNodes();
					
					switch(e.getCode()) {
					
						case SHIFT:
							
							if (plyrsNotAnswered.contains(GameboardWindow._players.get(0))) {
								
								imgViewQuestion.setImage(imgHappy);
								enableNodes();
								plyrsNotAnswered.remove(GameboardWindow._players.get(0));
								txtPlayerBuzzed.setText(GameboardWindow._players.get(0).getUsername() + " answered!");
								GameboardWindow.currentPlayerIndex = 0;
								break;
							}

						case SPACE:
							
							if (plyrsNotAnswered.contains(GameboardWindow._players.get(1))) {
								
								imgViewQuestion.setImage(imgHappy);
								enableNodes();
								plyrsNotAnswered.remove(GameboardWindow._players.get(1));
								txtPlayerBuzzed.setText(GameboardWindow._players.get(1).getUsername() + " answered!");
								GameboardWindow.currentPlayerIndex = 1;
								break;
							}

						case ENTER:
							
							if (plyrsNotAnswered.contains(GameboardWindow._players.get(2))) {
								
								imgViewQuestion.setImage(imgHappy);
								enableNodes();
								plyrsNotAnswered.remove(GameboardWindow._players.get(2));
								txtPlayerBuzzed.setText(GameboardWindow._players.get(2).getUsername() + " answered!");
								GameboardWindow.currentPlayerIndex = 2;
								break;
							}

						default: break;
					}
				}
				
				// disable buzzing in
				//questionScene.setOnKeyPressed(null);
				
				// stop and restart timer
				timer.stop();
				startTimer();
				
			} catch (Exception ex) {
				
				System.out.println(ex.getMessage());
			}
		});
		
		// fire OK Button if Enter key is pressed
		tfAnswerField.setOnKeyPressed(e -> {
			
			if (e.getCode() == KeyCode.ENTER)
				btnOK.fire();
		});
		
		// Button action - OK
		btnOK.setOnAction(e -> {
			
			timer.stop();
			
			Player p = GameboardWindow._players.get(GameboardWindow.currentPlayerIndex);
			
			// if answered correct
			if (tfAnswerField.getText().toLowerCase().equals(question.getAnswer())) {
				
				// increment player's balance, set player's turn, update score text
				p.setCurrentScore(p.getCurrentScore() + question.getValue());
				GameboardWindow.turnIndex = GameboardWindow.currentPlayerIndex;
				PlayerUsernameAndScore.txtPlayerScore[GameboardWindow.turnIndex].setText(String.format("$%,d", p.getCurrentScore()));
				
				gotoGameBoardWindow();
				
			// answered wrong
			} else {
				
				imgViewQuestion.setImage(imgSad);
				
				// decrement player's balance, set player's turn, update score text
				p.setCurrentScore(p.getCurrentScore() - question.getValue());
				PlayerUsernameAndScore.txtPlayerScore[GameboardWindow.currentPlayerIndex].setText(String.format("$%,d", p.getCurrentScore()));
				
				// if not all players have tried to answer, then repeat question
				if (!(plyrsNotAnswered.isEmpty())) {
					
					disableNodes();
					tfAnswerField.clear();
					txtPlayerBuzzed.setText("");
					startTimer();
				}
				
				// otherwise go to GameboardWindow
				else
					gotoGameBoardWindow();
			}
		});
		
		// ProgessBar listener - what to do when timer runs out
		progressBar.progressProperty().addListener(e -> {
			
			// if timer runs out
			if (progressBar.getProgress() == 0) {
				
				/* if no player buzzed in, then go back to GameboardWindow
				   otherwise decrement balance of player who buzzed in
				   if not all players have tried to answer, then repeat the question */
				
				gotoGameBoardWindow();
			}
		});
		
	} // end init
	
	// enable these nodes when a player buzzes in
	private void enableNodes() {
		
		btnOK.setDisable(false);
		tfAnswerField.setDisable(false);
		
		if (GameboardWindow.numPlayers != 1)
			txtPlayerBuzzed.setVisible(true);
	}
	
	private void disableNodes() {
		
		btnOK.setDisable(true);
		tfAnswerField.setDisable(true);
		
		if (GameboardWindow.numPlayers != 1)
			txtPlayerBuzzed.setVisible(true);
	}
	
	private void startTimer() {
		
		progressBar.setProgress(1);
		timer.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new KeyValue(progressBar.progressProperty(), 0)));
		timer.play();
	}
	
	private void gotoGameBoardWindow() {
		
		GameboardWindow.questionStage.close();
		Main.getPrimaryStage().show();
	}
	
	public void answeredCorrectImage() { imgViewQuestion.setImage(new Image("resources/images/trebek_happy.jpg")); }
	
	public void answeredWrongImage() { imgViewQuestion.setImage(new Image("resources/images/trebek_sad.jpg")); }
	
	public Scene getQuestionScene() { return questionScene; }

}
