package jeopardy;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

public class WagerWindow extends GridPane implements Initializer {
	
	public  Label      lblPrompt;
	public  TextField  tfWagerAmount;
	public  static int wagerAmount;
	public  Button     btnOk;
	private Scene      wagerScene;
	private Question   question;
	
	public WagerWindow(Question q) {
		
		lblPrompt     = new Label("Enter wager amount:");
		tfWagerAmount = new TextField();
		btnOk         = new Button("Ok");
		wagerScene    = new Scene(this);
		question      = q;
		
		// set alignment/spacing
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(11, 12, 11, 12));
		this.setHgap(5);
		this.setVgap(5);
		GridPane.setHalignment(btnOk, HPos.RIGHT);
		
		init();
	}

	@Override
	public void init() {
		
		this.add(lblPrompt, 0, 0);
		this.add(tfWagerAmount, 1, 0);
		this.add(btnOk, 1, 1);
		
		// Button action - OK
		btnOk.setOnAction((ActionEvent e) -> {
			
			try {
				
				int amt = Integer.parseInt(tfWagerAmount.getText());
				
				if (amt >= 200 && amt <= 1000) {
					
					wagerAmount = amt;
					GameboardWindow.wagerStage.close();
					GameboardWindow.questionStage.setScene(new QuestionWindow(question).getQuestionScene());
					GameboardWindow.questionStage.setTitle("Clue");
					GameboardWindow.questionStage.show();
				}
				
			} catch (Exception ex) {
				
				System.out.println(ex.getMessage());
			}
			
		});
		
		// fire OK Button if Enter key is pressed
		tfWagerAmount.setOnKeyPressed(e -> {
			
			if (e.getCode() == KeyCode.ENTER)
				btnOk.fire();
		});
	}
	
	public Scene getWagerScene() { return wagerScene; }

}
