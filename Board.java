import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.beans.EventHandler;

class Board {
    static void loadScene(Stage stage) {
        VBox root = new VBox();

        HBox hbox = funcBox(stage);

        GridPane board = new GridPane();
        root.setStyle("-fx-border-width: 1;-fx-border-color: black;");

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x ++) {
                Button square = new Button();

                square.setId( Main.toId( x, y ) );
                square.setMinSize(100, 100);

                square.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> {
                    Button source = (Button) e.getSource();
                    String id = source.getId();
                    Scene scene = source.getScene();
                    Main.addPiece(Main.idX(id), Main.idY(id), scene, false);
                });

                board.add(square, x, y);
            }
        }

        root.getChildren().addAll(hbox, board);

        Scene scene = new Scene(root);

        Main.addPiece(3, 3, scene, true);
        Main.addPiece(3, 4, scene, true);
        Main.addPiece(4, 4, scene, true);
        Main.addPiece(4, 3, scene, true);

        scene.getStylesheets().add("board.css");

        stage.setTitle("Othello");
        stage.setScene(scene);
        stage.show();
    }

    private static HBox funcBox (Stage stage) {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets( 10, 10, 10 ,10));
        hbox.setStyle("-fx-background-color: #444;");

        /////////////////////////////////////////////////

        Button restartButton = new Button("Restart Game");
        restartButton.setMinSize(100, 20);
        restartButton.setStyle("-fx-background-color: #fff;-fx-border-width: 0");

        restartButton.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> {
            Main.isWhite = true;
            Main.boardArray = new int[8][8];
            PlayerAI.searchDepthVal = 4;
            loadScene(stage);
        });

        /////////////////////////////////////////////////

        Button scoreButton = new Button("Calculate Scores");
        scoreButton.setMinSize(100, 20);
        scoreButton.setStyle("-fx-background-color: #fff;-fx-border-width: 0");

        scoreButton.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> {
            int[] score = Main.calculateScore();
            System.out.println("White pieces: " + score[0] + ", Black pieces: " + score[1]);
        });

        /////////////////////////////////////////////////

        Label label = new Label("White player's turn!");
        label.setMinSize(359, 20);
        label.setAlignment(Pos.CENTER);
        label.setId("turnLabel");

        /////////////////////////////////////////////////

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8");
        comboBox.setStyle("-fx-background-color: #fff;-fx-border-width: 0;-fx-background-radius: 0");
        comboBox.getSelectionModel().select(3);
        comboBox.setOnAction( event -> {
            PlayerAI.searchDepthVal = Integer.parseInt(comboBox.getValue());
        });

        ////////////////////////////////////////////////

        Button advancedAiButton = new Button("Ai Move");
        advancedAiButton.setMinSize(100, 20);
        advancedAiButton.setStyle("-fx-background-color: #fff;-fx-border-width: 0");

        advancedAiButton.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> {
            Button source = (Button) e.getSource();
            Scene scene = source.getScene();

            PlayerAI.minimaxDecision( Main.boardArray, Main.isWhite, scene);
        });

        /////////////////////////////////////////////////

        hbox.getChildren().addAll(restartButton, scoreButton, label, comboBox, advancedAiButton);

        return hbox;
    }
}
