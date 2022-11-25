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

	public Label     lblUsername;
	public TextField tfUsername;
	public Button    btnSave, btnCancel;
	public String    username;
	
	public ProfileWindow() {
		
		// Label
		lblUsername = new Label("Username:");
		
		// TextField
		tfUsername = new TextField();
		
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
		
		// add nodes to GriPane
		this.add(lblUsername, 0, 0);
		this.add(tfUsername, 1, 0);
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
					String sql = "INSERT INTO Players(user_name) VALUES(?)";
					PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
					pstmt.setString(1, username);
					pstmt.executeUpdate();
					
				} catch(SQLException sqlex) {
					System.out.println(sqlex.getMessage());
				}
			}
			
			gotoPrimaryScene();
		});
		
		// Button action - Cancel
		btnCancel.setOnAction((ActionEvent e) -> {
			
			System.out.println("Cancel button clicked");
			gotoPrimaryScene();
		});
		
	} // end init()

	private void gotoPrimaryScene() {
		Main.getPrimaryStage().setScene(Main.getPrimaryScene());
		Main.getPrimaryStage().setTitle("Jeopardy");
	}

}
