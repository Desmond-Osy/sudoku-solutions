
import java.io.*;
import java.util.*;

public class Sudoku {

    static final int EMPTY_SQUARE = -1;
    public static int[][] createPuzzle(char[] array) {
        //contract: converts the single dimensional array of characters to a 9x9 array of integers
        //This function replaces the * or - characters from array to -1 integer
        int[][] puzzle = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if ((array[(i * 9) + j] == '*') || (array[(i * 9) + j] == '-')) {
                    puzzle[i][j] = -1;
                } else {
                    puzzle[i][j] = (int) array[(i * 9) + j] - (int) '0';
                }

            }
        }
        return puzzle;
    }

    public static char[] readFile(String fileName) {
        File file = new File(fileName);
        char[] array = new char[81];
        int ch;
        StringBuffer strContent = new StringBuffer("");
        FileInputStream fin = null;

        try {

            fin = new FileInputStream(file);

            int count = 0;
            while ((ch = fin.read()) != -1) {
                if (ch != 10 && ch != 13 && ch != 32) {
                    array[count] = ((char) ch);
                    count++;
                }
            }
            fin.close();

        } catch (FileNotFoundException e) {
            System.out.println("File " + file.getAbsolutePath()
                    + " could not be found on filesystem");
        } catch (IOException ioe) {
            System.out.println("Exception while reading the file" + ioe);
        }

        return array;
    }

    public static void printPuzzle(int[][] puzzle) {
        
        for(int i = 0; i < 9; i++){
            //put some braces
            System.out.print("[");
            for(int j = 0; j < 9; j++){
                System.out.print(" " + (puzzle[i][j] == -1? "*": puzzle[i][j]) + " ");
            }
            System.out.print("]");
            System.out.println();
        }

        System.out.println("");
    }

    public static boolean isValid(int[][] puzzle) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                for (int k = j + 1; k < 9; ++k) {
                    if (puzzle[i][j] != -1 && puzzle[i][j] == puzzle[i][k]) {
                        return false;
                    }
                    if (puzzle[j][i] != -1 && puzzle[j][i] == puzzle[k][i]) {
                        return false;
                    }
                    if (puzzle[j % 3 + (i % 3) * 3][j / 3 + (i / 3) * 3] != -1
                            && puzzle[j % 3 + (i % 3) * 3][j / 3 + (i / 3) * 3]
                            == puzzle[k % 3 + (i % 3) * 3][k / 3 + (i / 3) * 3]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
  public static boolean isComplete(int[][] thePuzzle){
    boolean result = true;
      for(int r = 0; r < thePuzzle.length; ++r){
	  for(int c = 0; c < thePuzzle[r].length; ++c){
	      if( EMPTY_SQUARE == thePuzzle[r][c]){
	          return false;

	      }
	    }
         }
	 return result;
	}

    public static boolean solve(int[][] puzzle) {
     if(isComplete(puzzle) && isValid(puzzle)) return true;
     else if (!isValid(puzzle)) return false;               //puzzle is wrong so return false(this might be the case when the puzzle has repeating numbers in a column or a row
        int x = -1;
        int y = -1;
        int min = 10;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (puzzle[i][j] == -1) {
                    int c = 0;
                    //----------count the number of posible integers that can fit in empty square-----
                    for (int k = 1; k <= 9; ++k) {
                        puzzle[i][j] = k;
                        if (isValid(puzzle)) {
                            ++c;
                        }
                        puzzle[i][j] = -1;     //restore cell to it's empty state
                    }
                    //end count
                    if (min > c) {
                        min = c;
                        x = i;
                        y = j;
                    }
                }
            }
        }
        if (x == -1) {
            return true;
        }
        for (int k = 1; k <= 9; ++k) {
            puzzle[x][y] = k;
            if (isValid(puzzle)) {
                if (solve(puzzle)) {
                    return true;
                }
            }
        }
        puzzle[x][y] = -1;
        return false;
    }

    public static void main(String[] args) {
        String fileName = "";
        if (args.length == 0) {
            System.out.print("Enter the name of the file that contains the puzzle: ");
            Scanner s = new Scanner(System.in);
            fileName = s.nextLine();
        } else if (args.length == 1) {
            fileName = args[0];
        }

        int[][] thePuzzle = createPuzzle(readFile(fileName));
        System.out.println();
        System.out.println();
        System.out.println("-------------------PUZZLE INPUT-----------------");
        printPuzzle(thePuzzle);

        if (solve(thePuzzle)) {
            System.out.println("Solution:");
            printPuzzle(thePuzzle);
        } else {
            System.out.println("No solution for this puzzle");
        }
    }
}
