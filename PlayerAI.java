import javafx.scene.Scene;
import java.util.*;

class PlayerAI {
    private static int player;
    private static int notPlayer;
    private static int counter = 0;
    static int searchDepthVal = 4;

    static void minimaxDecision(int[][] board, boolean isWhite, Scene scene) {
        int player = isWhite ? 1 : 2;
        counter = 0;

        List<Move> moveList = getMoveList( board, isWhite );

        if ( moveList.isEmpty() ) {
            Main.noMovePossible( scene );
        }
        else {
            int bestX = 0, bestY = 0, bestMoveVal = -999999;

            for (Move move : moveList) {
                int[][] tempBoard = move.makeMove(board, player);

                int val = minimaxValue(tempBoard, isWhite, !isWhite, 1, -999999, 999999);

                if (val > bestMoveVal) {
                    bestMoveVal = val;

                    bestX = move.x;
                    bestY = move.y;
                }
            }

            System.out.println("Move counter: " + counter);
            Main.addPiece(bestX, bestY, scene, false);
        }
    }

    private static int minimaxValue ( int [][] board, boolean isWhiteInitial, boolean isWhiteCurrentTurn, int searchDepth, int alpha, int beta ) {
        int tempPlayer = isWhiteCurrentTurn ? 1 : 2;

        if ( searchDepth == searchDepthVal ) {
            return heuristic( board, isWhiteInitial );
        }

        List<Move> moveList = getMoveList( board, isWhiteCurrentTurn );

        if ( moveList.isEmpty() )
        {
            return minimaxValue( board, isWhiteInitial, !isWhiteCurrentTurn, searchDepth + 1, alpha, beta );
        }
        else {
            int bestMoveVal = isWhiteInitial == isWhiteCurrentTurn ? -999999 : 999999;

            for (Move move : moveList) {
                int[][] tempBoard = move.makeMove(board, tempPlayer);

                int val = minimaxValue( tempBoard, isWhiteInitial, !isWhiteCurrentTurn, searchDepth + 1, alpha, beta );

                if (isWhiteCurrentTurn == isWhiteInitial) {
                    bestMoveVal = Math.max( bestMoveVal, val );
                    alpha = Math.max( alpha, bestMoveVal );
                    if ( beta <= alpha ) {
                        break;
                    }
                }
                else {
                    bestMoveVal = Math.min( bestMoveVal, val );
                    beta = Math.min( beta, bestMoveVal );
                    if ( beta <= alpha ) {
                        break;
                    }
                }
            }

            return bestMoveVal;
        }
    }


    private static int heuristic ( int [][] board, boolean isWhite) {
        int player = isWhite ? 1 : 2;
        int opponent = isWhite ? 2 : 1;

        int playerScore = 0, opponentScore = 0;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x ++) {
                if ( board[x][y] == player ) {
                    playerScore++;

                    if (( x == 0 || x == 7 ) && ( y == 0 || y == 7 ))
                        playerScore += 10;

                    if ( y == 0 || y == 7 || x == 0 || x == 7)
                        playerScore ++;
                }
                else if ( board[x][y] == opponent ) {
                    opponentScore++;

                    if (( x == 0 || x == 7 ) && ( y == 0 || y == 7 ))
                        opponentScore += 10;

                    if ( y == 0 || y == 7 || x == 0 || x == 7)
                        opponentScore ++;
                }
            }
        }

        counter++;
        return playerScore - opponentScore;
    }


    static List<Move> getMoveList ( int[][] board, boolean isWhite) {
        List<Move> moveList = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y ++) {
                if ( board[x][y] == 0 ) {
                    Move move = getMove( x, y, board, isWhite);

                    if ( !move.convertedPieces.isEmpty() ) {
                        moveList.add(move);
                    }
                }
            }
        }

        return moveList;
    }


    static Move getMove ( int x, int y, int [][] board, boolean isWhite) {
        player = isWhite ? 1 : 2;
        notPlayer = isWhite ? 2 : 1;

        Move move = new Move();
        move.x = x;
        move.y = y;
        move.convertedPieces = new ArrayList<>();

        for (int dirX = -1; dirX < 2; dirX++) {
            for (int dirY = -1; dirY < 2; dirY++) {
                move.convertedPieces.addAll(checkDirection(x, y, dirX, dirY, board));
            }
        }

        return move;
    }


    private static List<int[]> checkDirection (int x, int y, int dirX, int dirY, int [][] board) {
        List<int[]> convertedList = new ArrayList<>();

        if (( y > 0 || dirY != -1) && (y < 7 || dirY != 1) && (x > 0 || dirX != -1) && (x < 7  || dirX != 1)) {
            if (board[x + dirX][y + dirY] == notPlayer) {
                return piecesConverted(x, y, dirX, dirY, board, convertedList);
            }
        }

        return convertedList;
    }


    private static List<int[]> piecesConverted (int x, int y, int dirX, int dirY, int [][] board, List<int[]> convertedPieces) {
        x += dirX;
        y += dirY;

        if ( board[x][y] == notPlayer ) {
            convertedPieces.add(new int[] { x, y });

            if (( y > 0 || dirY != -1) && (y < 7 || dirY != 1) && (x > 0 || dirX != -1) && (x < 7  || dirX != 1)) {
                return piecesConverted(x , y, dirX, dirY, board, convertedPieces);
            }
        }

        if ( board[x][y] == player ) {
            return convertedPieces;
        }
        else {
            convertedPieces.clear();
            return convertedPieces;
        }
    }
}