package jeopardy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainWindow extends BorderPane implements Initializer {
	
	public Button btnCreateProfile, btnViewLeaderboard, btnStartGame, btnLvAdd, btnLvRemove;
	public HBox hBoxCenter, hBoxBottom;
	public VBox vBoxLvButtons;
	public Image imgLogo;
	public ImageView imgViewLogo;
	public ListView<String> lvPlayers, lvPlayersAdded;
	public static ObservableList<String> players, playersAdded;
	
	public MainWindow() {

		// Jeopardy logo
		imgLogo     = new Image("resources/images/logo_transparent.png");
		imgViewLogo = new ImageView(imgLogo);
		imgViewLogo.setFitWidth(310);
		imgViewLogo.setFitHeight(40);
		
		// ListViews
		players      = FXCollections.observableArrayList();
		playersAdded = FXCollections.observableArrayList();
		populateLvPlayers();
		
		lvPlayers      = new ListView<>(players);
		lvPlayersAdded = new ListView<>();
		
		lvPlayers.setMaxHeight(100);
		lvPlayersAdded.setMaxHeight(100);
		lvPlayers.setMaxWidth(130);
		lvPlayersAdded.setMaxWidth(130);
		
		// Buttons
		btnCreateProfile   = new Button("Create Profile");
		btnViewLeaderboard = new Button("View Leaderboard");
		btnStartGame       = new Button("Start Game");
		btnLvAdd           = new Button(">");
		btnLvRemove        = new Button("<");
		
		// HBox - for ListViews and VBox
		hBoxCenter = new HBox(10);
		hBoxCenter.setAlignment(Pos.CENTER);
		hBoxCenter.setPadding(new Insets(11, 0, 6, 0));
		
		// HBox - for bottom Buttons
		hBoxBottom = new HBox(10);
		hBoxBottom.setAlignment(Pos.CENTER);
		hBoxBottom.setPadding(new Insets(6, 0, 0, 0));
		
		// VBox - for "<" and ">" Buttons
		vBoxLvButtons = new VBox(10);
		vBoxLvButtons.setAlignment(Pos.CENTER);
		
		// BorderPane alignment/padding
		BorderPane.setAlignment(imgViewLogo, Pos.CENTER);
		BorderPane.setAlignment(hBoxCenter, Pos.CENTER);
		BorderPane.setAlignment(hBoxBottom, Pos.CENTER);
		this.setPadding(new Insets(11, 0, 11, 0));
		
		init();
	}

	@Override
	public void init() {
		
		// add Buttons to VBox
		vBoxLvButtons.getChildren().addAll(btnLvAdd, btnLvRemove);
		
		// add ListViews and VBox to HBox
		hBoxCenter.getChildren().addAll(lvPlayers, vBoxLvButtons, lvPlayersAdded);
		
		// add bottom Buttons
		hBoxBottom.getChildren().addAll(btnCreateProfile, btnViewLeaderboard, btnStartGame);
		
		// add nodes to BorderPane sections
		this.setTop(imgViewLogo);
		this.setCenter(hBoxCenter);
		this.setBottom(hBoxBottom);

		// Button action - Create Profile
		btnCreateProfile.setOnAction((ActionEvent e) -> {

			Main.getPrimaryStage().setScene(new Scene(new ProfileWindow()));
			Main.getPrimaryStage().setTitle("Profile");
		});
		
		// Button action - Start Game
		btnStartGame.setOnAction((ActionEvent e) -> {

			Main.getPrimaryStage().setScene(new Scene(new GameboardWindow(), 747, 820));
			Main.getPrimaryStage().setTitle("Gameboard");
		});
		
		// Button action - View Leaderboard
		btnViewLeaderboard.setOnAction((ActionEvent e) -> {

			Main.getPrimaryStage().setScene(new Scene(new LeaderboardWindow()));
			Main.getPrimaryStage().setTitle("Leaderboard");
		});
		
		// Button action - ">"
		btnLvAdd.setOnAction((ActionEvent e) -> {
			
			// get the player selected
			String player = lvPlayers.getSelectionModel().getSelectedItem();
			
			if (!(player == null)) {
				
				// add player to the playersAdded ArrayList
				playersAdded.add(player);
				
				// add player to ListView of players added
				lvPlayersAdded.setItems(playersAdded);
				
				// remove player from ListView players
				lvPlayers.getItems().remove(player);
			}
		});
		
		// Button action - "<"
		btnLvRemove.setOnAction((ActionEvent e) -> {
			
			// get the player selected
			String player = lvPlayersAdded.getSelectionModel().getSelectedItem();
			
			if (!(player == null)) {
				
				// add player to the players ArrayList
				players.add(player);
				
				// add player to ListView of players
				lvPlayers.setItems(players);
				
				// remove player from ListView players added
				lvPlayersAdded.getItems().remove(player);
			}
		});
		
		// ListView action - double click item
		lvPlayers.setOnMouseClicked((MouseEvent me) -> {
			
			if (me.getClickCount() == 2) {
				
				// retrieve selected player info from database and open in ViewEditProfileWindow
				Main.getPrimaryStage().setScene(new Scene(new ViewEditProfileWindow(lvPlayers.getSelectionModel().getSelectedItem())));
				Main.getPrimaryStage().setTitle("View/Edit Profile");
			}
		});
	
	} // end init
	
	// query usernames from database and add to ArrayList
	public static void populateLvPlayers() {
		
		try {
			
			// empty the ArrayList
			players.removeAll(players);
			
			String    sql  = "SELECT user_name FROM Players";
			Statement stmt = Main.getConnection().createStatement();
			ResultSet rs   = stmt.executeQuery(sql);
			
			while (rs.next())
				players.add(rs.getString("user_name"));
			
		} catch (SQLException sqlex) {
			
			System.out.println(sqlex.getMessage());
		}
	}

}