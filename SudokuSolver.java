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
      System.out.print("Node(" + (c.x+1) + ", " + (c.y+1) + "): ");
      for( int i = 0; i < inputs.size(); i++ )
        System.out.print(inputs.get(i) + " ");
      System.out.print("\n");
      /*if( edges != null ) {
        System.out.print("\n\tEdges: ");
        for( int i = 0; i < edges.size(); i++ )
          System.out.print(edges.get(i).data + " ");
        System.out.print("\n");
      }*/
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
  public Queue<dNode> buildGraph(Sudoku game) {

    //Initializing structures
    Queue<dNode> nodes = new LinkedList<dNode>();
    Queue<dNode> retnodes = new LinkedList<dNode>();

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
          for( int m = 0; m < in.size(); m++ ) {
            dNode node = new dNode(curr, in);
            //node.printContent();
            nodecnt++;
            nodes.add(node);
          }
        }
      }
    }

    //Creates edges for each consecutive node in queue
    dNode current, next;
    int edgecnt = 0;
    int idx;
    List<dNode> copies = new ArrayList<dNode>();
    //List<dNode> retnodes = new ArrayList<dNode>();

    while( nodes.size() > 0 ) {
      current = nodes.poll();
      next = nodes.peek();
      if( current == null )
        break;
      if( next == null ) {
        List<dEdge> finaledges = new ArrayList<dEdge>();
        for( Integer i : current.inputs ) { 
         dEdge edge = new dEdge( current, null, i );
         finaledges.add(edge);
         edgecnt++;
         edge.printContent();
        }
        current.edges = finaledges;
        break;
      }
      copies.add(current);
      retnodes.add(current);
      while( current.c.x == next.c.x && current.c.y == next.c.y ) {
        nodes.poll();
        current = next;
        next = nodes.peek();
        copies.add(current);
        retnodes.add(current);
      }
      List<dEdge> edgelist = new ArrayList<dEdge>();
      for( Integer i : current.inputs ) { 
        dEdge edge = new dEdge( current, next, i );
        edgelist.add(edge);
        edgecnt++;
        edge.printContent();
      }
      for( int i = 0; i < copies.size(); i++ )
        copies.get(i).edges = edgelist;
      copies.clear();
      
    }
    System.out.println(emptycnt + " empty spots.");
    System.out.println("Created " + nodecnt + " nodes and " + edgecnt + " edges.");

    return retnodes;
  }

  //DFS traversal of the nodes in te graph
  public List<dEdge> dfs(Sudoku game, Queue<dNode> graph) {
    Deque<dNode> stack = new ArrayDeque<dNode>();
    List<dEdge> ret = new ArrayList<dEdge>();
    
    dNode first, current, neighbor;
    first = graph.poll();
    first.visited = true;
    game.setBoard( first.c.x, first.c.y, first.edges.get(0).data );
    stack.push(first);

    while( stack.size() > 0 ) {
      current = stack.pop();
      for( int i = 0; i < current.edges.size(); i++ ) {
        if( !current.edges.get(i).visited && isValid( current.c.x, current.c.y, current.edges.get(i).data ) ) {
          game.setBoard( current.c.x, current.c.y, current.edges.get(i).data );
          current.edges.get(i).visited = true;
          current.edges.get(i).printContent();
          ret.add(current.edges.get(i));
          break;
        }
      }

      if( game.isFull() ) {
        System.out.println("Found solution!");
        game.printBoard();
        break;
      }
      for( int j = 0; j < current.edges.size(); j++ ) {
        neighbor = current.edges.get(j).end;
        if( !neighbor.visited ) {
          neighbor.visited = true;
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
  
    Queue<dNode> graph;
    List<dEdge> solutionSet;

    boolean loadSuccess;

    loadSuccess = game.loadFromFile(file);
    //game.toString(); 
  
    graph = ss.buildGraph(game);
    solutionSet = ss.dfs(game, graph);
  }
}
