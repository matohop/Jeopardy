package jeopardy;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LeaderboardWindow extends GridPane implements Initializer {
	
	public Label     lblOne, lblTwo, lblThree, lblFour, lblFive, lblSix, lblSeven, lblEight, lblNine, lblTen;
	public TextField tfOne, tfTwo, tfThree, tfFour, tfFive, tfSix, tfSeven, tfEight, tfNine, tfTen;
	public Button    btnBack;

	public LeaderboardWindow() {
		
		// Labels and Button
		lblOne 	 = new Label("#1:");
		lblTwo 	 = new Label("#2:");
		lblThree = new Label("#3:");
		lblFour  = new Label("#4:");
		lblFive  = new Label("#5:");
		lblSix 	 = new Label("#6:");
		lblSeven = new Label("#7:");
		lblEight = new Label("#8:");
		lblNine  = new Label("#9:");
		lblTen 	 = new Label("#10:");
		btnBack  = new Button("Back");
		
		// TextFields
		tfOne 	= new TextField();
		tfTwo 	= new TextField();
		tfThree = new TextField();
		tfFour 	= new TextField();
		tfFive 	= new TextField();
		tfSix 	= new TextField();
		tfSeven = new TextField();
		tfEight = new TextField();
		tfNine 	= new TextField();
		tfTen 	= new TextField();
		
		// set alignment/spacing
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(11, 12, 11, 12));
		this.setHgap(5);
		this.setVgap(5);
		GridPane.setHalignment(btnBack, HPos.RIGHT);
				
		init();
	}
	
	@Override
	public void init() {
		
		// add nodes to GridPane
		this.add(lblOne, 0, 0);
		this.add(tfOne, 1, 0);
		this.add(lblTwo, 0, 1);
		this.add(tfTwo, 1, 1);
		this.add(lblThree, 0, 2);
		this.add(tfThree, 1, 2);
		this.add(lblFour, 0, 3);
		this.add(tfFour, 1, 3);
		this.add(lblFive, 0, 4);
		this.add(tfFive, 1, 4);
		this.add(lblSix, 0, 5);
		this.add(tfSix, 1, 5);
		this.add(lblSeven, 0, 6);
		this.add(tfSeven, 1, 6);
		this.add(lblEight, 0, 7);
		this.add(tfEight, 1, 7);
		this.add(lblNine, 0, 8);
		this.add(tfNine, 1, 8);
		this.add(lblTen, 0, 9);
		this.add(tfTen, 1, 9);
		this.add(btnBack, 1, 10);
		
		// disable TextFields
		for (Node child : this.getChildren()) {
			if (child instanceof TextField)
				child.setDisable(true);
		}

		// Button action - Back
		btnBack.setOnAction((ActionEvent e) -> {

			Main.gotoPrimaryScene();
		});
		
	} // end init

}
