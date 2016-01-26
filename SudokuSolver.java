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
    public boolean isValidNode( Sudoku game ) {
      if( this.data == 0 )
        return false;
      else
        return game.isValid( this.c.x, this.c.y, this.data );
    }
    
    //Helper method that adds the node to the board
    public void addNode( Sudoku game ) {
      if( this.data != 0 ) 
        game.setBoard( this.c.x, this.c.y, this.data );
    }

    //Helper method that removes the node from the board
    public void removeNode( Sudoku game ) {
      if( this.data != 0 )
        game.clearCell( this.c.x, this.c.y );
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

    public void printContent() {
      System.out.print("(" + (x+1) + ", " + (y+1) + ")\n");
    }
  }

  //Builds the decision graph based on coordinates and nodes of possible decisions
  public List<dNode> buildGraph( Sudoku game ) {
    Map<Integer, List<dNode>> decisionMap = new HashMap<Integer, List<dNode>>(); 
    //List<Coordinate> cq = new ArrayList<Coordinate>();
    List<dNode> ret = new ArrayList<dNode>();
    int index = 0;
    int nodeCount = 0;

    //Sentinel/dummy node to act as a root
    Coordinate sentc = new Coordinate(-1,-1);
    dNode sentinel = new dNode( sentc, 0 );
    List<dNode> sentarr = new ArrayList<dNode>();
    sentarr.add( sentinel );
    decisionMap.put( index, sentarr );
    index++;

    //cq.add( sentc );

    //Creates a map of nodes
    for( int i = 0; i < numRows; i++ ) {
      for( int j = 0; j < numCols; j++ ) {
        if( game.fixed[i][j] == 0 ) {
          Coordinate curr = new Coordinate(i, j);
          List<dNode> keyList = new ArrayList<dNode>();
          for( int k = 1; k < numRows+1; k++ ) {
            dNode node = new dNode( curr, k );
            if( node.isValidNode(game) ) {
              keyList.add(node);
              nodeCount++;
              node.printContent();
            }
          }
          decisionMap.put( index, keyList );
          index++;
          //cq.add( curr );
        }
      }
    }
   
    System.out.println("Created " + nodeCount + " nodes.");

    //Sets the neighbors of each node
    index = 0;
    while( index < decisionMap.size()-1 ) { 
      List<dNode> nlist = decisionMap.get(index+1);
      for( dNode node : decisionMap.get(index) ) {
        node.neighbors = nlist;
      }
      index++;
    } 

    //Dumps the content of the map into a list to be traversed later
    for( int i = 0; i < decisionMap.size(); i++ ) {
       for( dNode node : decisionMap.get(i) )
         ret.add(node);
    }

    return ret;
  } 

  public void dfs( Sudoku game, List<dNode> graph ) {
    Deque<dNode> stack = new ArrayDeque<dNode>();
    System.out.print("Finding solution... ");
    
    //Adds sentinel to the stack
    dNode first, current, neighbor;
    first = graph.get(0);
    first.visited = true;
    stack.push(first);

    while( stack.size() > 0 ) {
      current = stack.pop();

      if( current.visited ) {
        current.removeNode(game);
      }

      if( current.isValidNode( game ) ) {
        current.addNode(game);
        game.printBoard();
      }
      if( game.isFull() ) {
        System.out.println("Done!");
        game.printBoard();
        return;
      }

      for( int i = 0; i < current.neighbors.size(); i++ ) {
        neighbor = current.neighbors.get(i);
        //neighbor.printContent();
        if( !neighbor.visited ) {
          neighbor.visited = true;
          //neighbor.printContent();
          stack.push(neighbor);
        }
      }
    }
    System.out.println("Failed.");
  } 

  //Main driver
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    File file = new File(args[0]);
    SudokuSolver ss = new SudokuSolver();
  
    List<dNode> graph;

    boolean loadSuccess;

    loadSuccess = game.loadFromFile(file);
    //game.toString(); 
  
    graph = ss.buildGraph(game);
    ss.dfs(game, graph);
  }
}
