
import java.io.*;
import java.util.*;

/**
 * Pentominoes.java
 *
 * Cameron Bradley - 3344991
 * Castipher McSkimming - 8287490
 * Jacob Cone - 3977920
 * Luke Tang - 4258935
 *
 */
public class Pentominoes {

    private static List<List<Character>> puzzleBoard = new ArrayList<>();
    private static List<List<Character>> emptyPuzzleBoard = new ArrayList<>();

    private static String firstLine;

    private static boolean limited = true;
    private static int depth = 0;

    /**
     * Main method that attempts to solve a pentomino puzzle.
     * 
     * @param args the list of arguments. (unused) 
     *
     */ 
    public static void main(String[] args) throws FileNotFoundException {
        List<Pentomino> pentominoes = new ArrayList<>();
        // Map<Character, Integer> sortedPentominoes = new HashMap<>();

        int lineCount = 0;
        int lineLength = 0;

        int pentominoSquares = 0;
        int fillableSquares = 0;

        // read input file
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            // read the first line of input
            if (lineCount == 0) {
                firstLine = new String(line);

                for (int l = 0; l < line.length(); l++) {
                    char letter = line.charAt(l);

                    if (letter == '*') {
                        limited = false;
                        pentominoSquares = -1;
                    } else if (letter >= 79 && letter <= 90) {
                        pentominoSquares += 5;

                        pentominoes.add(new Pentomino(letter));

                        // Integer value = sortedPentominoes.put(letter, 1);
                        // if (value != null) {
                        //     sortedPentominoes.put(letter, ++value);
                        // }
                    } else {
                        System.out.println("Bad input");
                        System.exit(1);
                    }
                }
            }
            lineCount += 1;

            // read the puzzle grid
            if (lineCount > 1) {
                List<Character> gridLine = new ArrayList<>();
                for (int l = 0; l < line.length(); l++) {
                    char square = line.charAt(l);

                    if (square == '.') {
                        fillableSquares++;
                    } else if (square == '*') {
                        // do nothing
                    } else {
                        System.out.println("Bad input");
                        System.exit(1);
                    }

                    gridLine.add(square);
                }
                puzzleBoard.add(gridLine);
            }

            lineLength = line.length();

        }
        lineCount -= 1; // as it counts the line with the letters

        emptyPuzzleBoard = copyPuzzleBoard(); // for impossible solutions

        // System.out.println(pentominoSquares + " pentomino squares");
        // System.out.println(fillableSquares + " fillable squares");

        // stop immediately if we know this is unsolvable
        if (fillableSquares % 5 != 0 || (pentominoSquares >= 0 && fillableSquares > pentominoSquares)) {
            declareImpossible();
        }

        List<List<Character>> solution = solve(puzzleBoard, pentominoes);

