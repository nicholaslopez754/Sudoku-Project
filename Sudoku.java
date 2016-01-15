/* Sudoku Game
 * Author: Nicholas Lopez
 * Date: 11/22/2015
 *
 * TODO:
 * - check 3x3 surrounding
 *    - consolidate to an isValid() move
 * - Implement toString for game data
 * - Time took to solve
 * - Load from file
 * - Solver
 * - GUI / Animations
 * - Puzzle generator
 * - Save to file
 * - Reset
 */

import java.util.*;

public class Sudoku {
 
  public static int[][] theBoard;
  public static int[][] fixed;
  public static int[] numSet;
  public static int numRows = 9;
  public static int numCols = 9;
  public static int numMoves;

  //Subsets of the board matrix
  public static Set<Integer> s1, s2, s3, s4, s5, s6, s7, s8, s9;

  //Constructor
  public Sudoku() {

    //Instantiates the board for a char matrix
    theBoard = new int[numRows][numCols];
    fixed = new int[numRows][numCols];
    numSet = new int[numRows+1];
    numMoves = 0;

    //Instantiates the subsets
    s1 = new HashSet<Integer>();
    s2 = new HashSet<Integer>();
    s3 = new HashSet<Integer>();
    s4 = new HashSet<Integer>();
    s5 = new HashSet<Integer>();
    s6 = new HashSet<Integer>();
    s7 = new HashSet<Integer>();
    s8 = new HashSet<Integer>();
    s9 = new HashSet<Integer>();

    //Fills matrix with 0's as representation for empty
    for(int i = 0; i < numRows; i++) {
      for(int j = 0; j < numCols; j++) {
        theBoard[i][j] = 0;
        numSet[0]++;
      }
    }
    
  }
  

  //Sets the board with given args
  public void setBoard(int row, int col, int in) {
    //char temp = intToChar(in);
    int temp;
    if( fixed[row-1][col-1] == 0 ) {
      numSet[theBoard[row-1][col-1]]--; //Decrements the count of old item
      theBoard[row-1][col-1] =  in;
      numSet[in]++; //Increments the count of the new item
      //System.out.println("Number set at 0 is " + numSet[0]);
    }
    else
      System.out.println("Invalid move; this cell is in a fixed position");
  }

  //Checks if board is filled
  public boolean isFull() {
    //Checks the amount of empty spots
    if( numSet[0] > 0 )
      return false;
    else
      return true;
  }

  //Checks the row for valid move
  public boolean checkRow(int row, int in) {
    for(int i = 0; i < numRows; i++) {
      if( theBoard[row-1][i] == in ) {
        //System.out.println("Invalid move, try again. (col)");
        return false;
      }
    }
    return true;
  }

  //Checks the column for valid move
  public boolean checkColumn(int col, int in) {
    for(int i = 0; i < numCols; i++) {
      if( theBoard[i][col-1] == in ) {
        //System.out.println("Invalid move, try again. (col)");
        return false; 
      }
    }
    return true;
  }


  //Updates the board based on the args
  public void updateBoard(int row, int col, int in) {
    this.setBoard(row,col,in);
    this.printBoard();
    numMoves++;
  }

  //Method to print the ASCII game board
  public void printBoard() {

    //Prints the top border
    for(int k = 0; k < 25; k++)
          System.out.print("-");
    System.out.print("\n");

    //Prints the inside content & bottom border
    for(int i = 0; i < numCols; i++) {
      System.out.print("| ");
      for(int j = 0; j < numRows; j++) {
        if( theBoard[i][j] == 0 )
          System.out.print("* ");
        else
          System.out.print(theBoard[i][j] + " ");
        if( (j+1)%3 == 0)
          System.out.print("| ");
      }
      if( (i+1)%3 == 0 ) {
        System.out.print("\n");
        for(int k = 0; k < 25; k++)
          System.out.print("-");
      }
      System.out.print("\n");
    }
  }

