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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameboardWindow extends BorderPane implements Initializer {

	public Button[][]               tile;
	public Button                   btnQuitGame;
	public GridPane                 gridPane;
	public PlayerUsernameAndScore   playerUsernameAndScore;
	public ArrayList<String>        categories;
	public static ArrayList<Player> _players;
	public static int               numPlayers;
	
	public static Stage             questionStage;
	
	GameboardWindow() {
		
		tile                   = new Button[6][5];
		btnQuitGame            = new Button("Quit Game");
		gridPane               = new GridPane();
		playerUsernameAndScore = new PlayerUsernameAndScore();
		categories             = new ArrayList<>();
		numPlayers             = _players.size();
		questionStage          = new Stage();
		
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
				
				final int _col = col;
				final int _row = row + 1;
				
				// Actions/Listeners -------------------------------------------------------------
				
				tile[col][row].setOnAction((ActionEvent e) -> {
					
					try {
						
						String sql = "SELECT question_ID, category, clue, answer, value, type "
						           + "FROM Questions "
						           + "WHERE category = ? "
						           + "AND value = ?";
						
						PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
						pstmt.setString(1, categories.get(_col));
						pstmt.setInt(2, _row * 100);
						ResultSet rs = pstmt.executeQuery();
						
						Question q = new Question(
						                          rs.getInt("question_ID"), rs.getString("category"), rs.getString("clue"), 
						                          rs.getString("answer"),   rs.getInt("value"),       rs.getString("type"));
						
						questionStage.setScene(new QuestionWindow(q).getQuestionScene());
						questionStage.setTitle("Clue");
						
						// close gameboard stage and display question stage
						Main.getPrimaryStage().close();
						questionStage.show();
						
						// disable button
						tile[_col][_row - 1].setDisable(true);
						
						
					} catch (SQLException sqlex) {
						
						System.out.println(sqlex.getMessage());
						
					}
				});
			}
		}
		
		// add Quit Game button
		gridPane.add(btnQuitGame, 5, 6);
		
		getRandomCategories();
		
		// add GridPane to center section of BorderPane
		this.setCenter(gridPane);
		
		// add playerUsernameAndScore to top section of BorderPane
		this.setTop(playerUsernameAndScore);
		
		// button action - Quit Game
		btnQuitGame.setOnAction((ActionEvent e) -> {
			
			/* TODO update player's info */
			
			Main.gotoPrimaryScene();
			
		});
	}
	
	private void createCategoryBoxes() {
		
		// each category box contains a stackPane, rectangle, and label
		StackPane[] sp   = new StackPane[6];
		Rectangle[] rect = new Rectangle[6];
		Label[]     lbl  = new Label[6];
		
		for (int i = 0; i < 6; i++) {
			
			rect[i] = new Rectangle();
			sp[i]   = new StackPane();
			lbl[i]  = new Label(categories.get(i));
			
			rect[i].setStyle("-fx-fill: #3251fc;");
			
			// set height/width of rectangle/stackpane to tile size
			rect[i].widthProperty().bind(tile[0][1].widthProperty());
			rect[i].heightProperty().bind(tile[0][1].heightProperty());
			sp[i].setPrefWidth(tile[0][1].getWidth());
			sp[i].setPrefHeight(tile[0][1].getHeight());
			
			// set label text properties
			lbl[i].setWrapText(true);
			lbl[i].setTextAlignment(TextAlignment.CENTER);
			lbl[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14.5));
			lbl[i].setTextFill(Color.WHITE);

			// add nodes to stackpane, gridpane
			sp[i].getChildren().addAll(rect[i], lbl[i]);
			gridPane.add(sp[i], i, 0);
		}
	}
	
	
	// inner class to display player username(s) and balance at top of Gameboard
	class PlayerUsernameAndScore extends HBox implements Initializer {
		
		public Label[] lblPlayerUsername;
		public Text[]  txtPlayerScore;
		
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

			String plyr_username;
			int    sze = MainWindow.playersAdded.size();
			
			for (int i = 0; i < sze; i++) {
				
				plyr_username = MainWindow.playersAdded.get(i).toString();
				
				// query player info from database
				try {
					
					String sql = "SELECT player_ID, user_name, high_score, num_games_played, num_questions_correct " 
					           + "FROM Players "
					           + "WHERE user_name = ?";
					
					PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
					pstmt.setString(1, plyr_username);
					ResultSet rs = pstmt.executeQuery();
					
					// create new Player
					Player p = new Player(
							              rs.getInt("player_ID"), plyr_username, rs.getInt("high_score"),
							              rs.getInt("num_games_played"), rs.getInt("num_questions_correct"));
					
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
		
		createCategoryBoxes();
	}

} // end GameboardWindow