        if (solution != null) {
            declareSolution(solution);
        } else {
            declareImpossible();
        }
    }

    /**
     * Declare that a solution has been found.
     * 
     * @param solution a solved pentomino puzzle.
     * 
     */ 
    static void declareSolution(List<List<Character>> solution) {
        System.out.println(firstLine);
        printState(solution);
        System.exit(0);
    }

    /**
     * Declare that a pentomino puzzle is impossible to solve.
     * 
     */
    static void declareImpossible() {
        System.out.println(firstLine);
        printState(emptyPuzzleBoard);
        System.out.println("Impossible");
        System.exit(0);
    }

    /**
     * Recursively solves a pentomino puzzle by making sure that the next
     * pentomino piece can cover the uppermost and leftmost square that is
     * currently uncovered. 
     * 
     * @param puzzleBoard the grid of the pentomino puzzle to solve.
     * @param pieces the pieces currently available to use.
     * 
     * @return a solved puzzle if one was found, or null if none was found
     *
     */
    static List<List<Character>> solve(List<List<Character>> puzzleBoard, List<Pentomino> pieces) {
        // check if the pain has ended
        if (isSolved(puzzleBoard)) {
            depth--;
            return puzzleBoard;
        }
        // otherwise, we must persevere...

        int rFree = 0;
        int cFree = 0;

        // find the uppermost and leftmost square that is currently uncovered
        dotFinder:
        for (int r = 0; r < puzzleBoard.size(); r++) {
            for (int c = 0; c < puzzleBoard.get(r).size(); c++) {
                if (puzzleBoard.get(r).get(c) == '.') {
                    rFree = r;
                    cFree = c;
                    break dotFinder;
                }
            }
        }

        // System.out.println("[" + rFree + "][" + cFree + "] is free");

        // systematically solve the pentomino puzzle
        for (int i = 0; i < pieces.size(); i++) {
            Pentomino p = pieces.get(i);
            // System.out.println("Trying " + p);

            // try every way we can place the piece
            everyPiece:
            while (p.hasCombinations()) {
                while (p.canCyclePivots()) {
                    // System.out.println("Depth: " + depth);

                    // try to place a piece this way
                    List<List<Character>> newPuzzleBoard = placePiece(p, rFree, cFree, puzzleBoard);

                    // check if the placement was successful
                    if (newPuzzleBoard != null) {
                        // printState(newPuzzleBoard);
                        // System.out.println();

                        // create a new list of pieces, with the active piece removed or replaced
                        List<Pentomino> newPieces = new ArrayList<>(pieces);

                        if (limited) {
                            newPieces.remove(i);
                        } else {
                            // replace the pentomino with a different pentomino with the same letter
                            newPieces.set(i, new Pentomino(p.getType()));
                        }

                        // recursively find a solution
                        depth++;
                        List<List<Character>> sol = solve(newPuzzleBoard, newPieces);

                        // we found a solution!!!
                        if (sol != null) {
                            depth--;
                            return sol;
                        }
                    }

                    p.cyclePivot();
                }
                p.resetPivot();

                p.nextCombination();
            }

            p.resetCombinations();
        }

        depth--;
        return null;
    }

    /**
     * Prints the current state of the puzzle board.
     *
     */
    static void printState() {
        for (int i = 0; i < puzzleBoard.size(); i++) {
            for (int j = 0; j < puzzleBoard.get(i).size(); j++) {
                System.out.print(puzzleBoard.get(i).get(j));
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    /**
     * Prints the state of the puzzle board that is passed in.
     *
     */
    static void printState(List<List<Character>> state) {
        for (int i = 0; i < state.size(); i++) {
            for (int j = 0; j < state.get(i).size(); j++) {
                System.out.print(state.get(i).get(j));
            }
            System.out.print("\n");
        }
    }

    /**
     * Places a pentomino piece on the passed in puzzle board at the given row
     * and column position.
     *
     * @param p the pentomino piece to place.
     * @param r the row index to place the piece on.
     * @param c the column index to place the piece on.
     * @param puzzleBoard the puzzle board to place the piece on.
     *
     * @return a puzzle board with a newly placed piece or null if placement
     * failed
     *
     */
    static List<List<Character>> placePiece(Pentomino p, int r, int c, List<List<Character>> puzzleBoard) {
        // System.out.println("Place at " + "[" + r + "][" + c + "]");

        // set the position of the piece for future operations
        int[] pos = new int[2];
        pos[0] = r;
        pos[1] = c;
        p.setPos(pos);

        puzzleBoard = copyPuzzleBoard(puzzleBoard);

        boolean successful = true;

        try {
            // get relevant info for the piece
            int[][] coords = p.getCoords();
            int rPivot = coords[p.getPivot()][0];
            int cPivot = coords[p.getPivot()][1];

            // place the piece, one square at a time
            for (int i = 0; i < 5; i++) {

                // compute based on differences between each coordinate
                int dr = coords[i][0] - rPivot;
                int dc = coords[i][1] - cPivot;

                // System.out.println(i + ": placing with pivot at " + p.getPivot());
                // System.out.println("[" + r + dr + "][" + c + dc + "]");
                if (puzzleBoard.get(r + dr).get(c + dc) == '.') {
                    puzzleBoard.get(r + dr).set(c + dc, p.getType());
                } else {
                    // System.out.println("Negative 1");
                    successful = false;
                    break;
                }
            }

        } catch (IndexOutOfBoundsException e) {
            // System.out.println("Not enough space for " + p.getType() + " at [" + r + "][" + c + "]");
            // System.out.println("Negative 2");
            successful = false;
        }

        if (successful) {
            // System.out.println("Placed " + p.getType() + " at [" + r + "][" + c + "]" + " with pivot " + p.getPivot());
            return puzzleBoard;
        }

        return null;
    }

    /**
     * Check whether the puzzle grid has been solved.
     *
     * For a puzzle to be solved, all empty spaces, and only the empty spaces
     * must be filled.
     *
     * "In particular: no piece can cover a filled space, no piece can project
     * over the edge of the board, and no two pieces can overlap."
     *
     * @return a boolean indicating whether the puzzle grid was solved
     *
     */
    static boolean isSolved() {
        for (int i = 0; i < puzzleBoard.size(); i++) {
            for (int j = 0; j < puzzleBoard.get(i).size(); j++) {
                if (puzzleBoard.get(i).get(j) == '.') {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check whether the puzzle grid has been solved.
     *
     * For a puzzle to be solved, all empty spaces, and only the empty spaces
     * must be filled.
     *
     * "In particular: no piece can cover a filled space, no piece can project
     * over the edge of the board, and no two pieces can overlap."
     *
     * @return a boolean indicating whether the puzzle grid was solved
     *
     */
    static boolean isSolved(List<List<Character>> puzzleBoard) {
        for (int i = 0; i < puzzleBoard.size(); i++) {
            for (int j = 0; j < puzzleBoard.get(i).size(); j++) {
                if (puzzleBoard.get(i).get(j) == '.') {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns a copy of the puzzle board in its current state. If the puzzle
     * board is empty, then null is returned.
     *
     * @return a copy of the puzzle board in a specific state.
     *
     */
    static List<List<Character>> copyPuzzleBoard() {
        if (puzzleBoard.isEmpty()) {
            return null;
        }

        // make copy of array list
        List<List<Character>> puzzleBoardCopy = new ArrayList<>(puzzleBoard.size());
        for (int i = 0; i < puzzleBoard.size(); i++) {
            puzzleBoardCopy.add(new ArrayList<>(puzzleBoard.get(i)));
        }

        return puzzleBoardCopy;
    }

    /**
     * Returns a copy of the puzzle board that is passed in. If the puzzle board
     * is empty, then null is returned.
     *
     * @param puzzleBoard the puzzle board to copy
     *
     * @return a copy of the puzzle board that is passed in.
     *
     */
    static List<List<Character>> copyPuzzleBoard(List<List<Character>> puzzleBoard) {
        if (puzzleBoard.isEmpty()) {
            return null;
        }

        // make copy of array list
        List<List<Character>> puzzleBoardCopy = new ArrayList<>(puzzleBoard.size());
        for (int i = 0; i < puzzleBoard.size(); i++) {
            puzzleBoardCopy.add(new ArrayList<>(puzzleBoard.get(i)));
        }

        return puzzleBoardCopy;
    }

}
