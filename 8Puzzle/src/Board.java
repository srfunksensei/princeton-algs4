
/******************************************************************************
 * Compilation: javac Board.java 
 * Dependencies: java.util.ArrayList, java.util.Arrays, java.util.List,
 *              edu.princeton.cs.algs4.StdRandom
 * 
 *
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private final int[][] blocks;

    private int hamming;
    private int manhattan;

    private int emptyBlockRow;
    private int emptyBlockCol;

    private Board twin;

    /**
     * construct a board from an n-by-n array of blocks (where blocks[i][j] = block
     * in row i, column j)
     * 
     * @param blocks
     */
    public Board(int[][] blocks) {
        int val = 1;

        this.blocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            this.blocks[i] = Arrays.copyOf(blocks[i], blocks[i].length);

            for (int j = 0; j < blocks[i].length; j++) {
                if (this.blocks[i][j] != val) {
                    if (!(i == blocks.length - 1 && j == blocks[i].length - 1)) {
                        this.hamming++;
                    }

                    if (this.blocks[i][j] != 0) {
                        int row = (this.blocks[i][j] - 1) / blocks.length;
                        int col = (this.blocks[i][j] - 1) % blocks.length;
                        this.manhattan += (Math.abs(i - row) + Math.abs(j - col));
                    }
                }

                val++;

                if (this.blocks[i][j] == 0) {
                    emptyBlockRow = i;
                    emptyBlockCol = j;
                }
            }
        }
    }

    /**
     * board dimension n
     * 
     * @return
     */
    public int dimension() {
        return blocks.length;
    }

    /**
     * number of blocks out of place
     * 
     * @return
     */
    public int hamming() {
        return this.hamming;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     * 
     * @return
     */
    public int manhattan() {
        return this.manhattan;
    }

    /**
     * is this board the goal board?
     * 
     * @return
     */
    public boolean isGoal() {
        return this.hamming == 0 && this.manhattan == 0;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     * 
     * @return
     */
    public Board twin() {
        if (twin == null) {
            final int length = this.blocks.length;

            int row1 = StdRandom.uniform(length), 
                col1 = StdRandom.uniform(length);
            while (row1 == this.emptyBlockRow && col1 == this.emptyBlockCol) {
                row1 = StdRandom.uniform(length);
                col1 = StdRandom.uniform(length);
            }

            int row2 = StdRandom.uniform(length), 
                col2 = StdRandom.uniform(length);
            while ((row2 == this.emptyBlockRow && col2 == this.emptyBlockCol) || 
                    (row2 == row1 && col2 == col1)) {
                row2 = StdRandom.uniform(length);
                col2 = StdRandom.uniform(length);
            }

            final int[][] twinBlocks = copy();
            final int tmp = twinBlocks[row1][col1];
            twinBlocks[row1][col1] = twinBlocks[row2][col2];
            twinBlocks[row2][col2] = tmp;

            twin = new Board(twinBlocks);
        }
        
        return twin;
    }

    /**
     * does this board equal y?
     */
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (!(y.getClass() == this.getClass()))
            return false;

        Board board = (Board) y;
        return Arrays.deepEquals(this.blocks, board.blocks);
    }

    /**
     * all neighboring boards
     * 
     * @return
     */
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();

        final int prevRow = emptyBlockRow - 1, prevCol = emptyBlockCol - 1, nextRow = emptyBlockRow + 1,
                nextCol = emptyBlockCol + 1;

        if (prevRow >= 0) {
            final Board neighbor = neighborBoard(prevRow, emptyBlockCol);
            neighbors.add(neighbor);
        }
        if (nextRow < blocks.length) {
            final Board neighbor = neighborBoard(nextRow, emptyBlockCol);
            neighbors.add(neighbor);
        }
        if (prevCol >= 0) {
            final Board neighbor = neighborBoard(emptyBlockRow, prevCol);
            neighbors.add(neighbor);
        }
        if (nextCol < blocks.length) {
            final Board neighbor = neighborBoard(emptyBlockRow, nextCol);
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    private Board neighborBoard(final int row, final int col) {
        final int[][] copyBlocks = copy();
        final int tmp = copyBlocks[emptyBlockRow][emptyBlockCol];
        copyBlocks[emptyBlockRow][emptyBlockCol] = copyBlocks[row][col];
        copyBlocks[row][col] = tmp;

        return new Board(copyBlocks);
    }

    /**
     * Returns a copy of blocks
     * 
     * @return
     */
    private int[][] copy() {
        final int[][] copy = new int[this.blocks.length][this.blocks.length];
        for (int i = 0; i < this.blocks.length; i++) {
            copy[i] = Arrays.copyOf(this.blocks[i], this.blocks[i].length);
        }
        return copy;
    }

    /**
     * string representation of this board
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(blocks.length).append("\n");

        final int totalNumOfDigits = String.valueOf(blocks.length * blocks.length).length();
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                final int val = blocks[i][j];
                final int valDigits = String.valueOf(val).length();

                for (int k = 0; k < totalNumOfDigits - valDigits; k++) {
                    sb.append(" ");
                }

                sb.append(val).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
