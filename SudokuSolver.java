import java.util.*;
import java.io.*;

public class SudokuSolver extends Sudoku {

  public static List<dEdge> decisionGraph;
  public static int[][] toSolve;

  //Constructor
  public SudokuSolver() {
    //decisionGraph = buildGraph();
    toSolve = new int[numRows][numCols];
  }

  //Sub-class of nodes that will represent possible decisions to make
  public class dNode {

    //The coordinates in the matrix of the decision to be made
    int x,y;

    //List of possible inputs
    List<Integer> inputs;

    public dNode( int i, int j, List<Integer> inList ) {
      x = i;
      y = j;
      inputs = inList;
    }

    public void printContent() {
      System.out.print("Node: (" + (x+1) + ", " + (y+1) + "): ");
      for( int i = 0; i < inputs.size(); i++ )
        System.out.print(inputs.get(i) + " ");
      System.out.print("\n");
    }
  }
  
  //Sub-class of edges between the nodes that represents the decision made
  public class dEdge {
    dNode start, end;
    int data;

    public dEdge ( dNode a, dNode b, int in ) {
      start = a;
      end = b;
      data = in;
    }
  }

  //Builds the decision graph based on nodes and edges of possible decisions
  public List<dEdge> buildGraph(Sudoku game) {
    List<dEdge> graph = new ArrayList<dEdge>();

    //Generates all the nodes
    for( int i = 0; i < numRows; i++ ) {
      for( int j = 0; j < numCols; j++ ) {
        if( game.fixed[i][j] == 0 ) {
          List<Integer> in = new ArrayList<Integer>();
          for( int k =  1; k < numRows+1; k++ ) {
            if( game.isValid(i, j, k) ) {
              in.add(k);
            }
          }
        dNode node = new dNode(i, j, in); 
        node.printContent();
        }
      }
    }
    return graph;
  }

  //Main driver
  public static void main(String[] args) {
  Sudoku game = new Sudoku();
  File file = new File(args[0]);
  SudokuSolver ss = new SudokuSolver();

  boolean loadSuccess;

  loadSuccess = game.loadFromFile(file);
  //game.toString(); 
  
  decisionGraph = ss.buildGraph(game);
  }
}