  //toString to print data of game
  public String toString() {
    return ""; 
  }

  /* Checks if the element exists in the proper set
   * Returns true if it does not belong to its set 
   */
  public boolean checkSet(int row, int col, int in) {
    row -= 1;
    col -= 1;
    //Set 1
    if( 0 <= row < 3 && 0 <= col < 3) { 
      if( s1.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 2
    else if( 0 <= row < 3 && 3 <= col < 6) {
      if( s2.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 3
    else if( 0 <= row < 3 && 6 <= col < 9) {
      if( s3.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 4
    else if( 3 <= row < 6 && 0 <= col < 3) {
      if( s4.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 5
    else if( 3 <= row < 6 && 3 <= col < 6) {
      if( s5.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 6
    else if( 3 <= row < 6 && 6 <= col < 9) {
      if( s6.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 7
    else if( 6 <= row < 9 && 0 <= col < 3) {
      if( s7.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 8
    else if( 6 <= row < 9 && 3 <= col < 6) {
      if( s8.contains(in) ) 
        return false;
      else 
        return true;
    }
    //Set 9
    else {
      if( s9.contains(in) ) 
        return false;
      else 
        return true;
    }

  }

  //Combines all the check moves into 1 function
  public boolean isValid(int row, int col, int in) {
    if( checkRow(row,in) && checkCol(col,in) && checkSet(row,col,in ) )
      return true;
    else
      return false;
  }

  //Adds the element to the proper set
  public void addToSet(int row, int col, int in) {
  }

  //Easy puzzle to solve for testing
  public void easyPreset() {
    this.setBoard(1,1,6);
    this.setBoard(1,3,5);
    this.setBoard(1,4,7);
    this.setBoard(1,5,2);
    this.setBoard(1,8,3);
    this.setBoard(1,9,9);
    this.setBoard(2,1,4);
    this.setBoard(2,6,5);
    this.setBoard(2,7,1);
    this.setBoard(3,2,2);
    this.setBoard(3,4,1);
    this.setBoard(3,9,4);
    this.setBoard(4,2,9);
    this.setBoard(4,5,3);
    this.setBoard(4,7,7);
    this.setBoard(4,9,6);
    this.setBoard(5,1,1);
    this.setBoard(5,4,8);
    this.setBoard(5,6,9);
    this.setBoard(5,9,5);
    this.setBoard(6,1,2);
    this.setBoard(6,3,4);
    this.setBoard(6,5,5);
    this.setBoard(6,8,8);
    this.setBoard(7,1,8);
    this.setBoard(7,6,3);
    this.setBoard(7,8,2);
    this.setBoard(8,3,2);
    this.setBoard(8,4,9);
    this.setBoard(8,9,1);
    this.setBoard(9,1,3);
    this.setBoard(9,2,5);
    this.setBoard(9,5,6);
    this.setBoard(9,6,7);
    this.setBoard(9,7,4);
    this.setBoard(9,9,8);

    //Copy the board into the fixed set
    for( int i = 0; i < numRows; i++ ) {
      for( int j = 0; j < numCols; j++ ) 
        fixed[i][j] = theBoard[i][j];
    }
  }

  /* Main driver */
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    Scanner sc = new Scanner(System.in);

    int row, col, input;

    System.out.println("\n"+"Welcome to Sudoku!");
    game.easyPreset();
    game.printBoard();

    while(true) {
       System.out.println("Enter a row number (1-9)");
       row = sc.nextInt();
       System.out.println("Enter a column number (1-9)");
       col = sc.nextInt();
       System.out.println("Enter a number to place (1-9)");
       input = sc.nextInt();

       //Updates the game board & prints
       if( game.isValid(row, col, input) ) {
         game.updateBoard(row, col, input);
         if( game.isFull() )
           break;
       }
       else
          System.out.println("Invalid move. Please try again");
    }

    System.out.println("Finished!");
  }
}
