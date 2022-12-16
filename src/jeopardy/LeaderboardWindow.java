package jeopardy;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LeaderboardWindow extends GridPane implements Initializer {

	private Button    btnBack;
	private ResultSet resultSet;
	
	public LeaderboardWindow() {
		
		// get top 10 highest scoring players
		populateLeadingPlayers();
		
		// set alignment/spacing
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(11, 12, 11, 12));
		this.setHgap(5);
		this.setVgap(5);
		
		btnBack = new Button("Back");
		GridPane.setHalignment(btnBack, HPos.RIGHT);
		
		init();
	}
	
	@Override
	public void init() {
		
		try {
			
			// add player username, score to nodes
			for (int i = 1; resultSet.next(); i++) {
				
				TextField tf = new TextField(String.format("$%,d", resultSet.getInt("high_score")));
				tf.setPrefWidth(100);
				tf.setEditable(false);
				
				this.add(new Label("#" + i + ":"), 0, i);
				this.add(new Label(resultSet.getString("user_name")), 1, i);
				this.add(tf, 2, i);
			}
			
		} catch (SQLException sqlex) {
			
			System.out.println(sqlex.getMessage());
		}

		this.add(btnBack, 2, 11);
		
		// Button action - Back
		btnBack.setOnAction((ActionEvent e) -> {
			
			Main.gotoPrimaryScene();
		});
		
	} // end init
	
	// query the leading players and add to TreeMap
	private void populateLeadingPlayers() {
		
		try {
			
			String sql = "SELECT user_name, high_score "
			           + "FROM Players "
			           + "WHERE high_score != 0 "
			           + "ORDER BY high_score "
			           + "DESC LIMIT 10";
			
			Statement stmt = Main.getConnection().createStatement();
			resultSet      = stmt.executeQuery(sql);

			
		} catch (SQLException sqlex) {
			
			System.out.println(sqlex.getMessage());
		}
	}
}
