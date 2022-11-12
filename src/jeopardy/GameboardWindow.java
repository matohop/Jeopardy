package jeopardy;

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
	public GridPane gridPane;
	public PlayerUsernameAndScore playerUsernameAndScore;
	
	GameboardWindow() {
		
		tile = new Button[6][5];
		gridPane = new GridPane();
		playerUsernameAndScore = new PlayerUsernameAndScore();
		
		gridPane.setHgap(3);
		gridPane.setVgap(3);
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
		
		// add GridPane to center section of BorderPane
		this.setCenter(gridPane);
		
		// add playerUsernameAndScore to top section of BorderPane
		this.setTop(playerUsernameAndScore);
	}
	
	
	// inner class to display player username(s) and balance at top of Gameboard
	class PlayerUsernameAndScore extends HBox implements Initializer {
		
		public Label[] lblPlayerUsername;
		public Text[]  txtPlayerScore;
		
		PlayerUsernameAndScore() {
			
			lblPlayerUsername = new Label[3]; // hardcoded size for testing
			txtPlayerScore = new Text[3];  // hardcoded size for testing
			
			// alignment/padding/spacing
			this.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(10));
			this.setSpacing(50);
			
			init();
		}
		
		@Override
		public void init() {
			
			// create label for username, text for score
			for (int i = 0; i < lblPlayerUsername.length; i++) {
				
				lblPlayerUsername[i] = new Label("Test"); // hardcoded username for testing
				txtPlayerScore[i] = new Text("$" + Integer.toString(0)); // hardcoded balance for testing
				
				// set font/color
				lblPlayerUsername[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 20));
				txtPlayerScore[i].setFont(Font.font("ITC Korinna", FontWeight.BOLD, 14));
				txtPlayerScore[i].setFill(Color.GREEN);
				
				// add label/text nodes to HBox
				this.getChildren().addAll(lblPlayerUsername[i], txtPlayerScore[i]);
			}	
		}
		
	} // end PlayerUsernameAndScore

} // end GameboardWindow
