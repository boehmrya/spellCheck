/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpellCheck;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/******************************************************************************
 *  A symbol table implemented with a separate chaining hash table.
 ******************************************************************************/

public class WordList<Key> {

    private static int n;       // number of key-value pairs
    private static int m;       // hash table size
    private static double loadLimit = 0.75; // the upper limit ratio of elements to buckets (before resizing) 
    private static SingleLList[] st;   // array of doubly-linked-list symbol tables

    
    // create separate chaining hash table
    public WordList() {
        this(997);
    } 

    
    // create separate chaining hash table with m lists
    public WordList(int m) {
        this.m = m;
        st = new SingleLList[m];
    } 

    
    /*
    * calculate and return the hash value between 0 and m-1
    */
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    } 

    
    /*
    * return number of key-value pairs in symbol table
    */
    public int numberWords() {
        return n;
    }
    
    
    /*
    * resize the array of linked lists to double the previous size
    */
    public void resize() {
        int oldNumBuckets = m; // hold number to iterate through current list
        m = (m * 2); // double the number of buckets
        SingleLList[] stTemp = new SingleLList[m]; // new resized array to copy elements into
        
        for (int i = 0; i < oldNumBuckets; i++) {
            
            // check if the slot has a linked list before getting keys and rehashing
            try {
                
                // Put all of the keys for a linked list in the array
                ArrayList keys = st[i].getKeys();
            
                // for each key in the linked list, rehash and add key
                // to the new array.
                for (int j = 0; j < keys.size(); j++) {
                    Key key = (Key) keys.get(j);
                    int k = hash(key);
                    try {
                         stTemp[k].addToStart(key);
                    }
                    catch (NullPointerException e) {
                        stTemp[k] = new SingleLList();
                        stTemp[k].addToStart(key);
                    } 
                }
            }
            // if the bucket has no linked list, proceed to next bucket
            catch (NullPointerException e) {
                continue;
            }
            
        }
        
        // point st to the new (resized) array
        st = stTemp;
    }
    

    /*
    * is the symbol table empty?  Returns true if yes, false if no.
    */
    public boolean isEmpty() {
        return numberWords() == 0;
    }

    
    /*
    * return true if the key exists, otherwise false
    */
    public boolean search(Key key) {
        boolean keyExists;
        int i = hash(key);
        try {
            keyExists = st[i].contains(key);
        }
        catch (NullPointerException e) {
            keyExists = false;
        }
        return keyExists;
    }

    
    // insert key into the table
    public void insert(Key key) {
        int i = hash(key);
        try {
            st[i].addToStart(key);
            n++;
        }
        catch (NullPointerException e) {
            if (st[i] == null) {
                st[i] = new SingleLList();
            }
            st[i].addToStart(key);
            n++;
        } 
        // resize the array if load factor is over the limit.
        if (this.isOverload()) {
           this.resize();
        }
    }
    
    
    // checks if the hashTable's load factor is over the limit.
    public boolean isOverload() {
        double loadFactor = n / m;
        return (loadFactor > loadLimit);
    }
    

    // delete key (and associated value) from the symbol table.
    public void delete(Key key) {
        throw new UnsupportedOperationException("delete not currently supported");
    }
    
    
    /**
     * creates and returns a file.
     * @param fileName a string representing the name of the file
     * @return the created file
     */
    public static File createFile(String fileName) {
        File newFile = null;
  
        // tries to create new file in the system
        try {
            // create new file
            newFile = new File("../" + fileName);

            if (newFile.exists()) {
                newFile.delete();
            }

            // check whether file creation worked
            newFile.createNewFile();

        }
        catch(Exception e) {
            System.out.println("Couldn't create the new file");
        }
        
        return newFile; 
    }
    
    
    /**
     * takes an array list of strings as input, and writes each string to a file.
     * @param words an array list of words.
     * @param outFileName a string representing the name of the file to write to.
     */
    public static void fileWrite( ArrayList<String> words, String outFileName) {
        
        try {
           BufferedWriter writer = new BufferedWriter(new FileWriter("../" + outFileName));
           
            // Write each word in the array onto a separate line.
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        }
        catch(IOException e) { 
            System.out.println("File Not Found");
        }
        
    }
    
    
    public static ArrayList sortList() {
        
        // array list to hold and sort all words before printing
        ArrayList words  = new ArrayList();
        
        // add all words into the arraylist
        for (int i = 0; i < m; i++) {
            
            try {
                // Put all of the keys for a linked list in the array
                ArrayList keys = st[i].getKeys();

                // for reach key in the linked list, rehash and add key
                // to the new array.
                for (int j = 0; j < keys.size(); j++) {
                    if (keys.get(j) != null) {
                       words.add(keys.get(j)); 
                    }
                } 
            }
            catch (NullPointerException e) {
                continue;
            }
              
        }
        
        // sort the array list
        Collections.sort(words);
        
        return words;
        
    }
    
    
    // prints all words in alphabetical order.
    public void sortedPrint() {
        
        // get a sorted list of the words in the hash table
        ArrayList words = sortList();
        
        // print each word in the array list
        for (int k = 0; k < words.size(); k++) {
            System.out.println(words.get(k));
        }
        
    }
    
    
    // prints all words in alphabetical order and writes to a file.
    public void sortedPrint( String fileName ) {
        // get out file object and out file name.
        File outFile = createFile(fileName);
        String outFileName = outFile.getName();
        
        // get a sorted list of the words in the hash table
        ArrayList words = sortList();
        
        // print each word in the array list
        for (int k = 0; k < words.size(); k++) {
            System.out.println((k + 1) + ": " + words.get(k));
        }
        
        // write all of the sorted words into the file created above.
        fileWrite(words, outFileName);
        
    }
        
        
}
