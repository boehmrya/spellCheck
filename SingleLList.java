/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpellCheck;

import java.util.ArrayList;

/**
 *
 * @author Ryan Boehm
 */
public class SingleLList<T> {
    
    private class Node {
        private T key;
        private Node next;

        public Node(T key, Node next)  {
            this.key  = key;
            this.next = next;
        }
    } //End of Node inner class


    private Node head;

    public SingleLList( ) {
        head = null;
    }

    /**
    Adds a node at the start of the list with the specified data.
    The added node will be the first node in the list.
    */
    public void addToStart(T key) {
        head = new Node(key, head);
    }

    /**
    Removes the head node and returns true if the list contains at
    least one node. Returns false if the list is empty.
    */
    public boolean deleteHeadNode( ) {
        if (head != null) {
            head = head.next;
            return true;
        }
        else
            return false;
    }

    /**
     * Returns the number of nodes in the list.
    */
    public int size() {
        int count = 0;
        Node position = head;
        while (position != null) {
            count++;
            position = position.next;
        }
        return count;
    }

    
    /**
     * Finds the first node containing the target key, and returns the
     * corresponding value. If target is not in the list, null is returned.
    */
    public boolean contains(T key) {
        for (Node x = head; x != null; x = x.next) {
            if (key.equals(x.key)) 
		return true;
        }
        return false;
    }
    
    
    /**
     * Prints out the keys and values for every item in the linked list.
     */
    public void outputList( ) {
        Node position = head;
        while (position != null) {
            System.out.println( "Key: " + position.key );
            position = position.next;
        }
        System.out.println(); // line break
    }
    
    
    /**
     * Returns an array of all keys in the linked list.
     */
    public ArrayList getKeys( ) {
        ArrayList keys = new ArrayList();
        Node position = head;
        while (position != null) {
            keys.add(position.key);
            position = position.next;
        }
        return keys;
    }

    public boolean isEmpty( ) {
        return (head == null);
    }

    public void clear( ) {
        head = null;
    }
    
     
}
