/* Sudoku Game
 * Author: Nicholas Lopz
 * Date: 11/22/2015
 *
 * TODO:
 * - Solver
 * - Main class
 * - GUI / Animations
 * - Puzzle generator
 * - Save to file
 * - Reset
 */

import java.util.*;
import java.io.*;

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
    if( fixed[row][col] == 0 ) { //Here
      numSet[theBoard[row][col]]--; //Here
      theBoard[row][col] =  in; //Here
      numSet[in]++;
      addToSet(row,col,in);
    }
    else
      System.out.println("Invalid move; this cell cannot be changed.");
  }

  //Clears the cell in the board
  public void clearCell(int row, int col) { 
    if( fixed[row][col] == 0 && theBoard[row][col] != 0 ) {
      numSet[theBoard[row][col]]--;
      theBoard[row][col] = 0;
    }
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
      if( theBoard[row][i] == in ) { //Here
        //System.out.println("Invalid move; input number exists in this row.");
        return false;
      }
    }
    return true;
  }

  //Checks the column for valid move
  public boolean checkColumn(int col, int in) {
    for(int i = 0; i < numCols; i++) {
      if( theBoard[i][col] == in ) { //Here
        //System.out.println("Invalid move; input number exists in this column");
        return false; 
      }
    }
    return true;
  }

  //Updates the board based on the args
  public void updateBoard(int row, int col, int in) {
    this.setBoard(row,col,in);
    this.printBoard();
  }

  //Method to print the ASCII game board
  public void printBoard() {
    
    System.out.print("\n");
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
    System.out.print("\n");
  }

  //toString to print data of game
  public String toString() {
    for(int i = 0; i < numSet.length; i++)
      System.out.println("Number of " + i + ": " + numSet[i]);

    System.out.println("\nNumber of moves taken so far: " + numMoves + "\n");
    return null;
  }

  /* Checks if the element exists in the proper set
   * Returns true if it does not belong to its set 
   */
  public boolean checkSet(int row, int col, int in) {
    //row -= 1;
    //col -= 1;
    boolean flag = true;
    //Set 1
    if( 0 <= row && row < 3 && 0 <= col && col < 3) { 
      if( s1.contains(in) ) 
        flag = false;
    }
    //Set 2
    else if( 0 <= row && row < 3 && 3 <= col && col < 6) {
      if( s2.contains(in) ) 
        flag = false;
    }
    //Set 3
    else if( 0 <= row && row < 3 && 6 <= col && col < 9) {
      if( s3.contains(in) ) 
        flag = false;
    }
    //Set 4
    else if( 3 <= row && row < 6 && 0 <= col && col < 3) {
      if( s4.contains(in) ) 
        flag = false;
    }
    //Set 5
    else if( 3 <= row && row < 6 && 3 <= col && col < 6) {
      if( s5.contains(in) ) 
        flag = false;
    }
    //Set 6
    else if( 3 <= row && row < 6 && 6 <= col && col < 9) {
      if( s6.contains(in) ) 
        flag = false;
    }
    //Set 7
    else if( 6 <= row && row < 9 && 0 <= col && col < 3) {
      if( s7.contains(in) ) 
        flag = false;
    }
    //Set 8
    else if( 6 <= row && row < 9 && 3 <= col && col < 6) {
      if( s8.contains(in) ) 
        flag = false;
    }
    //Set 9
    else {
      if( s9.contains(in) ) 
        flag = false;
    }

    //if( !flag )
      //System.out.println("Invalid move; input number exists in local block.");
    return flag;
  }

  //Combines all the check moves into 1 function
  public boolean isValid(int row, int col, int in) {
    if( checkRow(row,in) && checkColumn(col,in) && checkSet(row,col,in ) )
      return true;
    else
      return false;
  }

  //Adds the element to the proper set
  public void addToSet(int row, int col, int in) {
    //row -= 1;
    //col -= 1;
    //Set 1
    if( 0 <= row && row < 3 && 0 <= col && col < 3) { 
      if( theBoard[row][col] != 0 )
        s1.remove(theBoard[row][col]);
      s1.add(in);
    }
    //Set 2
    else if( 0 <= row && row < 3 && 3 <= col && col < 6) {
      if( theBoard[row][col] != 0 )
        s2.remove(theBoard[row][col]);
      s2.add(in);
    }
    //Set 3
    else if( 0 <= row && row < 3 && 6 <= col && col < 9) {
      if( theBoard[row][col] != 0 )
        s3.remove(theBoard[row][col]);
      s3.add(in);
    }
    //Set 4
    else if( 3 <= row && row < 6 && 0 <= col && col < 3) {
      if( theBoard[row][col] != 0 )
        s4.remove(theBoard[row][col]);
      s4.add(in);
    }
    //Set 5
    else if( 3 <= row && row < 6 && 3 <= col && col < 6) {
      if( theBoard[row][col] != 0 )
        s5.remove(theBoard[row][col]);
      s5.add(in);
    }
    //Set 6
    else if( 3 <= row && row < 6 && 6 <= col && col < 9) {
      if( theBoard[row][col] != 0 )
        s6.remove(theBoard[row][col]);
      s6.add(in);
    }
    //Set 7
    else if( 6 <= row && row < 9 && 0 <= col && col < 3) {
      if( theBoard[row][col] != 0 )
        s7.remove(theBoard[row][col]);
      s7.add(in);    
    }
    //Set 8
    else if( 6 <= row && row < 9 && 3 <= col && col < 6) {
      if( theBoard[row][col] != 0 )
        s8.remove(theBoard[row][col]);
      s8.add(in);       
    }
    //Set 9
    else {
      if( theBoard[row][col] != 0 )
        s9.remove(theBoard[row][col]);
      s9.add(in);       
    }
  }

  /* Load from a text file that contains 81 integers. 
   * Each integer will fill the next spot in the row 
   * until the board is filled. 
   *
   * By convention, the integers will range
   * from 1-9, while an empty space is denoted by a 0
   */
  public boolean loadFromFile(File file) {
    try {
      Scanner scanner = new Scanner(file);
      int temp;
      while(scanner.hasNextInt()) {
        for(int i = 0; i < numRows; i++) {
          for(int j = 0; j < numCols; j++) {
            temp = scanner.nextInt();
            if( temp < 0 || temp > 9 ) {
              System.out.println("\n\tFile must contain only integers ranging"
                                 + " from 1 to 9. Now exiting...\n");
              scanner.close();
              return false;
            }
            else
              this.setBoard(i, j, temp);
          }
        }
      }

      //Copy the board into the fixed set
      for( int i = 0; i < numRows; i++ ) {
        for( int j = 0; j < numCols; j++ ) 
          fixed[i][j] = theBoard[i][j];
      }
      
      scanner.close();
      return true;
    }
    catch(FileNotFoundException ex) {
      System.out.println("\n\tCannot find " + file.getName() +"\n");
      return false;
    }
  }

  /* Main driver */
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    Scanner sc = new Scanner(System.in);
    File file = new File(args[0]);

    int row, col, input;
    boolean loadSuccess;

    //Sets the boolean if load was successful
    loadSuccess = game.loadFromFile(file);
   
    //Only starts the game if loading the file was successful
    if( loadSuccess ) {
      System.out.println("\n"+"Welcome to Sudoku!");
      game.printBoard();
      
      //Starts a time counter
      final long startTime = System.currentTimeMillis();

      //Runs until the board is full with valid moves
      while(true) {
        System.out.println("Enter a row number (1-9)");
        row = sc.nextInt()-1;
        if( row < 0 || row > 8 )
          continue;

        System.out.println("Enter a column number (1-9)");
        col = sc.nextInt()-1;
        if( col < 0 || col > 8 )
          continue;

        System.out.println("Enter a number to place (1-9)");
        input = sc.nextInt();
        if( input < 1 || input > 9 )
          continue;
        
        numMoves++;

        //Updates the game board & prints
        if( game.isValid(row, col, input) ) {
          game.updateBoard(row, col, input);
          if( game.isFull() )
             break;
        }
        else  
            game.printBoard();

        //game.toString();
      }

      //Time calculations
      final long endTime = System.currentTimeMillis();
      final long elapsedTime = endTime - startTime;
      int seconds = (int) (elapsedTime/1000) % 60;
      int minutes = (int) (elapsedTime/(1000*60)) % 60;

      System.out.println("Finished with " + numMoves + " moves in " +
                         minutes + " minutes and " + seconds + " seconds!\n");
      sc.close();
    }
  }
}
