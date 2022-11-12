package jeopardy;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
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

public class QuestionWindow extends VBox implements Initializer {
	
	public Question    question;
	public Text        txtClue, txtPlayerBuzzed;
	public TextField   tfAnswerField;
	public Button      btnOK;
	public ImageView   imgViewQuestion;
	public ProgressBar progressBar;
	public Timeline    timer;
	
	QuestionWindow() {
		
		txtClue = new Text();
		txtClue.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 15));
		
		txtPlayerBuzzed = new Text();
		txtPlayerBuzzed.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14));

		tfAnswerField = new TextField();
		btnOK = new Button("OK");
		imgViewQuestion = new ImageView(new Image("resources/images/trebek_happy.jpg"));
		progressBar = new ProgressBar(0);
		timer = new Timeline();
		
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
		
		// set ProgressBar width, rotate 180 to simulate a countdown
		progressBar.prefWidthProperty().bind(this.widthProperty());
		progressBar.setRotate(180);
		
		// start countdown
		startTimer();
		

		// Actions/Listeners -------------------------------------------------------------
		
		// player buzzes in
		Main.getPrimaryScene().setOnKeyPressed(e -> {
			
			try {
				
				switch(e.getCode()) {
				
					case SHIFT: /* player 1 do stuff */ break;
					case SPACE: /* player 2 do stuff */ break;
					case ENTER: /* player 3 do stuff */ break;
					
					default: break;
				}
				
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
			
			// if answered correct
			if (tfAnswerField.getText().toLowerCase().equals(question.getAnswer())) {
				
				/* increment player's balance
				   set player's turn */
				
			// answered wrong
			} else {
				
				/* decrement player's balance
				   if not all players have tried to answer, then repeat question
				   otherwise go back to GameboardWindow */
			}
		});
		
		// ProgessBar listener - what to do when timer runs out
		progressBar.progressProperty().addListener(e -> {
			
			// if timer runs out
			if (progressBar.getProgress() == 1) {
				
				/* if no player buzzed in, then go back to GameboardWindow
				   otherwise decrement balance of player who buzzed in
				   if not all players have tried to answer, then repeat the question */
			}
		});
		
	} // end init()
	
	// enable these nodes when a player buzzes in
	public void enableNodes() {
		
		btnOK.setDisable(false);
		tfAnswerField.setDisable(false);
		txtPlayerBuzzed.setVisible(true);
	}
	
	public void startTimer() {
		
		timer.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new KeyValue(progressBar.progressProperty(), 1)));
		timer.play();
	}
	
	public void answeredCorrectImage() { imgViewQuestion.setImage(new Image("resources/images/trebek_happy.jpg")); }
	
	public void answeredWrongImage() { imgViewQuestion.setImage(new Image("resources/images/trebek_sad.jpg")); }

}
