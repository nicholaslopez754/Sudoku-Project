public class SudokuSolver extends Sudoku {

  //Constructor
  public SudokuSolver() {}

  //Sub-class of nodes that will represent possible decisions to make
  public class decisionNode {

    //The coordinates in the matrix of the decision to be made
    int x,y;

    //List of possible inputs
    List<Integer> inputs;

    public decisionNode(int i, int j, List<Integer> inList ) {
      x = i;
      y = j;
      inputs = inList;
    }
  }
  
  //Sub-class of edges between the nodes that represents the decision made
  public class decisionEdge {
    
    public class decisionEdge
  }

  //Main driver
  public static void main(String[] args) {
  Sudoku game = new Sudoku();
  File file = new File(args[0]);

  boolean loadSuccess;

  loadSuccess = game.loadFromFile(file);


  }
}
