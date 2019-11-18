import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
public class Main extends Application {

    static Boolean isWhite = true;
    static int[][] boardArray = new int[8][8];

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board.loadScene(primaryStage);
    }

    static void addPiece(int x, int y, Scene scene, Boolean initialise) {
        if ( initialise ) {
            renderPiece(x, y, scene);
            isWhite = !isWhite;
        }
        else if ( boardArray[x][y] == 0 ) {
            Move move = PlayerAI.getMove(x, y, boardArray, isWhite);

            if ( !move.convertedPieces.isEmpty() ) {
                renderPiece(x, y, scene);

                move.convertedPieces.forEach( convertedPiece -> {
                    renderPiece(convertedPiece[0], convertedPiece[1], scene);
                });

                isWhite = !isWhite;

                Label label = (Label) scene.lookup("#turnLabel");
                label.setText( isWhite? "White player's turn!" : "Black player's turn!" );

                System.out.print("Button: " + toId(x, y) + "\n");
            }
            else if ( PlayerAI.getMoveList(boardArray, isWhite).isEmpty() ) {
                noMovePossible( scene );
            }
        }

        if ( !initialise )
            endGame();
    }

    static void noMovePossible ( Scene scene ) {
        Alert noMoveAlert = new Alert(Alert.AlertType.INFORMATION);
        noMoveAlert.setContentText("No possible move for " + (isWhite? "white!\n" : "black!\n") + "It is now " + (!isWhite? "white's" : "black's") + " turn :)");
        noMoveAlert.setTitle(null);
        noMoveAlert.setHeaderText(null);
        noMoveAlert.show();

        isWhite = !isWhite;

        Label label = (Label) scene.lookup("#turnLabel");
        label.setText( isWhite? "White player's turn!" : "Black player's turn!" );
    }

    private static void endGame() {
        int[] state = calculateScore();
        int whiteScore = state[0], blackScore = state[1], zeroCounter = state[2];

        if ( whiteScore == 0 || blackScore == 0 || zeroCounter == 0 ) {
            String winner = whiteScore > blackScore ? "white wins " : "black wins ";
            String score = whiteScore > blackScore ? whiteScore  + " to " + blackScore : blackScore + " to " + whiteScore;

            Alert endgameAlert = new Alert(Alert.AlertType.INFORMATION);
            endgameAlert.setContentText("Congratulations " + winner + score + "!");
            endgameAlert.setTitle("End of Game");
            endgameAlert.setHeaderText(null);
            endgameAlert.show();
        }
    }

    static int[] calculateScore () {
        int whiteScore = 0, blackScore = 0, zeroCounter = 0;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x ++) {
                if ( boardArray[x][y] == 1 )
                    whiteScore++;
                else if ( boardArray[x][y] == 2 )
                    blackScore++;
                else
                    zeroCounter++;
            }
        }

        return new int[] { whiteScore, blackScore, zeroCounter};
    }

    private static void renderPiece (int x, int y, Scene scene) {
        boardArray[x][y] = isWhite? 1 : 2;
        String url = isWhite ? "whitePiece.png" : "blackPiece.png";

        Button buttonClicked = (Button) scene.lookup("#" + toId(x, y));

        Image image = new Image(url, 80, 80, false, false);
        buttonClicked.setGraphic(new ImageView(image));
    }

    static void printBoard ( int[][] board ) {
        for (int x = 0; x < 8; x++) {
            System.out.println(board[0][x] + " " + board[1][x] + " " +
                    board[2][x] + " " + board[3][x] + " " +
                    board[4][x] + " " + board[5][x] + " " +
                    board[6][x] + " " + board[7][x]);
        }
        System.out.println("-----------------");
    }

    static int idY (String id) {
        return Character.getNumericValue( id.charAt(1) );
    }

    static int idX (String id) {
        return Character.getNumericValue( id.charAt(0) );
    }

    static String toId(int x, int y) {
        return Integer.toString(x) + y;
    }

    static void main(String[] args) {
        launch(args);
    }
}