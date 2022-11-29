package jeopardy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameboardWindow extends BorderPane implements Initializer {

	public Button[][] tile;
	public Button btnQuitGame;
	public GridPane gridPane;
	public PlayerUsernameAndScore playerUsernameAndScore;
	public ArrayList<String> categories;
	
	GameboardWindow() {
		
		tile = new Button[6][5];
		btnQuitGame = new Button("Quit Game");
		gridPane = new GridPane();
		playerUsernameAndScore = new PlayerUsernameAndScore();
		categories = new ArrayList<>();
		
		gridPane.setHgap(3);
		gridPane.setVgap(3);
		GridPane.setHalignment(btnQuitGame, HPos.RIGHT);
		this.setPadding(new Insets(3, 3, 3, 3));
		
		init();
	}

	@Override
	public void init() {
		
		for (int col = 0; col < 6; col++) {
			for (int row = 0; row < 5; row++) {
				
				// create tile with monetary value and set size/font/color
				tile[col][row] = new Button("$" + ((row + 1) * 100));
				tile[col][row].setMinSize(120, 120);
				tile[col][row].setStyle("-fx-base: #3251fc; -fx-font-size: 15; -fx-font-weight: bold;");
				tile[col][row].setTextFill(Color.YELLOW);
				
				// set size of grid section to size of tile
				GridPane.setFillHeight(tile[col][row], true);
				GridPane.setFillWidth(tile[col][row], true);
				
				// add tile to grid section
				gridPane.add(tile[col][row], col, row + 1);
			}
		}
		
		// add Quit Game button
		gridPane.add(btnQuitGame, 5, 6);
		
		// add GridPane to center section of BorderPane
		this.setCenter(gridPane);
		
		// add playerUsernameAndScore to top section of BorderPane
		this.setTop(playerUsernameAndScore);
		
		getRandomCategories();
		
		// button action - Quit Game
		btnQuitGame.setOnAction((ActionEvent e) -> {
			
			/* TODO update player's info */
			
			Main.gotoPrimaryScene();
			
		});
	}
	
	
	// inner class to display player username(s) and balance at top of Gameboard
	class PlayerUsernameAndScore extends HBox implements Initializer {
		
		public Label[] lblPlayerUsername;
		public Text[] txtPlayerScore;
		public ArrayList<Player> _players;
		
		PlayerUsernameAndScore() {
			
			_players          = new ArrayList<>();
			lblPlayerUsername = new Label[MainWindow.playersAdded.size()];
			txtPlayerScore    = new Text[MainWindow.playersAdded.size()];
			
			// alignment/padding/spacing
			this.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(10));
			this.setSpacing(50);
			
			init();
		}
		
		@Override
		public void init() {
			
			getCurrentPlayers();
			
			// create label for username, text for score
			for (int i = 0; i < _players.size(); i++) {
				
				lblPlayerUsername[i] = new Label(_players.get(i).getUsername());
				txtPlayerScore[i]    = new Text("$" + Integer.toString(_players.get(i).getCurrentScore()));
				
				// set font/color
				lblPlayerUsername[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 20));
				txtPlayerScore[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14));
				txtPlayerScore[i].setFill(Color.GREEN);
				
				// add label/text nodes to HBox
				this.getChildren().addAll(lblPlayerUsername[i], txtPlayerScore[i]);
			}	
		}
		
		private void getCurrentPlayers() {
			
			int    plyr_ID, plyr_high_score, plyr_num_games, plyr_num_correct;
			String plyr_username;
			
			for (int i = 0; i < MainWindow.playersAdded.size(); i++) {
				
				plyr_username = MainWindow.playersAdded.get(i).toString();
				
				// query player info from database
				try {
					
					String sql = "SELECT player_ID, user_name, high_score, num_games_played, num_questions_correct " 
					           + "FROM Players "
					           + "WHERE user_name = ?";
					
					PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
					pstmt.setString(1, plyr_username);
					ResultSet rs = pstmt.executeQuery();
					
					plyr_ID          = rs.getInt("player_ID");
					plyr_high_score  = rs.getInt("high_score");
					plyr_num_games   = rs.getInt("num_games_played");
					plyr_num_correct = rs.getInt("num_questions_correct");
					
					// create new Player
					Player p = new Player(plyr_ID, plyr_username, plyr_high_score, plyr_num_games, plyr_num_correct);
					
					_players.add(p);
					
				} catch (SQLException e) {
				
					System.out.println(e.getMessage());
				}
				
			}
		}
		
	} // end PlayerUsernameAndScore
	
	// get 6 random and unique categories
	private void getRandomCategories() {

		try {
			
			String sql = "SELECT category_name "
			           + "FROM Categories "
			           + "ORDER BY random() "
			           + "LIMIT 6";
			
			Statement stmt = Main.getConnection().createStatement();
			ResultSet rs   = stmt.executeQuery(sql);
			
			System.out.print("6 random and unique categories: ");
			
			while (rs.next()) {
			
				categories.add(rs.getString("category_name"));
			}
			
			System.out.println(categories);
			
		} catch (SQLException sqlex) {
			
			System.out.println(sqlex.getMessage());
		}
	}

} // end GameboardWindow
