package jeopardy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.Observable;
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

public class MainWindow extends BorderPane implements Initializer {
	
	private Button 	  btnCreateProfile, btnViewLeaderboard, btnStartGame, btnLvAdd;
	private HBox 	  hBoxCenter, hBoxBottom;
	private Image 	  imgLogo;
	private ImageView imgVLogo;
	private ListView<String> lvPlayers, lvPlayersAdded;
	private static ObservableList<String> players;
	
	public MainWindow() {
		init();
	}

	@Override
	public void init() {
		
		// buttons
		btnCreateProfile   = new Button("Create Profile");
		btnViewLeaderboard = new Button("View Leaderboard");
		btnStartGame 	   = new Button("Start Game");
		btnLvAdd 		   = new Button(">");
		
		// listviews
		players = FXCollections.observableArrayList();
		populateLvPlayers();
		lvPlayers = new ListView<>(players);
		lvPlayersAdded = new ListView<>();
		lvPlayers.setMaxHeight(100);
		lvPlayersAdded.setMaxHeight(100);
		lvPlayers.setMaxWidth(130);
		lvPlayersAdded.setMaxWidth(130);
		
		// jeopardy logo
		imgLogo	 = new Image("jeopardy/images/logo_transparent.png");
		imgVLogo = new ImageView(imgLogo);
		imgVLogo.setFitWidth(310);
		imgVLogo.setFitHeight(40);
		
		// hbox for listviews
		hBoxCenter = new HBox(10);
		hBoxCenter.getChildren().add(lvPlayers);
		hBoxCenter.getChildren().add(btnLvAdd);
		hBoxCenter.getChildren().add(lvPlayersAdded);
		hBoxCenter.setAlignment(Pos.CENTER);
		hBoxCenter.setPadding(new Insets(11, 0, 6, 0));
		
		// hbox for buttons
		hBoxBottom = new HBox(10);
		hBoxBottom.getChildren().add(btnCreateProfile);
		hBoxBottom.getChildren().add(btnViewLeaderboard);
		hBoxBottom.getChildren().add(btnStartGame);
		hBoxBottom.setAlignment(Pos.CENTER);
		hBoxBottom.setPadding(new Insets(6, 0, 0, 0));
		
		// set borderpane node positions and alignment
		this.setTop(imgVLogo);
		this.setCenter(hBoxCenter);
		this.setBottom(hBoxBottom);
		this.setPadding(new Insets(11, 0, 11, 0));
		BorderPane.setAlignment(imgVLogo, Pos.CENTER);
		BorderPane.setAlignment(hBoxCenter, Pos.CENTER);
		BorderPane.setAlignment(hBoxBottom, Pos.CENTER);
		
		// button action - create profile
		btnCreateProfile.setOnAction((ActionEvent e) -> {
			System.out.println("Create Profile button clicked");
			Main.getPrimaryStage().setScene(new Scene(new ProfileWindow()));
			Main.getPrimaryStage().setTitle("Profile");
		});
		
		// button action - start game
		btnStartGame.setOnAction((ActionEvent e) -> {
			System.out.println("Start Game button clicked");
		});
		
		// button action - view leaderboard
		btnViewLeaderboard.setOnAction((ActionEvent e) -> {
			System.out.println("View Leaderboard button clicked");
			Main.getPrimaryStage().setScene(new Scene(new LeaderboardWindow()));
			Main.getPrimaryStage().setTitle("Leaderboard");
		});
		
		// button action - ">"
		btnLvAdd.setOnAction((ActionEvent e) -> {
			System.out.println("Add player button clicked");
		});

		// listview action - selected item
		lvPlayers.getSelectionModel().selectedItemProperty().addListener((Observable ov) -> {
			System.out.println("Selected indices: " + lvPlayers.getSelectionModel().getSelectedIndices());
			System.out.println("Selected items: " + lvPlayers.getSelectionModel().getSelectedItem());
		});
		
		// listview action - double click item
		lvPlayers.setOnMouseClicked((MouseEvent me) -> {
			if (me.getClickCount() == 2) {
				// retrieve selected player info from database and open in ProfileWindow
				System.out.println(lvPlayers.getSelectionModel().getSelectedItem() + " double clicked");
			}
		});

		
	} // end init()
	
	private void populateLvPlayers() {
		
		try {
			String    sql  = "SELECT userName FROM Players";
			Statement stmt = Main.getConnection().createStatement();
			ResultSet rs   = stmt.executeQuery(sql);
			
			while (rs.next())
				players.add(rs.getString("userName"));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public Button getBtnCreateProfile() {
		return btnCreateProfile; 
	}

	public void setBtnCreateProfile(Button btnCreateProfile) {
		this.btnCreateProfile = btnCreateProfile; 
	}

	public Button getBtnViewLeaderboard() {
		return btnViewLeaderboard;
	}

	public void setBtnViewLeaderboard(Button btnViewLeaderboard) {
		this.btnViewLeaderboard = btnViewLeaderboard;
	}

	public Button getBtnStartGame() {
		return btnStartGame;
	}

	public void setBtnStartGame(Button btnStartGame) {
		this.btnStartGame = btnStartGame;
	}

	public Button getBtnLvAdd() {
		return btnLvAdd;
	}

	public void setBtnLvAdd(Button btnLvAdd) {
		this.btnLvAdd = btnLvAdd;
	}

	public ListView<String> getLvPlayers() {
		return lvPlayers;
	}

	public void setLvPlayers(ListView<String> lvPlayersToAdd) {
		this.lvPlayers = lvPlayersToAdd;
	}

	public ListView<String> getLvPlayersAdded() {
		return lvPlayersAdded;
	}

	public void setLvPlayersAdded(ListView<String> lvPlayersAdded) {
		this.lvPlayersAdded = lvPlayersAdded;
	}

	public static ObservableList<String> getPlayers() {
		return players;
	}

	public static void setListViewItems(ObservableList<String> players) {
		MainWindow.players = players;
	}

	public HBox gethBoxCenter() {
		return hBoxCenter;
	}

	public void sethBoxCenter(HBox hBoxCenter) {
		this.hBoxCenter = hBoxCenter;
	}

	public HBox gethBoxBottom() {
		return hBoxBottom;
	}

	public void sethBoxBottom(HBox hBoxBottom) {
		this.hBoxBottom = hBoxBottom;
	}

	public Image getImgLogo() {
		return imgLogo;
	}

	public void setImgLogo(Image imgLogo) {
		this.imgLogo = imgLogo;
	}

	public ImageView getImgVLogo() {
		return imgVLogo;
	}

	public void setImgVLogo(ImageView imgVLogo) {
		this.imgVLogo = imgVLogo;
	}

}