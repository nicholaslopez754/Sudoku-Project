import java.util.*;
import java.io.*;

public class SudokuSolver extends Sudoku {

  public static Sudoku copy;

  //Constructor
  public SudokuSolver(Sudoku game) { 
    copy = game.clone();
  }

  //Sub-class of nodes that will represent possible decisions to make
  public class dNode {

    //The coordinates in the matrix of the decision to be made
    Coordinate c;
    int index;  
    //The move to make at the coordinate
    int data;

    //Flag useful for traversals
    boolean visited;
  
    //List of neighbors
    List<dNode> neighbors;

    dNode prev; 

    //Constructor
    public dNode( Coordinate coord, int datain ) {
      c = coord;
      data = datain;
      neighbors = new ArrayList<dNode>();
      visited = false;
    }

    //Clone
    public dNode clone() {
      Coordinate tempc = new Coordinate(this.c.x, this.c.y);
      int datain = this.data;
      dNode node = new dNode( tempc, datain );
      //System.out.print("Copied: ");
      //node.printContent();
      return node;
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
    
    //Helper method that iteratively adds the path to the graph
    public void addPath( Sudoku game ) {
      dNode curr = this;
      while( curr.data != 0 && curr.isValidNode(game) ) {
        curr.addNode(game);
        curr.printContent();
        curr = curr.prev;
      }
      System.out.println("finished path add");
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
  public Map<Integer, List<dNode>> buildGraph( Sudoku game ) {
    Map<Integer, List<dNode>> decisionMap = new HashMap<Integer, List<dNode>>(); 
    //List<Coordinate> cq = new ArrayList<Coordinate>();
    List<dNode> ret = new ArrayList<dNode>();
    int index = 0;
    int nodeCount = 0;

    //Sentinel/dummy node to act as a root
    Coordinate sentc = new Coordinate(-1,-1);
    dNode sentinel = new dNode( sentc, 0 );
    sentinel.index = index;
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
            node.index = index;
            if( node.isValidNode(game) ) {
              keyList.add(node);
              nodeCount++;
              //node.printContent();
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
      //System.out.println("init nlist size: " + nlist.size()); 
      for( dNode node : decisionMap.get(index) ) {
        //node.neighbors = nlist; //Shallow copy
        //System.out.print("Curr: ");
        //node.printContent();
        //System.out.println("Going to add " + nlist.size() + " neighbors");
        for( dNode listnode : nlist ) {
          dNode temp = listnode.clone();
          //temp.printContent();
          node.neighbors.add(temp); //deep copy
        }
      }
      index++;
    } 
    /* for( int i = 0; i < decisionMap.size(); i ++ ) {
      System.out.println("Row in map: " + i );
      for( dNode node : decisionMap.get(i) ) 
        node.printContent();
    } */
    //Dumps the content of the map into a list to be traversed later
    for( int i = 0; i < decisionMap.size(); i++ ) {
       for( dNode node : decisionMap.get(i) )
         ret.add(node);
    }
   
    return decisionMap;
  }


  public void dfs( Sudoku game, Map<Integer, List<dNode>> graph ) {
    Deque<dNode> stack = new ArrayDeque<dNode>();
    System.out.print("Finding solution... "); 
    //Adds sentinel to the stack
    dNode first, current, neighbor;
    List<dNode> templist;
    int index = 0;
    first = graph.get(index).get(0);
    //first.visited = true;
    stack.push(first);

    while( stack.size() > 0 ) {
      /*System.out.print("Stack size: " + stack.size() + "\n" );
      for( dNode node : stack )
        node.printContent(); */
      current = stack.pop();
      //System.out.println("popped: ");
      //current.printContent();
      //System.out.println("v: " + current.visited);
      index++;

      //game.clearBoard();
      //current.addPath(game);

      if( current.isValidNode( game ) ) {
        //game.clearBoard();
        current.addNode(game);
        game.printBoard();
      }

      if( game.isFull() ) {
        System.out.println("Done!");
        game.printBoard();
        return;
      }
      if( !current.visited ) {
        current.visited = true;
        templist = graph.get(current.index+1);
        if( templist == null ) {
          System.out.println("null");
          continue;
        }
        /*System.out.println(templist.size());
        for( dNode node : templist )
          node.printContent(); */
        for( int i = 0; i < templist.size(); i++ ) {
          neighbor = templist.get(i);
          //neighbor.printContent();
          ///System.out.println(neighbor.visited);
          //System.out.println("nsize: " + neighbor.neighbors.size());
          neighbor.prev = current;
          stack.push(neighbor);
        
        }
      }
      
       
    }
    System.out.println("Failed.");

  }

  //Back tracking method
  //  Find row, col of an unassigned cell
  /*If there is none, return true
  For digits from 1 to 9
    a) If there is no conflict for digit at row,col
        assign digit to row,col and recursively try fill in rest of grid
    b) If recursion successful, return true
    c) Else, remove digit and try another
  If all digits have been tried and nothing worked, return false*/
  public boolean backTrack() {
    int r = -1;
    int c = -1;
    for( int i = 0; i < numRows; i++ ) {
      for( int j = 0; j < numCols; j++ ) {
        if( copy.fixed[i][j] == 0 ) {
          r = i;
          c = j;
        }
      }
    }
    if( r == -1 && c == -1 )
      return true;
    for( int i = 1; i < numRows+1; i++ ) {
      if( copy.isValid(r, c, i ) ) {
        copy.setBoard(r, c, i);

        if( backTrack() )
          return true;

        copy.clearCell(r, c);
      }
    }
    return false;
  }

  //Main driver
  public static void main(String[] args) {
    Sudoku game = new Sudoku();
    File file = new File(args[0]);
    //SudokuSolver ss = new SudokuSolver();
    boolean loadSuccess;

    loadSuccess = game.loadFromFile(file);
    //game.toString(); 
    
    SudokuSolver ss = new SudokuSolver(game);
    copy.toString();
    boolean solved;
    solved = ss.backTrack();

    System.out.println(solved);

  }
}
