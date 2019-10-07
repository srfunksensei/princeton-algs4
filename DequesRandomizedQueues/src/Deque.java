/******************************************************************************
 * Compilation: javac Deque.java  
 * Dependencies: java.util.Iterator.java java.util.NoSuchElementException.java 
 *
 * Deque.
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double-ended queue or deque (pronounced “deck”) is a generalization of a
 * stack and a queue that supports adding and removing items from either the
 * front or the back of the data structure.
 * 
 * @author mb
 *
 */
public class Deque<Item> implements Iterable<Item> {
    
    private int size = 0; // number of items in deque
    private Node<Item> first, last; // first & last references 

    /**
     * construct an empty deque
     */
    public Deque() {
    }

    /**
     * is the deque empty?
     * 
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * return the number of items on the deque
     * 
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front
     * 
     * @param item item to be added
     * @throws IllegalArgumentException if the client calls method with a null argument
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        
        final Node<Item> f = first;
        final Node<Item> newNode = new Node<>(null, item, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    /**
     * add the item to the end
     * 
     * @param item item to be added
     * @throws IllegalArgumentException if the client calls method with a null argument
     */
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        
        final Node<Item> l = last;
        final Node<Item> newNode = new Node<>(l, item, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    /**
     * remove and return the item from the front
     * 
     * @throws NoSuchElementException if the client calls method when the deque is empty
     * @return
     */
    public Item removeFirst() {
        if (first == null)
            throw new NoSuchElementException();
        
        final Item element = first.item;
        final Node<Item> next = first.next;
        first.item = null;
        first.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        
        return element;
    }

    /**
     * remove and return the item from the end
     * 
     * @throws NoSuchElementException if the client calls method when the deque is empty
     * @return
     */
    public Item removeLast() {
        if (last == null)
            throw new NoSuchElementException();
        
        final Item element = last.item;
        final Node<Item> prev = last.prev;
        last.item = null;
        last.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        
        return element;
    }

    /**
     * return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator() {
        return new ArrayIterator();
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
    private class ArrayIterator implements Iterator<Item> {
        private Node<Item> lastReturned; // last item returned
        private Node<Item> next; // next item
        private int nextIndex; // next index of the item
        
        public ArrayIterator() {
            next = first;
        }
        
        /**
         * checks if there is next item to iterate over it
         */
        public boolean hasNext() {
            return nextIndex < size;
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
            
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }
    }
}
