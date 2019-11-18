import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static Boolean isWhite = true;
    private static Boolean changed = false;
    static int[][] boardArray = new int[8][8];
    private static List<String> toRender = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board.loadScene(primaryStage);
    }

    static void addPiece(String id, Scene scene, Boolean initialise) {
        int x = idX(id);
        int y = idY(id);
        int player = isWhite ? 1 : 2;
        int notPlayer = isWhite ? 2 : 1;

        if ( initialise ) {
            renderPiece(id, scene);
            boardArray[x][y] = player;
            isWhite = !isWhite;
        }
        else if ( boardArray[x][y] == 0 ) {
            checkDirection(x, y, -1, -1, notPlayer, scene);
            checkDirection(x, y, 0, -1, notPlayer, scene);
            checkDirection(x, y, 1, -1, notPlayer, scene);
            checkDirection(x, y, 1, 0, notPlayer, scene);
            checkDirection(x, y, 1, 1, notPlayer, scene);
            checkDirection(x, y, 0, 1, notPlayer, scene);
            checkDirection(x, y, -1, 1, notPlayer, scene);
            checkDirection(x, y, -1, 0, notPlayer, scene);

            if ( changed ) {
                renderPiece(id, scene);
                boardArray[x][y] = player;

                isWhite = !isWhite;
                changed = false;

                Label label = (Label) scene.lookup("#turnLabel");
                label.setText( isWhite? "White player's turn!" : "Black player's turn!" );

                System.out.print("Button: " + id + "\n");
            }
        }
    }


    private static void checkForChain (int x, int y, int dirX, int dirY, Scene scene) {
        int notPlayer = isWhite ? 2 : 1;
        int player = isWhite ? 1 : 2;
        x += dirX;
        y += dirY;

        if ( boardArray[x][y] == notPlayer ) {
            toRender.add(toId(x, y));

            if (( y > 0 || dirY != -1)
                    && (y < 7 || dirY != 1)
                    && (x > 0 || dirX != -1)
                    && (x < 7  || dirX != 1)) {
                checkForChain (x , y, dirX, dirY, scene);
            }
        }

        if ( boardArray[x][y] == player  ) {
            toRender.forEach((tempId) -> {
                renderPiece(tempId, scene);

                boardArray[idX(tempId)][idY(tempId)] = player;
            });

            toRender.clear();
            changed = true;
        }
        else {
            toRender.clear();
        }
    }

    private static void checkDirection (int x, int y, int dirX, int dirY, int notPlayer, Scene scene) {
        try {
            if (boardArray[x + dirX][y + dirY] == notPlayer) {
                checkForChain(x, y, dirX, dirY, scene);
            }
        }
        catch ( Exception ignored ) {}
    }

    private static void renderPiece (String id, Scene scene) {
        String url = isWhite ? "whitePiece.png" : "blackPiece.png";

        Button buttonClicked = (Button) scene.lookup("#" + id);

        Image image = new Image(url, 80, 80, false, false);
        buttonClicked.setGraphic(new ImageView(image));
    }

    private static int idY (String id) {
        return Character.getNumericValue( id.charAt(1) );
    }

    private static int idX (String id) {
        return Character.getNumericValue( id.charAt(0) );
    }

    static String toId(int x, int y) {
        return Integer.toString(x) + Integer.toString(y);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
