package tictactoe;
import java.util.Scanner;
import java.util.stream.StreamSupport;
import java.util.Random;

public class Main {
    //private static int greedPrints = 0;

    public static void main(String[] args) {
        // write your code here
        String gameMode = "";
        while (!"exit".equals(gameMode)) {
            gameMode = openMenu();

            if (!"exit".equals(gameMode)) {
                String cells = "_________";
                //--String cells = inputCells();
                char[][] matrix = fillTheMatrix(cells);
                printGridFromMatrix(matrix);
                startTheGame(matrix, gameMode);
            }
        }
    }

    public static String openMenu() {

        String option;
        Scanner scanner = new Scanner(System.in);

        boolean valid = false;
        String gameMode = "exit";
        while (!valid) {
            System.out.print("Input command: ");
            option = scanner.nextLine();

            switch (option) {
                case "start easy easy":
                    gameMode = "cpu2cpu";
                    valid = true;
                    return gameMode;
                case "start easy user":
                    gameMode = "cpu2p";
                    valid = true;
                    return gameMode;
                case "start user user":
                    gameMode = "p2p";
                    valid = true;
                    return gameMode;
                case "exit":
                    valid = true;
                    return option;
                default:
                    System.out.println("Bad parameters!");
                    break;
            }
        }
        return gameMode;
    }

    public static char getMove(char[][] matrix) {
        int xQuantity = counter(matrix, 'X');
        int oQuantity = counter(matrix, 'O');
        if (xQuantity == oQuantity) {
            return 'X';
        } else if (xQuantity > oQuantity) {
            return 'O';
        } else {
            return '_';
        }
    }

    public static int[] CPUMove(char[][] matrix) {
        int[] coordinates = new int[2];
        Random random = new Random();
        boolean searching = true;
        System.out.println("Making move level \"easy\"");
        while (searching) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[i][j] == '_') {
                        if (random.nextInt(2) == 0) {
                            coordinates[0] = i + 1;
                            coordinates[1] = j + 1;
                            searching = false;
                            return coordinates;
                        }
                    }
                }
            }
        }

        return coordinates;
    }

    public static void startTheGame(char[][] matrix, String gameMode) {
        int[] coordinates = { 0, 0 };
        char move = getMove(matrix);
        while (!findTheWinner(matrix)) {
            if ("cpu2p".equals(gameMode)) {
                switch (move){
                    case 'X':
                        coordinates = inputCoordinates(matrix);
                        break;
                    case 'O':
                        coordinates = CPUMove(matrix);
                        break;
                }
            } else if ("cpu2cpu".equals(gameMode)) {
                coordinates = CPUMove(matrix);
            } else if ("p2p".equals(gameMode)) {
                coordinates = inputCoordinates(matrix);
            }


            matrix = updateMatrixWithMove(matrix, coordinates, move);
            printGridFromMatrix(matrix);

            if (move == 'X') {
                move = 'O';
            } else {
                move = 'X';
            }
        }
    }

    public static char[][] updateMatrixWithMove(char[][] matrix, int[] coordinates, char move) {
        matrix[coordinates[0]-1][coordinates[1]-1] = move;
        return matrix;
    }

    public static boolean checkMatrixWithCoordinates(int[] coordinates, char[][] matrix) {
        if (matrix[coordinates[0]-1][coordinates[1]-1] != '_'){
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        } else {
            return true;
        }
    }

    public static int[] inputCoordinates(char[][] matrix) {
        Scanner scanner = new Scanner(System.in);

        int[] coordinates = new int[2];
        boolean valid = false;
        while (!valid) {
            System.out.print("Enter the coordinates: ");
            String input = scanner.nextLine();
            if (input.matches("\\d\\s\\d")) {
                String[] inputArr = input.split(" ");
                for (int i = 0; i < 2; i++) {
                    coordinates[i] = Integer.parseInt(inputArr[i]);
                    if (coordinates[i] < 1 || coordinates[i] > 3) {
                        System.out.println("Coordinates should be from 1 to 3!");
                        break;
                    } else {
                        if (i == 1 && checkMatrixWithCoordinates(coordinates, matrix)) {
                            valid = true;
                        }
                    }
                }
            } else {
                System.out.println("You should enter numbers!");
            }
        }


        return coordinates;

    }

    public static boolean checkRowsColsDiags(char V, char[][] matrix) {
        boolean result = false;
        for (int i = 0; i < 3; i ++) {
            result = matrix[i][0] == V
                    && matrix [i][1] == V
                    && matrix [i][2] == V
                    || matrix[0][i] == V
                    && matrix [1][i] == V
                    && matrix [2][i] == V
                    || matrix[0][0] == V
                    && matrix[1][1] == V
                    && matrix[2][2] == V
                    || matrix [0][2] == V
                    && matrix[1][1] == V
                    && matrix[2][0] == V;
            if (result) {
                break;
            }
        }
        return result;
    }

    public static int counter(char[][] matrix, char V) {
        StringBuilder str = new StringBuilder();
        for (char[] ch : matrix) {
            for (int i = 0; i < 3; i++) {
                str.append(ch[i]);
            }
        }
        String cells = str.toString();
        return (int)(cells.chars().filter(ch -> ch == V).count());
    }

    public static boolean findTheWinner(char[][] matrix) {
        boolean xWon;
        boolean oWon;
        int xQuantity = counter(matrix, 'X');
        int oQuantity = counter(matrix, 'O');
        xWon = checkRowsColsDiags('X', matrix);
        oWon = checkRowsColsDiags('O', matrix);
        if (Math.abs(xQuantity - oQuantity) > 1) {
            System.out.println("Impossible");
            return true;
        } else if (xWon && !oWon) {
            System.out.println("X wins");
            return true;
        } else if(!xWon && oWon) {
            System.out.println("O wins");
            return true;
        } else if (xQuantity + oQuantity == 9) {
            System.out.println("Draw");
            return true;
        } else if (xWon && oWon) {
            System.out.println("Impossible");
            return true;
        } else {
            //System.out.println("Game not finished");
            return false;
        }

    }

    public static char[][] fillTheMatrix(String cells) {
        char[][] matrix = new char[3][3];
        int counter = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = cells.charAt(counter);
                counter++;
            }
        }
        return matrix;
    }

    public static String inputCells() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter cells: ");
        String cells = scanner.next();
        return cells;
    }

    public static void printGrid(String cells) {
        System.out.println("---------");
        int j = 1;
        for (int i = 0; i <= cells.length() - 1; i++) {

            if (j % 3 == 0) {
                System.out.print(" ");
                System.out.print(cells.charAt(i));
                System.out.print(" |");
                System.out.println();
            } else {
                if ((j - 1) % 3 == 0) {
                    System.out.print("|");
                }
                System.out.print(" ");
                System.out.print(cells.charAt(i));
            }
            j++;
        }
        System.out.println("---------");
    }

    public static void printGridFromMatrix(char[][] matrix) {
        System.out.println("---------");

        for (char[] ch : matrix) {
            System.out.print("| ");
            for (int j = 0; j < ch.length; j++) {
                System.out.print(ch[j]);
                System.out.print(" ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
        //greedPrints++;
    }

}
