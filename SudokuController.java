/* 
 * Sudoku Controller
 *
 * Author: Nicholas Lopez
 * Date: 1/28/16
 */

import javax.swing.*;
import javax.swing.event.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SudokuController extends JFrame {

  //Underlying game
  private Sudoku game;
  private boolean loadSuccess;
  
  //Constructor
  public SudokuController(Sudoku game) {

    //Make a new instances of sudoku
    this.game = game;
    //loadSuccess = game.LoadFromFile();
    

    JPanel buttonContainer = new JPanel();
    JButton resetBtn = new JButton("Reset");
    JButton loadBtn = new JButton("Load");
    JButton solveBtn = new JButton("Solve");

    //Add listeners
    resetBtnListener rblistener = new resetBtnListener();
    resetBtn.addActionListener(rblistener);
    solveBtnListener sblistener = new solveBtnListener();
    solveBtn.addActionListener(sblistener);
    //Add buttons to the container
    buttonContainer.add(resetBtn);
    buttonContainer.add(solveBtn);

    JPanel displayBoard = new JPanel();
    displayBoard.setLayout(new GridLayout(game.numRows, game.numCols));
    for( int i = 0; i < game.numRows; i++ ) {
      for( int j = 0; j < game.numCols; j++ ) {
        JPanel cell = new BoardCell(i,j);
        displayBoard.add(cell);
      }
    }

    //repaint();
    setLayout( new BorderLayout() );
    setTitle("Sudoku Solver");
    add( displayBoard, BorderLayout.CENTER );
    //add( resetBtn, BorderLayout.SOUTH );
    //add( solveBtn, BorderLayout.EAST );
    add( buttonContainer, BorderLayout.SOUTH );

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setSize(600,600);
    setResizable(false);
    setVisible( true );
    
    //repaint();
  }
  
  //Button listener for the reset button
  class resetBtnListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      game.clearBoard();
      repaint();
    }
  }

  //Button listener for the solve button
  class solveBtnListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      boolean solved;
      solved = game.backTrack();
      if( solved )
        repaint();
    }
  }

  class BoardCell extends JPanel {
    private int row;
    private int col;
    //private int data;

    //private int data = game.theBoard[row][col];
    BoardCell(int r, int c) {
      row = r;
      col = c;
      //Playlistener
      //Add mouse listener
    }

    public Dimension getPreferredSize() {
      return new Dimension( 50, 50 );
    }
    
    
    //Paint the board cell
    protected void paintComponent( Graphics g ) {
      super.paintComponent( g );

      //Background
      g.setColor(Color.white);
      g.fillRect(0,0, getWidth(), getHeight());

      //Shade the fixed positions
      if( game.fixed[row][col] != 0 ) {
        g.setColor(new Color(0,0,0, 40));
        g.fillRect(0, 0, getWidth(), getHeight());
      }

      //Cell
      g.setColor(Color.black);
      g.drawRect(0, 0, getWidth(), getHeight());
      
      
      Font cellfont = new Font("Arial", Font.PLAIN, 36);
      g.setFont(cellfont);
      if( game.theBoard[row][col] != 0 ) {
        g.drawString(String.valueOf(game.theBoard[row][col]), 22,  45 );
        //g.fillRect(0, 0, getWidth(), getHeight());
      }
    }
  }

  //Main driver
  public static void main(String[] args) {
    File file = new File(args[0]);
    if( args.length != 1 ) {
      System.out.println("\n\t Usage: java SudokuController [-file]\n");
      return;
    }
    Sudoku game = new Sudoku();
    boolean loadSuccess = game.loadFromFile(file);
    if( loadSuccess )
      new SudokuController(game);
  }
}
