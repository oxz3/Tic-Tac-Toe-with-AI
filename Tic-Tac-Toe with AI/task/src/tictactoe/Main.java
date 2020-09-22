package tictactoe;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.lang.Math;

public class Main {
        static int xCount = 0;
        static int oCount = 0;
        static String[][] board = new String[3][3];

    public static void main(String[] args) {
        // write your code here

        String[] players = inputCommand();

        initializeBoard(board);
        printBoard(board);
        playGame(board, players);


    }

    public static String[] inputCommand() {
        Scanner keyboard = new Scanner(System.in);
        String[] players = new String[2];
        boolean legalCommand = false;

        do {
            try {
                System.out.print("Input command: ");
                String inputComm = keyboard.next();

                if (inputComm.equals("exit")) {
                    System.exit(0);
                    // Exit condition
                }
                else if (inputComm.equals("start")) {
                    players[0] = keyboard.next();
                    players[1] = keyboard.next();
                    legalCommand = true;
                }
                else {
                    System.out.print("Bad parameters!");
                }
            } catch (Exception e) {
                System.out.println("Bad parameters!");
                System.exit(0);
            }

        } while (!legalCommand);

        return players;
    }

    public static void printBoard(String board[][]) {
        // print out game board
        xCount = 0;
        oCount = 0;
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("_"))
                    System.out.print("  ");
                else {
                    System.out.print(board[i][j] + " ");
                    if (board[i][j].equals("X"))
                        xCount++;
                    else if (board[i][j].equals("O"))
                        oCount++;
                }
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static void initializeBoard(String board[][]) {
        // input into grid blanks
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "_";
            }
        }
    }

    public static void playerMove(String board[][], String symbol) {
        int cord1;
        int cord2;
        Scanner keyboard = new Scanner(System.in);
        boolean legalMove = false;

        do {
            System.out.print("Enter the coordinates: ");
            try {
                cord1 = Integer.parseInt(keyboard.next());
                cord2 = Integer.parseInt(keyboard.next());
                if (((cord1 > 3) || (cord2 > 3)) || ((cord1 < 0) || (cord2 < 0)))
                    System.out.println("Coordinates should be from 1 to 3!");
                else {

                    if (!board[3 - cord2][cord1 - 1].equals("_")) {
                        System.out.println("This cell is occupied! Choose another one!");
                        legalMove = false;
                    } else {
                        board[3 - cord2][cord1 - 1] = symbol;
                        legalMove = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("You should enter numbers!");
            }

        } while (!legalMove);
    }

    public static void computerMoveEasy(String board[][], String symbol) {
        int cord1;
        int cord2;
        boolean legalMove = false;

        Random random = new Random();

        do {
            cord1 = random.nextInt(3) + 1;
            cord2 = random.nextInt(3) + 1;
            if (!board[3 - cord2][cord1 - 1].equals("_")) {
                legalMove = false;
            } else {
                board[3 - cord2][cord1 - 1] = symbol;
                System.out.println("Making move level \"easy\"");
                legalMove = true;
            }
        } while (!legalMove);
    }

    public static void computerMoveMedium(String board[][], String symbol) {
        int cord1;
        int cord2;
        boolean legalMove = false;

        Random random = new Random();
        legalMove = false;

        if (!goForNearWin(board, symbol)) {
            if (!blockForNearWin(board, symbol)) {
                do {
                    cord1 = random.nextInt(3) + 1;
                    cord2 = random.nextInt(3) + 1;
                    if (!board[3 - cord2][cord1 - 1].equals("_")) {
                        legalMove = false;
                    } else {
                        board[3 - cord2][cord1 - 1] = symbol;
                        System.out.println("Making move level \"medium\"");
                        legalMove = true;
                    }
                } while (!legalMove);
            }
        }
    }

    public static void computerMoveHard(String board[][], String symbol) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move();
        ArrayList<Move> emptyArr = findEmptySpots(board);
        for (int i = 0; i < emptyArr.size(); i++) {
            int cord1 = emptyArr.get(i).cord1;
            int cord2 = emptyArr.get(i).cord2;
            board[cord1][cord2] = symbol;
            int score = miniMax(board, symbol,0, false);
            board[cord1][cord2] = "_";
            if (score > bestScore) {
                bestScore = score;
                bestMove.cord1 = cord1;
                bestMove.cord2 = cord2;
            }
        }
        board[bestMove.cord1][bestMove.cord2] = symbol;
        System.out.println("Making move level \"hard\"");
    }

    public static ArrayList<Move> findEmptySpots(String board[][]) {
        ArrayList<Move> spots = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals("_")) {
                    Move cords = new Move();
                    cords.cord1 = i;
                    cords.cord2 = j;
                    spots.add(cords);
                }
            }
        }
        return spots;
    }

    public static int miniMax(String board[][], String symbol, int depth, boolean isMax) {
        Move bestMove = new Move();
        ArrayList<Move> emptyArr = findEmptySpots(board);

        String termResult = checkForTerminal(board);
        String oppSymbol;
        if (symbol.equals("X"))
            oppSymbol = "O";
        else
            oppSymbol = "X";

        if (termResult.equals(symbol)) {
            // Win
            return 10;
        } else if (termResult.equals("D")) {
            // Tie
            return 0;
        } else if (termResult.equals(oppSymbol)) {
            // Lose
            return -10;
        }

        if (isMax) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < emptyArr.size(); i++) {
                int cord1 = emptyArr.get(i).cord1;
                int cord2 = emptyArr.get(i).cord2;
                board[cord1][cord2] = symbol;
                int score = miniMax(board, oppSymbol,depth + 1, false);
                board[cord1][cord2] = "_";
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < emptyArr.size(); i++) {
                int cord1 = emptyArr.get(i).cord1;
                int cord2 = emptyArr.get(i).cord2;
                board[cord1][cord2] = symbol;
                int score = miniMax(board, oppSymbol,depth + 1, true);
                board[cord1][cord2] = "_";
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }

    }

    public static boolean blockForNearWin(String board[][], String symbol) {
        String oppSymbol;
        if (symbol.equals("X"))
            oppSymbol = "O";
        else
            oppSymbol = "X";

        boolean block = true;
        // Horizontal checks
        if (board[0][0].equals(oppSymbol) && board[0][1].equals(oppSymbol) && board[0][2].equals("_")) {
            board[0][2] = symbol;
        } else if (board[0][0].equals(oppSymbol) && board[0][1].equals("_") && board[0][2].equals(oppSymbol)) {
            board[0][1] = symbol;
        } else if (board[0][0].equals("_") && board[0][1].equals(oppSymbol) && board[0][2].equals(oppSymbol)) {
            board[0][0] = symbol;
        } else if (board[1][0].equals(oppSymbol) && board[1][1].equals(oppSymbol) && board[1][2].equals("_")) {
            board[1][2] = symbol;
        } else if (board[1][0].equals(oppSymbol) && board[1][1].equals("_") && board[1][2].equals(oppSymbol)) {
            board[1][1] = symbol;
        } else if (board[1][0].equals("_") && board[1][1].equals(oppSymbol) && board[1][2].equals(oppSymbol)) {
            board[1][0] = symbol;
        } else if (board[2][0].equals(oppSymbol) && board[2][1].equals(oppSymbol) && board[2][2].equals("_")) {
            board[2][2] = symbol;
        } else if (board[2][0].equals(oppSymbol) && board[2][1].equals("_") && board[2][2].equals(oppSymbol)) {
            board[2][1] = symbol;
        } else if (board[2][0].equals("_") && board[2][1].equals(oppSymbol) && board[2][2].equals(oppSymbol)) {
            board[2][0] = symbol;
        }
        // Vertical checks
        else if (board[0][0].equals(oppSymbol) && board[1][0].equals(oppSymbol) && board[2][0].equals("_")) {
            board[2][0] = symbol;
        } else if (board[0][0].equals(oppSymbol) && board[1][0].equals("_") && board[2][0].equals(oppSymbol)) {
            board[1][0] = symbol;
        } else if (board[0][0].equals("_") && board[1][0].equals(oppSymbol) && board[2][0].equals(oppSymbol)) {
            board[0][0] = symbol;
        } else if (board[0][1].equals(oppSymbol) && board[1][1].equals(oppSymbol) && board[2][1].equals("_")) {
            board[2][1] = symbol;
        } else if (board[0][1].equals(oppSymbol) && board[1][1].equals("_") && board[2][1].equals(oppSymbol)) {
            board[1][1] = symbol;
        } else if (board[0][1].equals("_") && board[1][1].equals(oppSymbol) && board[2][1].equals(oppSymbol)) {
            board[0][1] = symbol;
        } else if (board[0][2].equals(oppSymbol) && board[1][2].equals(oppSymbol) && board[2][2].equals("_")) {
            board[2][2] = symbol;
        } else if (board[0][2].equals(oppSymbol) && board[1][2].equals("_") && board[2][2].equals(oppSymbol)) {
            board[1][2] = symbol;
        } else if (board[0][2].equals("_") && board[1][2].equals(oppSymbol) && board[2][2].equals(oppSymbol)) {
            board[0][2] = symbol;
        }
        // Diagonal checks
        else if (board[0][0].equals(oppSymbol) && board[1][1].equals(oppSymbol) && board[2][2].equals("_")) {
            board[2][2] = symbol;
        } else if (board[0][0].equals(oppSymbol) && board[1][1].equals("_") && board[2][2].equals(oppSymbol)) {
            board[1][1] = symbol;
        } else if (board[0][0].equals("_") && board[1][1].equals(oppSymbol) && board[2][2].equals(oppSymbol)) {
            board[0][0] = symbol;
        } else if (board[0][2].equals(oppSymbol) && board[1][1].equals(oppSymbol) && board[2][0].equals("_")) {
            board[2][0] = symbol;
        } else if (board[0][2].equals(oppSymbol) && board[1][1].equals("_") && board[2][0].equals(oppSymbol)) {
            board[1][1] = symbol;
        } else if (board[0][2].equals("_") && board[1][1].equals(oppSymbol) && board[2][0].equals(oppSymbol)) {
            board[0][2] = symbol;
        }

        else
            block = false;

        if (block)
            System.out.println("Making move level \"medium\"");
        return block;
    }

    public static String checkForTerminal(String board[][]) {
        // check for win conditions
        boolean isWinner;
        if (((board[0][0].equals("X")) && (board[0][1].equals("X")) && (board[0][2].equals("X"))) ||
                ((board[1][0].equals("X")) && (board[1][1].equals("X")) && (board[1][2].equals("X"))) ||
                ((board[2][0].equals("X")) && (board[2][1].equals("X")) && (board[2][2].equals("X"))) ||
                ((board[0][0].equals("X")) && (board[1][0].equals("X")) && (board[2][0].equals("X"))) ||
                ((board[0][1].equals("X")) && (board[1][1].equals("X")) && (board[2][1].equals("X"))) ||
                ((board[0][2].equals("X")) && (board[1][2].equals("X")) && (board[2][2].equals("X"))) ||
                ((board[0][0].equals("X")) && (board[1][1].equals("X")) && (board[2][2].equals("X"))) ||
                ((board[2][0].equals("X")) && (board[1][1].equals("X")) && (board[0][2].equals("X")))) {
            return "X";
        }
        else if (((board[0][0].equals("O")) && (board[0][1].equals("O")) && (board[0][2].equals("O"))) ||
                ((board[1][0].equals("O")) && (board[1][1].equals("O")) && (board[1][2].equals("O"))) ||
                ((board[2][0].equals("O")) && (board[2][1].equals("O")) && (board[2][2].equals("O"))) ||
                ((board[0][0].equals("O")) && (board[1][0].equals("O")) && (board[2][0].equals("O"))) ||
                ((board[0][1].equals("O")) && (board[1][1].equals("O")) && (board[2][1].equals("O"))) ||
                ((board[0][2].equals("O")) && (board[1][2].equals("O")) && (board[2][2].equals("O"))) ||
                ((board[0][0].equals("O")) && (board[1][1].equals("O")) && (board[2][2].equals("O"))) ||
                ((board[2][0].equals("O")) && (board[1][1].equals("O")) && (board[0][2].equals("O")))) {
            return "O";
        }
        else if (xCount == 5) {
            return "D";
        }
        else
            return "N";
    }

    public static boolean goForNearWin(String board[][], String symbol) {
        boolean win = true;
        // Horizontal checks
        if (board[0][0].equals(symbol) && board[0][1].equals(symbol) && board[0][2].equals("_")) {
            board[0][2] = symbol;
        } else if (board[0][0].equals(symbol) && board[0][1].equals("_") && board[0][2].equals(symbol)) {
            board[0][1] = symbol;
        } else if (board[0][0].equals("_") && board[0][1].equals(symbol) && board[0][2].equals(symbol)) {
            board[0][0] = symbol;
        } else if (board[1][0].equals(symbol) && board[1][1].equals(symbol) && board[1][2].equals("_")) {
            board[1][2] = symbol;
        } else if (board[1][0].equals(symbol) && board[1][1].equals("_") && board[1][2].equals(symbol)) {
            board[1][1] = symbol;
        } else if (board[1][0].equals("_") && board[1][1].equals(symbol) && board[1][2].equals(symbol)) {
            board[1][0] = symbol;
        } else if (board[2][0].equals(symbol) && board[2][1].equals(symbol) && board[2][2].equals("_")) {
            board[2][2] = symbol;
        } else if (board[2][0].equals(symbol) && board[2][1].equals("_") && board[2][2].equals(symbol)) {
            board[2][1] = symbol;
        } else if (board[2][0].equals("_") && board[2][1].equals(symbol) && board[2][2].equals(symbol)) {
            board[2][0] = symbol;
        }
        // Vertical checks
        else if (board[0][0].equals(symbol) && board[1][0].equals(symbol) && board[2][0].equals("_")) {
            board[2][0] = symbol;
        } else if (board[0][0].equals(symbol) && board[1][0].equals("_") && board[2][0].equals(symbol)) {
            board[1][0] = symbol;
        } else if (board[0][0].equals("_") && board[1][0].equals(symbol) && board[2][0].equals(symbol)) {
            board[0][0] = symbol;
        } else if (board[0][1].equals(symbol) && board[1][1].equals(symbol) && board[2][1].equals("_")) {
            board[2][1] = symbol;
        } else if (board[0][1].equals(symbol) && board[1][1].equals("_") && board[2][1].equals(symbol)) {
            board[1][1] = symbol;
        } else if (board[0][1].equals("_") && board[1][1].equals(symbol) && board[2][1].equals(symbol)) {
            board[0][1] = symbol;
        } else if (board[0][2].equals(symbol) && board[1][2].equals(symbol) && board[2][2].equals("_")) {
            board[2][2] = symbol;
        } else if (board[0][2].equals(symbol) && board[1][2].equals("_") && board[2][2].equals(symbol)) {
            board[1][2] = symbol;
        } else if (board[0][2].equals("_") && board[1][2].equals(symbol) && board[2][2].equals(symbol)) {
            board[0][2] = symbol;
        }
        // Diagonal checks
        else if (board[0][0].equals(symbol) && board[1][1].equals(symbol) && board[2][2].equals("_")) {
            board[2][2] = symbol;
        } else if (board[0][0].equals(symbol) && board[1][1].equals("_") && board[2][2].equals(symbol)) {
            board[1][1] = symbol;
        } else if (board[0][0].equals("_") && board[1][1].equals(symbol) && board[2][2].equals(symbol)) {
            board[0][0] = symbol;
        } else if (board[0][2].equals(symbol) && board[1][1].equals(symbol) && board[2][0].equals("_")) {
            board[2][0] = symbol;
        } else if (board[0][2].equals(symbol) && board[1][1].equals("_") && board[2][0].equals(symbol)) {
            board[1][1] = symbol;
        } else if (board[0][2].equals("_") && board[1][1].equals(symbol) && board[2][0].equals(symbol)) {
            board[0][2] = symbol;
        }

        else
            win = false;
        if (win)
            System.out.println("Making move level \"medium\"");
        return win;
    }

    public static void playGame(String board[][], String players[]) {

        while (!checkWinner(board)) {
            if (xCount == oCount) {
                // X turn
                if (players[0].equals("user")) {
                    playerMove(board, "X");
                } else if (players[0].equals("easy")) {
                    computerMoveEasy(board, "X");
                } else if (players[0].equals("medium")) {
                    computerMoveMedium(board, "X");
                } else if (players[0].equals("hard")) {
                    computerMoveHard(board, "X");
                } else {
                    // shouldn't make it here
                    System.exit(0);
                }

            } else {
                // Computer O turn
                if (players[1].equals("user")) {
                    playerMove(board, "O");
                } else if (players[0].equals("easy")) {
                    computerMoveEasy(board, "O");
                } else if (players[0].equals("medium")) {
                    computerMoveMedium(board, "O");
                } else if (players[0].equals("hard")) {
                    computerMoveHard(board, "O");
                } else {
                    // shouldn't make it here
                    System.exit(0);
                }
            }

            printBoard(board);
        }
    }

    public static boolean checkWinner(String board[][]) {
        // check for win conditions
        boolean isGameOver;
        if (((board[0][0].equals("X")) && (board[0][1].equals("X")) && (board[0][2].equals("X"))) ||
                ((board[1][0].equals("X")) && (board[1][1].equals("X")) && (board[1][2].equals("X"))) ||
                ((board[2][0].equals("X")) && (board[2][1].equals("X")) && (board[2][2].equals("X"))) ||
                ((board[0][0].equals("X")) && (board[1][0].equals("X")) && (board[2][0].equals("X"))) ||
                ((board[0][1].equals("X")) && (board[1][1].equals("X")) && (board[2][1].equals("X"))) ||
                ((board[0][2].equals("X")) && (board[1][2].equals("X")) && (board[2][2].equals("X"))) ||
                ((board[0][0].equals("X")) && (board[1][1].equals("X")) && (board[2][2].equals("X"))) ||
                ((board[2][0].equals("X")) && (board[1][1].equals("X")) && (board[0][2].equals("X")))) {
            System.out.print("X wins");
            isGameOver = true;
        }
        else if (((board[0][0].equals("O")) && (board[0][1].equals("O")) && (board[0][2].equals("O"))) ||
                ((board[1][0].equals("O")) && (board[1][1].equals("O")) && (board[1][2].equals("O"))) ||
                ((board[2][0].equals("O")) && (board[2][1].equals("O")) && (board[2][2].equals("O"))) ||
                ((board[0][0].equals("O")) && (board[1][0].equals("O")) && (board[2][0].equals("O"))) ||
                ((board[0][1].equals("O")) && (board[1][1].equals("O")) && (board[2][1].equals("O"))) ||
                ((board[0][2].equals("O")) && (board[1][2].equals("O")) && (board[2][2].equals("O"))) ||
                ((board[0][0].equals("O")) && (board[1][1].equals("O")) && (board[2][2].equals("O"))) ||
                ((board[2][0].equals("O")) && (board[1][1].equals("O")) && (board[0][2].equals("O")))) {
            System.out.print("O wins");
            isGameOver = true;
        }
        else if (xCount == 5) {
            System.out.print("Draw");
            isGameOver = true;
        }
        else
            isGameOver = false;

        return isGameOver;
    }

}

class Move {
    int cord1;
    int cord2;
}
