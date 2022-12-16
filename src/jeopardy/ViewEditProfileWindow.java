package jeopardy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class ViewEditProfileWindow extends ProfileWindow {
	
	public Label     lblHighScore, lblnNumGamesPlayed, lblAnsweredCorrect;
	public Text      txtHighScore, txtNumGamesPlayed, txtAnsweredCorrect;
	public Button    btnEdit, btnDelete;
	
	public ViewEditProfileWindow(String _username) {
		
		tfUsername.setText(_username);
		username = _username;
		
		lblHighScore       = new Label("High Score: ");
		lblnNumGamesPlayed = new Label("Games Played: ");
		lblAnsweredCorrect = new Label("Answered Correct: ");

		txtHighScore       = new Text();
		txtNumGamesPlayed  = new Text();
		txtAnsweredCorrect = new Text();
		
		btnEdit   = new Button("Edit");
		btnDelete = new Button("Delete");
		
		_init(_username);
		
	}

	public void _init(String _username) {
		
		// disable these nodes
		tfUsername.setDisable(true);
		btnSave.setDisable(true);
		btnDelete.setDisable(true);

		this.add(lblHighScore, 0, 1);
		this.add(txtHighScore, 1, 1);
		this.add(lblnNumGamesPlayed, 0, 2);
		this.add(txtNumGamesPlayed, 1, 2);
		this.add(lblAnsweredCorrect, 0, 3);
		this.add(txtAnsweredCorrect, 1, 3);
		this.add(btnEdit, 2, 4);
		this.add(btnDelete, 2, 0);
		
		// query player info from database into TextFields
		try {
			
			String sql = "SELECT user_name, high_score, num_games_played, num_questions_correct " 
			           + "FROM Players "
			           + "WHERE user_name = ?";
			
			PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
			pstmt.setString(1, _username);
			ResultSet rs = pstmt.executeQuery();
			
			txtHighScore.setText("  " + Integer.toString(rs.getInt("high_score")));
			txtNumGamesPlayed.setText("  " + Integer.toString(rs.getInt("num_games_played")));
			txtAnsweredCorrect.setText("  " + Integer.toString(rs.getInt("num_questions_correct")));
			
		} catch (SQLException e) {
		
			System.out.println(e.getMessage());
		}
		
		// Actions/Listeners -------------------------------------------------------------

		// Button action - Edit
		btnEdit.setOnAction((ActionEvent e) -> {
			
			tfUsername.setDisable(false);
			btnSave.setDisable(false);
			btnDelete.setDisable(false);
		});
		
		// Button action - Delete
		btnDelete.setOnAction((ActionEvent e) -> {
			
			try {
				
				// get playerID to delete
				String sql = "SELECT player_ID "
				           + "FROM Players "
				           + "WHERE user_name = ?";
				
				PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
				pstmt.setString(1, _username);
				ResultSet rs = pstmt.executeQuery();
				int playerID = rs.getInt("player_ID");
				
				// delete the player from database
				sql = "DELETE FROM Players "
				    + "WHERE player_ID = " + playerID;

				pstmt = Main.getConnection().prepareStatement(sql);
				pstmt.executeUpdate();
				
				System.out.println(_username + " removed from database");
				
				// refresh the ListView and go back to MainWindow
				MainWindow.populateLvPlayers();
				Main.gotoPrimaryScene();
				
			} catch (SQLException sqlex) {
				
				System.out.println(sqlex.getMessage());
			}
		});

	} // end _init
}
