package jeopardy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ProfileWindow extends GridPane implements Initializer {

	public Label     lblUsername, lblHighScore, lblnNumGamesPlayed, lblAnsweredCorrect;
	public TextField tfUsername, tfHighScore, tfNumGamesPlayed, tfAnsweredCorrect;
	public Button    btnSave, btnCancel;
	
	public ProfileWindow() {
		
		// Labels
		lblUsername = new Label("Username:");
		lblHighScore = new Label("High Score:");
		lblnNumGamesPlayed = new Label("Games Played:");
		lblAnsweredCorrect = new Label("Answered Correct:");
		
		// TextFields
		tfUsername = new TextField();
		tfHighScore = new TextField();
		tfNumGamesPlayed = new TextField();
		tfAnsweredCorrect = new TextField();
		
		// Buttons
		btnSave = new Button("Save");
		btnCancel = new Button("Cancel");
		
		// alignment/spacing
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(11, 12, 11, 12));
		this.setHgap(5);
		this.setVgap(5);
		GridPane.setHalignment(btnSave, HPos.RIGHT);
		
		init();
	}

	@Override
	public void init() {
		
		// disable these TextFields
		tfHighScore.setDisable(true);
		tfNumGamesPlayed.setDisable(true);
		tfAnsweredCorrect.setDisable(true);
		
		// add nodes to GriPane
		this.add(lblUsername, 0, 0);
		this.add(tfUsername, 1, 0);
		this.add(lblHighScore, 0, 1);
		this.add(tfHighScore, 1, 1);
		this.add(lblnNumGamesPlayed, 0, 2);
		this.add(tfNumGamesPlayed, 1, 2);
		this.add(lblAnsweredCorrect, 0, 3);
		this.add(tfAnsweredCorrect, 1, 3);
		this.add(btnCancel, 0, 4);
		this.add(btnSave, 1, 4);

		// Button action - Save
		btnSave.setOnAction((ActionEvent e) -> {
			String username = tfUsername.getText();
			
			if (!username.equals("")) {
				MainWindow.players.add(username);
				System.out.println(username + " added");
				
				// insert username into database
				try {
					String sql = "INSERT INTO Players(userName) VALUES(?)";
					PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
					pstmt.setString(1, username);
					pstmt.executeUpdate();
					
				} catch(SQLException sqlex) {
					System.out.println(sqlex.getMessage());
				}
			}
			
			backToMainScene();
		});
		
		// Button action - Cancel
		btnCancel.setOnAction((ActionEvent e) -> {
			System.out.println("Cancel button clicked");
			backToMainScene();
		});
		
	} // end init()

	private void backToMainScene() {
		Main.getPrimaryStage().setScene(Main.getMainScene());
		Main.getPrimaryStage().setTitle("Jeopardy");
	}

}
