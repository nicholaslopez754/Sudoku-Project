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

    boolean visited;

    public dNode( Coordinate coord, List<Integer> inList ) {
      c = coord; 
      inputs = inList;
      visited = false;
    }

    public void printContent() {
      System.out.print("Node(" + (c.x+1) + ", " + (c.y+1) + ")");
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
  public List<dNode> buildGraph(Sudoku game) {

    //Initializing structures
    Queue<dNode> nodes = new LinkedList<dNode>();
    List<dNode> ret = new ArrayList<dNode>();

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
      List<dEdge> curredges = new ArrayList<dEdge>();

      for( Integer i : current.inputs ) {
        next = nodes.peek();
        
        dEdge edge = new dEdge( current, next, i );
        edgecnt++;
        //edge.printContent();
        curredges.add(edge);
      }
      current.edges = curredges;
      ret.add(current);
    }
    System.out.println(emptycnt + " empty spots.");
    System.out.println("Created " + nodecnt + " nodes and " + edgecnt+ " edges.");
    return ret;
  }

  //DFS traversal of the nodes in te graph
  public List<dEdge> dfs(Sudoku game, List<dNode> graph) {
    Deque<dNode> stack = new ArrayDeque<dNode>();
    List<dEdge> ret = new ArrayList<dEdge>();

    dNode start;
    start = graph.get(0);
    start.visited = true;
    stack.push(start);
    
    while( stack.size() > 0 ) {
       dNode next = stack.pop();
      
       for(int i = 0; i < next.edges.size(); i++ ) {
         if( game.isValid(next.c.x, next.c.y, next.edges.get(i).data)) {
           game.setBoard(next.c.x, next.c.y, next.edges.get(i).data);
           System.out.println("Added " + next.edges.get(i).data + " in (" + (next.c.x+1) + ", " + (next.c.y+1) + ")");
           break;
         }
       }

       for(int i = 0; i < next.edges.size(); i++) {
         dNode neighbor = next.edges.get(i).end;

         if( neighbor == null ) {
           System.out.println("broke");
           break;
         }
         if( !neighbor.visited && game.isValid(neighbor.c.x, neighbor.c.y, next.edges.get(i).data )) {
           game.setBoard(neighbor.c.x, neighbor.c.y, next.edges.get(i).data);
           System.out.println("Added " + next.edges.get(i).data + " in (" + (neighbor.c.x+1) + ", " + (neighbor.c.y+1) + ")");
           neighbor.visited = true;
           next.edges.get(i).printContent();
           ret.add(next.edges.get(i));
           stack.push(neighbor);
         }

       }
    }

    return ret;

  }

  //Main driver
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    File file = new File(args[0]);
    SudokuSolver ss = new SudokuSolver();
  
    List<dNode> graph;
    List<dEdge> solutionSet;

    boolean loadSuccess;

    loadSuccess = game.loadFromFile(file);
    //game.toString(); 
  
    graph = ss.buildGraph(game);
    solutionSet = ss.dfs(game, graph);
  }
}
