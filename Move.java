import java.util.List;

class Move {
    int x;
    int y;
    List<int[]> convertedPieces;

    int[][] makeMove(int[][] board, int player) {
        int[][] tempBoard = new int[8][];

        for(int i = 0; i < board.length; i++) {
            tempBoard[i] = board[i].clone();
        }

        tempBoard[x][y] = player;

        convertedPieces.forEach((move) -> {
            tempBoard[ move[0] ][ move[1] ] = player;
        });

        return tempBoard;
    }
}
