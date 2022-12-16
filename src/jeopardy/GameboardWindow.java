package jeopardy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	public Text                     txtPlayerTurn;
	public static ArrayList<String> categories;
	public static ArrayList<Player> _players;
	public static int               numPlayers;
	public int                      counter;
	public static int               turnIndex, currentPlayerIndex;
	
	public static Stage             questionStage, wagerStage;
	
	GameboardWindow() {
		
		tile                   = new Button[6][5];
		btnQuitGame            = new Button("Quit Game");
		gridPane               = new GridPane();
		playerUsernameAndScore = new PlayerUsernameAndScore();
		txtPlayerTurn          = new Text();
		categories             = new ArrayList<>();
		numPlayers             = _players.size();
		questionStage          = new Stage();
		wagerStage             = new Stage();
		counter                = 0;
		turnIndex              = 0;
		
		txtPlayerTurn.setFont(Font.font("ITC Korinna", FontWeight.BOLD, 18));
		txtPlayerTurn.setFill(Color.GREEN);
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
						
						// if Daily Double
						if (q.getType().equals("DD")) {

							wagerStage.setScene(new WagerWindow(q).getWagerScene());
							wagerStage.setTitle("Daily Double");
							Main.getPrimaryStage().close();
							wagerStage.show();
						}
						
						else {

							questionStage.setScene(new QuestionWindow(q).getQuestionScene());
							questionStage.setTitle("Clue");
							
							// close Gameboard Stage and display Question Stage
							Main.getPrimaryStage().close();
							questionStage.show();
						}
						
						// disable button, increment counter
						tile[_col][_row - 1].setDisable(true);
						counter++;
						
						// Game over
						if (counter == 30) {
							
							updatePlayerStats();
							Main.gotoPrimaryScene();
						}

					} catch (SQLException sqlex) {
						
						System.out.println(sqlex.getMessage());
						
					}
				});
			}
		}
		
		// add Quit Game button and flashing text
		gridPane.add(btnQuitGame, 5, 6);
		gridPane.add(txtPlayerTurn, 3, 6);
		
		getRandomCategories();
		
		// add GridPane to center section of BorderPane
		this.setCenter(gridPane);
		
		// add playerUsernameAndScore to top section of BorderPane
		this.setTop(playerUsernameAndScore);
		
		// button action - Quit Game
		btnQuitGame.setOnAction((ActionEvent e) -> {
			
			// stop animation thread
			PlayerUsernameAndScore.isInProgress = false;
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
			
			// set height/width of rectangle/stackpane
			rect[i].widthProperty().bind(tile[0][1].widthProperty());
			rect[i].setHeight(80);
			sp[i].setPrefWidth(tile[0][1].getWidth());
			sp[i].setPrefHeight(rect[i].getHeight());
			
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
			while (rs.next())
				categories.add(rs.getString("category_name"));
			System.out.println(categories);
				
		} catch (SQLException sqlex) {
				
			System.out.println(sqlex.getMessage());
		}
		
		createCategoryBoxes();
	}
	
	private void updatePlayerStats() {
		
		for (int i = 0; i < _players.size(); i++) {
			
			try {
				
				// update profile statistics
				String sql = "UPDATE Players "
				           + "SET high_score = ?, num_games_played = ?, num_questions_correct = ? "
				           + "WHERE player_ID = " + _players.get(i).getPlayerID();
				
				PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
				
				if (_players.get(i).getCurrentScore() > _players.get(i).getHighScore())
					pstmt.setInt(1, _players.get(i).getCurrentScore());
				else
					pstmt.setInt(1, _players.get(i).getHighScore());
				
				pstmt.setInt(2, _players.get(i).getNumGamesPlayed() + 1);
				pstmt.setInt(3, _players.get(i).getNumQuestionsCorrect());
				pstmt.executeUpdate();
				
			} catch (SQLException sqlex) {
				
				System.out.println(sqlex.getMessage());
			}
			
			System.out.println("Player stats updated");
		}
	}
	
	// -----------------------------------------------------------------------
	// inner class to display player username(s) and balance at top of Gameboard
	// -----------------------------------------------------------------------
	class PlayerUsernameAndScore extends HBox implements Initializer {
		
		public VBox[]           vBoxPresentPlayer;
		public Label[]          lblPlayerUsername;
		public static Text[]    txtPlayerScore;
		public String           flashingTxt;
		static volatile boolean isInProgress;
		
		PlayerUsernameAndScore() {
			
			_players          = new ArrayList<>();
			vBoxPresentPlayer = new VBox[MainWindow.playersAdded.size()];
			lblPlayerUsername = new Label[MainWindow.playersAdded.size()];
			txtPlayerScore    = new Text[MainWindow.playersAdded.size()];
			isInProgress      = true;
			
			// alignment/padding/spacing
			this.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(10));
			this.setSpacing(10);
			this.setStyle("-fx-border-color: black");
			
			init();
		}
		
		@Override
		public void init() {
			
			getCurrentPlayers();
			
			// create label for username, text for score
			for (int i = 0; i < _players.size(); i++) {
				
				lblPlayerUsername[i] = new Label(_players.get(i).getUsername());
				txtPlayerScore[i]    = new Text("$0");
				vBoxPresentPlayer[i] = new VBox(lblPlayerUsername[i], txtPlayerScore[i]);
				
				// set font/color/alignment
				lblPlayerUsername[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 20));
				txtPlayerScore[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14));
				txtPlayerScore[i].setFill(Color.GREEN);
				vBoxPresentPlayer[i].setAlignment(Pos.CENTER);
				vBoxPresentPlayer[i].setPrefWidth(100);
				
				// add label/text nodes to HBox
				this.getChildren().add(vBoxPresentPlayer[i]);
			}
			
			// start flashing current player
			try {
				
				startFlashingAnimation();
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
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
		
		// flash current player
		private void startFlashingAnimation() throws InterruptedException {
		
			new Thread(() -> {
		
				while (isInProgress) {

					if (txtPlayerTurn.getText().equals(""))
						flashingTxt = _players.get(turnIndex).getUsername();
					else
						flashingTxt = "";

					Platform.runLater(() -> txtPlayerTurn.setText(flashingTxt));
					
					try {
						
						Thread.sleep(500);
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
			}).start();
		}
		
	} // end PlayerUsernameAndScore

} // end GameboardWindow
