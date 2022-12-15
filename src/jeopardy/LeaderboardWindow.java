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
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class LeaderboardWindow extends GridPane implements Initializer {

	public static Map<Integer, String> players;
	public Button btnBack;
	
	public LeaderboardWindow() {
		
		// initialize variables
		players = new TreeMap<>(Collections.reverseOrder());
		btnBack = new Button("Back");
		
		// get top 10 highest scoring players
		populateLeadingPlayers();
		
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

		Iterator<Map.Entry<Integer, String>> itr = players.entrySet().iterator();

		// add player username, score to nodes
		for (int i = 1; itr.hasNext(); i++) {
			
			Map.Entry<Integer, String> entry = itr.next();
			
			TextField tf = new TextField(String.format("$%,d", entry.getKey()));
			tf.setPrefWidth(100);
			tf.setEditable(false);
			
			this.add(new Label("#" + i + ":"), 0, i);
			this.add(new Label(entry.getValue() + " "), 1, i);
			this.add(tf, 2, i);
		}

		this.add(btnBack, 2, 11);
		
		// Button action - Back
		btnBack.setOnAction((ActionEvent e) -> {
			
			Main.gotoPrimaryScene();
		});
		
	} // end init
	
	// query the leading players and add to TreeMap
	public static void populateLeadingPlayers() {
		
		try {
			
			String sql = "SELECT user_name, high_score " +
			             "FROM Players " +
					     "ORDER BY high_score " +
			             "DESC LIMIT 10";
			
			Statement stmt = Main.getConnection().createStatement();
			ResultSet rs   = stmt.executeQuery(sql);
			
			while (rs.next())
				players.put(rs.getInt("high_score"), rs.getString("user_name"));
			
		} catch (SQLException sqlex) {
			
			System.out.println(sqlex.getMessage());
		}
	}
}
