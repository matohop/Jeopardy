package jeopardy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		
		lblUsername = new Label("Username:");
		tfUsername  = new TextField();
		btnSave     = new Button("Save");
		btnCancel   = new Button("Cancel");
		username    = "";
		
		// alignment/spacing
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(11, 12, 11, 12));
		this.setHgap(5);
		this.setVgap(5);
		tfUsername.setPrefWidth(100);
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

		// Actions/Listeners -------------------------------------------------------------

		// Button action - Save
		btnSave.setOnAction((ActionEvent e) -> {
			
			String newUsername = tfUsername.getText().trim();
			
			// creating a new profile
			if (username.equals("")) {
				
				// if username not empty and doesn't exist
				if (!(newUsername.isEmpty()) && !(MainWindow.players.contains(newUsername))) {
					
					// INSERT username into database
					try {
						
						String sql = "INSERT INTO Players(user_name) VALUES(?)";
						PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
						pstmt.setString(1, newUsername);
						pstmt.executeUpdate();
						
						System.out.println(newUsername + " added to database");
						
					} catch (SQLException sqlex) {
			
						System.out.println(sqlex.getMessage());
					}
					
				} else {
					
					System.out.println("Save error");
				}
			
			// editing/updating existing profile
			} else if (!(newUsername.isEmpty()) && !(MainWindow.players.contains(newUsername))) {
				
				// UPDATE username in database
				try {
					
					// retrieve playerID to be updated
					String sql = "SELECT player_ID " 
					           + "FROM Players "
					           + "WHERE user_name = ?";
					
					PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
					pstmt.setString(1, username);
					ResultSet rs = pstmt.executeQuery();
					int playerID = rs.getInt("player_ID");
					
					// update using playerID
					sql = "UPDATE Players "
					    + "SET user_name = ? "
					    + "WHERE player_ID = ?";
					
					pstmt = Main.getConnection().prepareStatement(sql);
					pstmt.setString(1, newUsername);
					pstmt.setInt(2, playerID);
					pstmt.executeUpdate();
					
					System.out.println("player_ID: "      + playerID + 
					                   ", Old username: " + username +
					                   ", New username: " + newUsername);
					
				} catch (SQLException sqlex) {
					
					System.out.println(sqlex.getMessage());
				}
			}
			
			else {
				
				System.out.println("Save error");
			}
			
			MainWindow.populateLvPlayers();
			Main.gotoPrimaryScene();
		});
		
		// Button action - Cancel
		btnCancel.setOnAction((ActionEvent e) -> {

			Main.gotoPrimaryScene();
		});
		
	} // end init

}
