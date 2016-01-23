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
    Coordinate c;

    //List of possible inputs
    List<Integer> inputs;
    List<dEdge> edges;

    //boolean visited;

    public dNode( Coordinate coord, List<Integer> inList ) {
      c = coord; 
      inputs = inList;
      //visited = false;
    }

    public void printContent() {
      System.out.print("Node(" + (c.x+1) + ", " + (c.y+1) + "): ");
      for( int i = 0; i < inputs.size(); i++ )
        System.out.print(inputs.get(i) + " ");
      System.out.print("\n");
    }
    
  }
  
  //Sub-class of edges between the nodes that represents the decision made
  public class dEdge {
    dNode start, end;
    int data;
    boolean visited;

    public dEdge ( dNode a, dNode b, int in ) {
      start = a;
      end = b;
      data = in;
      visited = false;
    }

    public void printContent() {
      System.out.print("Edge between Node(" + (start.c.x+1) + ", " + (start.c.y+1) + ") and ");
      if( end != null ) 
        System.out.print("Node(" + (end.c.x+1) + ", " + (end.c.y+1) + ")");
      else
        System.out.print("end of graph");
      System.out.print(" with value " + data + "\n");

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
  public Queue<dEdge> buildGraph(Sudoku game) {

    //Initializing structures
    Queue<dNode> nodes = new LinkedList<dNode>();
    Queue<dEdge> ret = new LinkedList<dEdge>();

    Coordinate curr;
    int nodecnt = 0;
    int emptycnt = 0;
    //Generates all the nodes
    for( int i = 0; i < numRows; i++ ) {
      for( int j = 0; j < numCols; j++ ) {
        if( game.fixed[i][j] == 0 ) {
          curr = new Coordinate(i,j);
          emptycnt++;
          List<Integer> in = new ArrayList<Integer>();
          for( int k = 1; k < numRows+1; k++ ) {
            if( isValid( curr.x, curr.y, k ) ) 
              in.add(k);            
          }
          dNode node = new dNode(curr, in);
          //node.printContent();
          nodecnt++;
          nodes.add(node);
        }
      }
    }

    //Creates edges for each consecutive node in queue
    dNode current, next;
    int edgecnt = 0;
    while( nodes.size() > 0 ) {
      current = nodes.poll();

      for( Integer i : current.inputs ) {
        next = nodes.peek();
        
        dEdge edge = new dEdge( current, next, i );
        ret.add(edge);
        edgecnt++;
        //edge.printContent();
      }
    }
    System.out.println(emptycnt + " empty spots.");
    System.out.println("Created " + nodecnt + " nodes and " + edgecnt + " edges.");
    return ret;
  }

  //DFS traversal of the nodes in te graph
  public List<dEdge> dfs(Sudoku game, Queue<dEdge> graph) {
    Deque<dNode> stack = new ArrayDeque<dNode>();
    List<dEdge> ret = new ArrayList<dEdge>();
    
    dNode first, next, neighbor;
    first = graph.poll()
    first.visited = true;
    game.setBoard( first.start.c.x, first.start.c.y, first.data );
    return ret;

  }

  //Main driver
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    File file = new File(args[0]);
    SudokuSolver ss = new SudokuSolver();
  
    Queue<dEdge> graph;
    List<dEdge> solutionSet;

    boolean loadSuccess;

    loadSuccess = game.loadFromFile(file);
    //game.toString(); 
  
    graph = ss.buildGraph(game);
    solutionSet = ss.dfs(game, graph);
  }
}
