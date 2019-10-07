/******************************************************************************
 * Compilation: javac Solver.java 
 * Execution: java Solver 
 * Dependencies: edu.princeton.cs.algs4.MinPQ, 
 *              edu.princeton.cs.algs4.Stack
 * 
 * 
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    
    private final Stack<Board> boards = new Stack<>();
    
    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException(); 

        final MinPQ<SearchNode> pq = initPq(initial); 
        final MinPQ<SearchNode> pqTwin = initPq(initial.twin());
        
        while (!pq.isEmpty()) {
            final SearchNode node = pq.delMin();
            final SearchNode twinNode = pqTwin.delMin();
            
            if (node.board.isGoal()) {
                SearchNode goal = node;
                
                boards.push(goal.board);
                while (goal.predecessor != null) {
                    goal = goal.predecessor;
                    boards.push(goal.board);
                }
                
                break;
            }
            
            if (twinNode.board.isGoal()) {
                break;
            }
            
            for (final Board neighbor : node.board.neighbors()) {
                if (node.predecessor != null && neighbor.equals(node.predecessor.board)) {
                    continue;
                }
                
                pq.insert(new SearchNode(neighbor, node.move + 1, node));
            }
            
            for (final Board neighbor : twinNode.board.neighbors()) {
                if (twinNode.predecessor != null && neighbor.equals(twinNode.predecessor.board)) {
                    continue;
                }
                
                pqTwin.insert(new SearchNode(neighbor, node.move + 1, twinNode));
            }
        }
    }
    
    private MinPQ<SearchNode> initPq(final Board board) {
        if (board == null) throw new NullPointerException();
        
        final MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(board, 0, null));
        
        return pq;
    }

    /**
     * is the initial board solvable?
     * 
     * @return
     */
    public boolean isSolvable() {
        return !this.boards.isEmpty();
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * 
     * @return
     */
    public int moves() {
        return this.boards.size() - 1;
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * 
     * @return
     */
    public Iterable<Board> solution() {
        return isSolvable() ? this.boards : null;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        
        private final Board board;
        private final int move;
        private final int manhattan;
        private final SearchNode predecessor;
        
        private final int priority;
        
        public SearchNode(final Board board, final int move, final SearchNode predecessor) {
            this.board = board;
            this.move = move;
            this.predecessor = predecessor;
            
            this.manhattan = board.manhattan();
            this.priority = this.manhattan + move;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (that.priority > this.priority) return -1;
            if (that.priority < this.priority) return 1;
            
            if (that.manhattan > this.manhattan) return 1;
            if (that.manhattan < this.manhattan) return -1;
            return 0;
        }
    }
    
    public static void main(String[] args) {

        // create initial board from file
        In in = new In("puzzle04.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
