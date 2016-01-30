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
    JButton playBtn = new JButton("Play");
    JButton resetBtn = new JButton("Reset");
    JButton loadBtn = new JButton("Load");
    JButton saveBtn = new JButton("Save");
    JButton solveBtn = new JButton("Solve");

    //Add listeners
    playBtnListener pblistener = new playBtnListener();
    playBtn.addActionListener(pblistener);
    resetBtnListener rblistener = new resetBtnListener();
    resetBtn.addActionListener(rblistener);
    solveBtnListener sblistener = new solveBtnListener();
    solveBtn.addActionListener(sblistener);
    loadBtnListener lblistener = new loadBtnListener();
    loadBtn.addActionListener(lblistener);
    saveBtnListener svblistener = new saveBtnListener();
    saveBtn.addActionListener(svblistener);

    //Add buttons to the container
    buttonContainer.add(playBtn);
    buttonContainer.add(resetBtn);
    buttonContainer.add(solveBtn);
    buttonContainer.add(loadBtn);
    buttonContainer.add(saveBtn);

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

  //Button listener for the play button
  class playBtnListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      game.clearGame();
      //repaint();
      game.playRandom(30);
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
  
  //Button listener for the load button
  class loadBtnListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      game.clearGame();
      boolean loadSucess = false;
      JFileChooser fileChooser = new JFileChooser();
      if( fileChooser.showOpenDialog(SudokuController.this) == JFileChooser.APPROVE_OPTION ) {
        File file = fileChooser.getSelectedFile();
        loadSuccess = game.loadFromFile(file);
      }
      if( loadSuccess )
        repaint();

    }
  }

  //Button listener for the save button
  class saveBtnListener implements ActionListener {

    //Save the game to a file
    public void saveToFile() throws FileNotFoundException, UnsupportedEncodingException {
      PrintWriter writer = new PrintWriter("save.txt", "UTF-8");
      for(int i = 0; i < game.numRows; i++ ) {
        for( int j = 0; j < game.numCols; j++ ) {
          writer.print(game.theBoard[i][j]);
          writer.print(" ");
          //System.out.println(game.theBoard[i][j]);
        }
        writer.print("\n");
      }
      writer.close();
    }

    public void actionPerformed(ActionEvent e) {
      try {
        saveToFile(); 
      } catch(FileNotFoundException ex) {
      } catch(UnsupportedEncodingException ex) {
      }
    }
  }
  
  class BoardCell extends JPanel {
    private int row;
    private int col;


    BoardCell(int r, int c) {
      row = r;
      col = c;
      PlayListener play = new PlayListener();
      addMouseListener(play);
      addKeyListener(play);
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
        g.setColor(Color.black);
        g.drawString(String.valueOf(game.theBoard[row][col]), 22,  45 );
        
      }

    }
    
    private void makeMove( int r, int c, int d ) {
      if( game.isValid(r, c, d) && d != 0 ) 
        game.setBoard(r, c, d);
      repaint();
    }

    //Listener
    class PlayListener implements MouseListener, KeyListener {
      public char key;
      int in;

      public void mouseClicked( MouseEvent e ) {
        requestFocus();
        //selected = true;
        repaint();
      }
      public void mousePressed( MouseEvent e ) {}
      public void mouseReleased( MouseEvent e ) {}
      public void mouseEntered( MouseEvent e ) {} 
      public void mouseExited( MouseEvent e ) {}

      public void keyTyped( KeyEvent e ) {  
        key = e.getKeyChar();
        in = Character.getNumericValue(key);
        makeMove(row, col, in);
      }
      public void keyPressed( KeyEvent e ) {} 
      public void keyReleased( KeyEvent e ) {}
    }
  }

  //Main driver
  public static void main(String[] args) {
    if( args.length != 0 ) {
      System.out.println("\n\t Usage: java SudokuController\n");
      return;
    }
    Sudoku game = new Sudoku();
    new SudokuController(game);
  }
}
