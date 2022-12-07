package jeopardy;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class WagerWindow extends GridPane implements Initializer {
	
	public Label     lblPrompt;
	public TextField tfWagerAmount;
	public Button    btnOk;
	
	public WagerWindow() {
		
		lblPrompt     = new Label("Enter wager amount: ");
		tfWagerAmount = new TextField();
		btnOk         = new Button("Ok");
		
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
		
		// Button action - Back
		btnOk.setOnAction((ActionEvent e) -> {
			
			int wagerAmount = Integer.parseInt(tfWagerAmount.getText());
			
			
		});
	}

}
