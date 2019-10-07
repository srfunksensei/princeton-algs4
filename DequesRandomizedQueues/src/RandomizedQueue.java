/******************************************************************************
 * Compilation: javac RandomizedQueue.java  
 * Dependencies: java.util.Iterator.java java.util.NoSuchElementException.java 
 *              edu.princeton.cs.algs4.StdRandom.java
 *
 * RandomizedQueue.
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * 
 * A randomized queue is similar to a stack or queue, except that the item
 * removed is chosen uniformly at random from items in the data structure.
 * 
 * @author mb
 *
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0; // number of items in queue
    private Node<Item> tail, head; // first & last references
    private Node<Item> sample; // sample

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
    }

    /**
     * is the randomized queue empty?
     * 
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * return the number of items on the randomized queue
     * 
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the item
     * 
     * @param item item to be added
     * 
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        
        final Node<Item> f = tail;
        final Node<Item> newNode = new Node<>(f, item, null);
        tail = newNode;
        if (f == null)
            head = newNode;
        else
            f.next = newNode;
        size++;
    }

    /**
     * remove and return a random item
     * 
     * @return
     */
    public Item dequeue() {
        if (size == 0)
            throw new NoSuchElementException();

        if (sample == null) sample();
        
        final Item element = sample.item;
        final Node<Item> prev = sample.prev,
                         next = sample.next;
        
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            sample.prev = null;
        }
        
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            sample.next = null;
        }
        
        sample.item = null;
        sample = null;
        size--;
        
        return element;
    }
    
    /**
     * Returns the (non-null) Node at the specified element index.
     */
    private Node<Item> node(int index) {
        if (index < (size >> 1)) {
            Node<Item> x = head;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<Item> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    /**
     * return a random item (but do not remove it)
     * 
     * @return
     */
    public Item sample() {
        if (size == 0)
            throw new NoSuchElementException();

        final int randomIndex = StdRandom.uniform(size);
        sample = node(randomIndex);
        
        return sample.item;
    }

    /**
     * return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }
    
    /**
     * Simple representation of node
     * 
     * @author mb
     *
     */
    private static class Node<Item> {
        Item item; // actual data
        Node<Item> next; // next element
        Node<Item> prev; // prev element
        
        /**
         * constructs new node out of 
         * @param item data
         * @param next next element reference
         * @param prev previous element reference
         */
        public Node(final Node<Item> prev, final Item item, final Node<Item> next) {
            super();
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
    
    /**
     * Implementation of deque iterator
     * 
     * @author mb
     */
    private class RandomIterator implements Iterator<Item> {
        private int numVisitedItems = 0; // number of items visited
        private final int[] itemIndexes; // random indexes of items
        
        public RandomIterator() {
            itemIndexes = new int[size];
            
            for (int i = 0; i < size; i++) {
                itemIndexes[i] = i;
            }
            
            StdRandom.shuffle(itemIndexes);
        }
        
        /**
         * checks if there is next item to iterate over it
         */
        public boolean hasNext() {
            return numVisitedItems < size;
        }

        /**
         * @throws UnsupportedOperationException if the client calls method in the iterator
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Gets next element 
         * @throws NoSuchElementException if the client calls method in the iterator when there are no more items to return
         */
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            
            final Node<Item> elem = node(itemIndexes[numVisitedItems]);
            numVisitedItems++;
            return elem.item;
        }
    }
}
