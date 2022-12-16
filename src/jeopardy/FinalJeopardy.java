package jeopardy;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinalJeopardy extends VBox implements Initializer{

    public Question     q;
    public TextField    tfAnswerField;
    public Button       btnOk;
    public  ProgressBar progressBar;
    public  Timeline    timer;
    public int          wager, current;
    public ArrayList<Integer> wagers;
    public ArrayList<String>    responses;
    public boolean wait;
    
    FinalJeopardy(){

        btnOk            = new Button("Ok");
        tfAnswerField    = new TextField();
        progressBar      = new ProgressBar();
        timer            = new Timeline();
        wagers           = new ArrayList<>();
        responses        = new ArrayList<>();
        current          = 0;
        wait             = true;

        init();
    }

    @Override
    public void init() {

        introScreen();
    }

    public void introScreen(){

        this.getChildren().add(new Label("Welcome to the Final Jeopardy!"));
        this.getChildren().add(new Label("Category:"));
        this.getChildren().add(new Label(GameboardWindow.categories.get(6)));

        wagerScreen();

    }

    public void wagerScreen(){

        for(int i = 0; i < GameboardWindow._players.size(); i++) {
            wager  = 0;
            while(!(wager <= GameboardWindow._players.get(i).getCurrentScore() && wager >= 1)) {
                wager = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter wager amount"));
            }
            wagers.add(wager);
        }

        questionAndResponseScreen();
    }

    public void questionAndResponseScreen(){

        try {

            String sql = "SELECT question_ID, category, clue, answer, value, type "
                    + "FROM Questions "
                    + "WHERE category = ? ";

            PreparedStatement pstmt = Main.getConnection().prepareStatement(sql);
            pstmt.setString(1, GameboardWindow.categories.get(6));
            ResultSet rs = pstmt.executeQuery();

            q = new Question(rs.getInt("question_ID"), rs.getString("category"), rs.getString("clue"), rs.getString("answer"), 0, "FF");

        } catch (SQLException sqlex) {

            System.out.println(sqlex.getMessage());

        }

        for(int i = current; i < GameboardWindow._players.size();){
            this.getChildren().clear();
            this.getChildren().add(new Label(GameboardWindow._players.get(current).getUsername() + " is up"));
            this.getChildren().add(new Label(q.getClue()));
            this.getChildren().add(tfAnswerField);
            this.getChildren().add(progressBar);
            startTimer();
        }

        btnOk.setOnAction(e -> {
            timer.stop();

            responses.add(tfAnswerField.getText());
            tfAnswerField.clear();
            current++;
        });

        // -----------------------------------------------------------------------
        // ProgessBar listener - what to do when timer runs out
        // -----------------------------------------------------------------------
        progressBar.progressProperty().addListener(e -> {

            if (progressBar.getProgress() == 0) {

                timer.stop();
                responses.add(tfAnswerField.getText());
                tfAnswerField.clear();
                current++;
            }
        });

        results();
    }
    public void results(){

    }

    private void startTimer() {

        progressBar.setProgress(1);
        timer.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new KeyValue(progressBar.progressProperty(), 0)));
        timer.play();
    }


}
