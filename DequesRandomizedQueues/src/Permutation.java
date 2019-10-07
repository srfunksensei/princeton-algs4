/******************************************************************************
 * Compilation: javac Permutation.java  
 * Dependencies: edu.princeton.cs.algs4.StdIn.java
 *
 * Permutation.
 *
 ******************************************************************************/

import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 * Client program Permutation.java that takes an integer k as a command-line
 * argument; reads in a sequence of strings from standard input using
 * StdIn.readString(); and prints exactly k of them, uniformly at random. Print
 * each item from the sequence at most once.
 * 
 * @author mb
 *
 */
public class Permutation {

    public static void main(String[] args) {
        final RandomizedQueue<String> rQueue;
        
        int k = Integer.parseInt(args[0]);
        if (k > 0) {
            rQueue = new RandomizedQueue<>();
            while (!StdIn.isEmpty()) {
                final String item = StdIn.readString();
                rQueue.enqueue(item);
            }
            
            Iterator<String> it = rQueue.iterator();
            while (k > 0) {
                StdOut.printf("%s\n", it.next());
                k--;
            }
        }
        
        
        
    }

}
