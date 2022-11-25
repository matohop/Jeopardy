package jeopardy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ViewEditProfileWindow extends ProfileWindow {
	
	public Label     lblHighScore, lblnNumGamesPlayed, lblAnsweredCorrect;
	public TextField tfHighScore, tfNumGamesPlayed, tfAnsweredCorrect;
	public Button    btnEdit;
	
	public ViewEditProfileWindow(String username) {
		
		tfUsername.setText(username);
		
		lblHighScore = new Label("High Score:");
		lblnNumGamesPlayed = new Label("Games Played:");
		lblAnsweredCorrect = new Label("Answered Correct:");

		tfHighScore = new TextField();
		tfNumGamesPlayed = new TextField();
		tfAnsweredCorrect = new TextField();
		
		btnEdit = new Button("Edit");
		
		_init(username);
		
	}

	public void _init(String username) {
		
		// disable these TextFields
		tfUsername.setDisable(true);
		tfHighScore.setDisable(true);
		tfNumGamesPlayed.setDisable(true);
		tfAnsweredCorrect.setDisable(true);

		this.add(lblHighScore, 0, 1);
		this.add(tfHighScore, 1, 1);
		this.add(lblnNumGamesPlayed, 0, 2);
		this.add(tfNumGamesPlayed, 1, 2);
		this.add(lblAnsweredCorrect, 0, 3);
		this.add(tfAnsweredCorrect, 1, 3);
		this.add(btnEdit, 2, 4);
		
		// query player info from database into TextFields
		try {
			
			String sql = "SELECT user_name, high_score, num_games_played, num_questions_correct " 
					   + "FROM Players "
					   + "WHERE user_name = ?";
			
			PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			
			tfHighScore.setText(Integer.toString(rs.getInt("high_score")));
			tfNumGamesPlayed.setText(Integer.toString(rs.getInt("num_games_played")));
			tfAnsweredCorrect.setText(Integer.toString(rs.getInt("num_questions_correct")));
			
		} catch (SQLException e) {
		
			System.out.println(e.getMessage());
		}
		
		// Button action - Edit
		btnEdit.setOnAction((ActionEvent e) -> {
			
			tfUsername.setDisable(false);
		});

	} // end _init()
}
