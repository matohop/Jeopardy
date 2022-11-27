package jeopardy;

import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStage;
	private static Scene primaryScene;
	private static Connection connection;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		primaryStage = stage;
		establishConnection(); // to database
		
		// display the stage and scene
		primaryScene = new Scene(new MainWindow(), 340, 210);
		primaryStage.setScene(primaryScene);
		primaryStage.setTitle("Jeopardy");
		primaryStage.show();
	}
	
	// connect to the database
	private void establishConnection() {
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src/resources/jeopardy.db");

		} catch (Exception e) {
			
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0); 
		}
		
		System.out.println("Opened database successfully");
	}

	public static Stage getPrimaryStage() { return primaryStage; }

	public static void setPrimaryStage(Stage primaryStage) { Main.primaryStage = primaryStage; }

	public static Scene getPrimaryScene() { return primaryScene; }

	public void setPrimaryScene(Scene mainScene) { Main.primaryScene = mainScene; }
	
	public static Connection getConnection() { return connection; }

	public static void setConnection(Connection connection) { Main.connection = connection; }
	
}
