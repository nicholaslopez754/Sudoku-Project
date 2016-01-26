import java.util.*;
import java.io.*;

public class SudokuSolver extends Sudoku {


  //Constructor
  public SudokuSolver() { }

  //Sub-class of nodes that will represent possible decisions to make
  public class dNode {

    //The coordinates in the matrix of the decision to be made
    Coordinate c;
    
    //The move to make at the coordinate
    int data;

    //Flag useful for traversals
    boolean visited;
  
    //List of neighbors
    List<dNode> neighbors;

    //Constructor
    public dNode( Coordinate coord, int datain ) {
      c = coord;
      data = datain;
      neighbors = new ArrayList<dNode>();
      visited = false;
    }

    //Prints the content of the node for debugging
    public void printContent() {
      System.out.println("Node(" + (c.x+1) + ", " + (c.y+1) + "): " + data);
    }
    
    //Helper method to check if the node can be added to the board
    public boolean isValidNode( Sudoku game, dNode node ) {
      return game.isValid( node.c.x, node.c.y, node.data );
    }
    
    //Helper method that adds the node to the board
    public void addNode( Sudoku game, dNode node ) {
      game.setBoard( node.c.x, node.c.y, node.data );
    }
  }
  
  //Helper class to serve as a pair of coordinates
  public class Coordinate {
    int x; 
    int y;

    public Coordinate(int first, int second) {
      x = first;
      y = second;
    }
  }

  //Builds the decision graph based on nodes and edges of possible decisions
  public void buildGraph( Sudoku game ) {
    Map<Coordinate, List<dNode>> decisionmap = new HashMap<Coordinate, List<dNode>>(); 
    Queue<Coordinate> cq = new LinkedList<Coordinate>();
  }

  public void dfs(Sudoku game) {} 

  //Main driver
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    File file = new File(args[0]);
    SudokuSolver ss = new SudokuSolver();
  
    Queue<dNode> graph;

    boolean loadSuccess;

    loadSuccess = game.loadFromFile(file);
    //game.toString(); 
  
    //graph = ss.buildGraph(game);
    //solutionSet = ss.dfs(game, graph);
  }
}
