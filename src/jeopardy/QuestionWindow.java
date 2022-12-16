package jeopardy;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	public  Button      btnOk;
	public  Image       imgHappy, imgSad;
	public  ImageView   imgViewQuestion;
	public  ProgressBar progressBar;
	public  Timeline    timer;
	private Scene       questionScene;
	
	public ArrayList<Player> plyrsNotAnswered;
	
	QuestionWindow(Question q) {

		question         = q;
		txtClue          = new Text(question.getClue());
		txtPlayerBuzzed  = new Text("");
		tfAnswerField    = new TextField();
		btnOk            = new Button("Ok");
		imgHappy         = new Image("resources/images/trebek_happy.jpg");
		imgSad           = new Image("resources/images/trebek_sad.jpg");
		imgViewQuestion  = new ImageView(imgHappy);
		progressBar      = new ProgressBar();
		timer            = new Timeline();
		questionScene    = new Scene(this, 750, 300);
		plyrsNotAnswered = new ArrayList<>(GameboardWindow._players);
		
		txtClue.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 15));
		txtPlayerBuzzed.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14));

		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(5));
		this.setSpacing(5);
		
		init();
	}

	@Override
	public void init() {
		
		// disable QuestionWindow nodes
		disableNodes();
		
		// add nodes to VBox
		this.getChildren().addAll(imgViewQuestion, txtClue, tfAnswerField, btnOk, txtPlayerBuzzed, progressBar);
		
		// set ProgressBar width
		progressBar.prefWidthProperty().bind(this.widthProperty());
		
		// if DD, change question value
		if (isDailyDouble()) {
			
			question.setValue(WagerWindow.wagerAmount);
			enableNodes();
			questionScene.setOnKeyPressed(null);
		}
		
		// start countdown
		startTimer();
		
		// -----------------------------------------------------------------------
		// When a player buzzes in
		// -----------------------------------------------------------------------
		questionScene.setOnKeyPressed(e -> {
			
			try {
				
				if (GameboardWindow.numPlayers == 1) {

					switch(e.getCode()) {

						case SHIFT: if (plyrsNotAnswered.contains(GameboardWindow._players.get(0))) { onKeyPressed(0); break; }
						default: break;
					}

				} else if (GameboardWindow.numPlayers == 2) {
					
					switch(e.getCode()) {

						case SHIFT: if (plyrsNotAnswered.contains(GameboardWindow._players.get(0))) { onKeyPressed(0); break; }
						case SPACE: if (plyrsNotAnswered.contains(GameboardWindow._players.get(1))) { onKeyPressed(1); break; }
						default: break;
					}
					
				} else {
					
					switch(e.getCode()) {
					
						case SHIFT:      if (plyrsNotAnswered.contains(GameboardWindow._players.get(0))) { onKeyPressed(0); break; }
						case SPACE:      if (plyrsNotAnswered.contains(GameboardWindow._players.get(1))) { onKeyPressed(1); break; }
						case BACK_SPACE: if (plyrsNotAnswered.contains(GameboardWindow._players.get(2))) { onKeyPressed(2); break; }
						default: break;
					}
				}
				
			} catch (Exception ex) {
				
				System.out.println(ex.getMessage());
			}
		});
		
		// fire OK Button if Enter key is pressed
		tfAnswerField.setOnKeyPressed(e -> {
			
			if (e.getCode() == KeyCode.ENTER)
				btnOk.fire();
		});
		
		// -----------------------------------------------------------------------
		// Button action - OK
		// -----------------------------------------------------------------------
		btnOk.setOnAction(e -> {
			
			timer.stop();
			Player p = GameboardWindow._players.get(GameboardWindow.currentPlayerIndex);
			
			// DD type
			if (isDailyDouble()) {
				
				if (isCorrect())
					incrementBalance(p);
				else 
					decrementBalance(p);

				gotoGameBoardWindow();
			
			// REG type
			} else {
				
				if (isCorrect()) {

					incrementBalance(p);
					gotoGameBoardWindow();
					
				// answered wrong
				} else {

					decrementBalance(p);
					imgViewQuestion.setImage(imgSad);

					// if not all players have tried to answer, then repeat question
					if ( !(plyrsNotAnswered.isEmpty()) ) {
						
						disableNodes();
						tfAnswerField.clear();
						txtPlayerBuzzed.setText("");
						startTimer();

					} else
						gotoGameBoardWindow();
				}
			}
		});
		
		// -----------------------------------------------------------------------
		// ProgessBar listener - what to do when timer runs out
		// -----------------------------------------------------------------------
		progressBar.progressProperty().addListener(e -> {

			if (progressBar.getProgress() == 0) {

				timer.stop();
				Player p = GameboardWindow._players.get(GameboardWindow.currentPlayerIndex);
				
				if (isDailyDouble()) {
					
					decrementBalance(p);
					gotoGameBoardWindow();

				} else {
					
					/* if no player buzzed in, then go back to GameboardWindow
					   otherwise decrement balance of player who buzzed in
					   if not all players have tried to answer, then repeat the question */
					if ( !(txtPlayerBuzzed.getText().equals("")) ) {
						
						imgViewQuestion.setImage(imgSad);
						
						if ( !(plyrsNotAnswered.isEmpty()) ) {
							
							// decrement player's balance, update score text
							decrementBalance(p);
							
							// replay question
							disableNodes();
							tfAnswerField.clear();
							txtPlayerBuzzed.setText("");
							startTimer();
							
						} else {

							decrementBalance(p);
							gotoGameBoardWindow();
						}

					} else
						gotoGameBoardWindow();
				}
			}
		});
		
	} // end init
	
	
	// increment player's balance, set player's turn, update score text
	private void incrementBalance(Player p) {
		
		p.setCurrentScore(p.getCurrentScore() + question.getValue());
		p.setNumQuestionsCorrect(p.getNumQuestionsCorrect() + 1);
		
		GameboardWindow.turnIndex = GameboardWindow.currentPlayerIndex;
		PlayerUsernameAndScore.txtPlayerScore[GameboardWindow.turnIndex].setText(String.format("$%,d", p.getCurrentScore()));
	}
	
	// decrement player's balance, set player's turn, update score text
	private void decrementBalance(Player p) {
		
		p.setCurrentScore(p.getCurrentScore() - question.getValue());
		PlayerUsernameAndScore.txtPlayerScore[GameboardWindow.currentPlayerIndex].setText(String.format("$%,d", p.getCurrentScore()));
	}
	
	private void onKeyPressed(int idx) {

		timer.stop();
		startTimer();
		imgViewQuestion.setImage(imgHappy);
		enableNodes();
		plyrsNotAnswered.remove(GameboardWindow._players.get(idx));
		txtPlayerBuzzed.setText(GameboardWindow._players.get(idx).getUsername() + " answered!");
		GameboardWindow.currentPlayerIndex = idx;
	}
	
	// enable these nodes when a player buzzes in
	private void enableNodes() {
		
		btnOk.setDisable(false);
		tfAnswerField.setDisable(false);
		
		if (GameboardWindow.numPlayers != 1)
			txtPlayerBuzzed.setVisible(true);
	}
	
	// reset/disable these nodes
	private void disableNodes() {
		
		btnOk.setDisable(true);
		tfAnswerField.setDisable(true);
		
		if (GameboardWindow.numPlayers != 1)
			txtPlayerBuzzed.setVisible(true);
	}
	
	private void startTimer() {
		
		progressBar.setProgress(1);
		timer.getKeyFrames().add(new KeyFrame(Duration.seconds(15), new KeyValue(progressBar.progressProperty(), 0)));
		timer.play();
	}
	
	private void gotoGameBoardWindow() {
		
		GameboardWindow.questionStage.close();
		Main.getPrimaryStage().show();
	}
	
	private boolean isCorrect() { return tfAnswerField.getText().toLowerCase().equals(question.getAnswer().toLowerCase()); }
	
	private boolean isDailyDouble() { return question.getType().equals("DD"); };
	
	public void answeredCorrectImage() { imgViewQuestion.setImage(new Image("resources/images/trebek_happy.jpg")); }
	
	public void answeredWrongImage() { imgViewQuestion.setImage(new Image("resources/images/trebek_sad.jpg")); }
	
	public Scene getQuestionScene() { return questionScene; }

}
