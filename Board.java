import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class Board {
    static void loadScene(Stage stage) {
        VBox root = new VBox();

        HBox hbox = funcBox(stage);

        GridPane board = new GridPane();
        root.setStyle("-fx-border-width: 1;" +
                      "-fx-border-color: black;");

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x ++) {
                Button square = new Button();

                square.setId( Main.toId( x, y ) );
                square.setMinSize(100, 100);

                square.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> {
                    Button source = (Button) e.getSource();
                    String id = source.getId();
                    Scene scene = source.getScene();
                    Main.addPiece(id, scene, false);
                });

                board.add(square, x, y);
            }
        }

        root.getChildren().addAll(hbox, board);

        Scene scene = new Scene(root);

        Main.addPiece("33", scene, true);
        Main.addPiece("34", scene, true);
        Main.addPiece("44", scene, true);
        Main.addPiece("43", scene, true);

        scene.getStylesheets().add("board.css");

        stage.setTitle("Othello");
        stage.setScene(scene);
        stage.show();
    }

    private static HBox funcBox (Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets( 10, 10, 10 ,10));
        hbox.setStyle("-fx-background-color: #444;");

        Button button = new Button("Restart game");
        button.setMinSize(100, 20);
        button.setStyle("-fx-background-color: #fff;" +
                        "-fx-border-width: 0");

        button.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> {
            Main.isWhite = true;
            Main.boardArray = new int[8][8];
            loadScene(stage);
        });

        Label label = new Label("White player's turn!");
        label.setId("turnLabel");

        Region region = new Region();
        region.setMinWidth(175);

        hbox.getChildren().addAll(button, region, label);

        return hbox;
    }
}
