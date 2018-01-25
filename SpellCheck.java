

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


/**
 * @author Ryan Boehm
 */
public class SpellCheck {
    
    /**
     * asks the user for the name of a file
     * @return: a string representing the filename
     */
    public static String getFileName() {
        Scanner userInput = new Scanner(System.in); // to get the name of the file.
        String fileName;
        
        // get the name of the file from the user.
        System.out.println("Enter the name of the file to spellcheck: ");
        fileName = userInput.nextLine();
        
        return fileName;
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
            newFile = new File(fileName);

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
     * takes a file name as a string as input, and reads the file's contents.
     * @param fileName: a string representing the name of the file to read from.
     * @return: a string with all of the files content.
     */
    public static String fileRead( String fileName ) {
        String line;  // each line read from the file.
        StringBuilder fileContents = new StringBuilder(); // to handle line by line append operations
        String finalFileContents = ""; // to store final result
        
        try {
            // read the file
            BufferedReader br = new BufferedReader(new FileReader(fileName)); 
            while ((line = br.readLine())!= null) { 
               line += " ";
               fileContents.append(line); //append all file contents into one string
            } 
            
            // Convert all of the file's contents back into one big string
            finalFileContents = fileContents.toString();
            
            // close the file
            br.close(); 
        }
        catch(IOException e) { 
            System.out.println("File Not Found");
        }
        
        return finalFileContents;  
    }
    
    
    /**
     * takes a string as input, and writes it to a file.
     * @param words: an array list of words
     * @param outFileName: a string representing the name of the file to write to.
     */
    public static void fileWrite( ArrayList<String> words, String outFileName) {
        
        try {
           BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));
           
            // Write each word in the array onto a separate line.
            for (String word : words) {
                writer.write(word);
            }
            writer.flush();
            writer.close();
        }
        catch(IOException e) { 
            System.out.println("File Not Found");
        }
        
    }
    
    
    /**
     * converts a string of words into an array of words
     * @param fileContents: the string of words.
     * @return an array of strings representing each word.
     */
    public static ArrayList createWordList(String fileContents) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // to validate words
        ArrayList wordList = new ArrayList(); 
        
        // hold the current word being processed
        StringBuilder word = new StringBuilder();
        String finalWord;

        // Verify if any of the letters are not in the alphabet
        for (int i = 0; i < fileContents.length(); i++) {
            char c = fileContents.charAt(i);
            finalWord = word.toString();
            
            // add word to master list and reset if current char is not a letter
            // otherwise keep building the current word
            if ((alphabet.indexOf(c)) < 0) {
                if (word.length() > 0) {
                    wordList.add(finalWord);    
                }
                wordList.add(String.valueOf(c));
                word.setLength(0); // reset the word to empty
            }
            else {
                word.append(c); // add letter to current word and proceed to check the next letter.
            }
        }  
        return wordList;
    }
    
    
    /*
    * takes a string representing a word as input, returns word surrounded by equals signs
    * @param word: a string representing a word
    * @return a string that includes the input word surrounded by two equals signs.
    */
    public static String markIncorrect( String word ) {
        String markedWord = "=" + word + "=";
        return markedWord;
    }
    
    
    /*
    * helper function to determine if a word only contains letters (used in check spelling method) 
    * @param word: a string representing a word
    * @return true of the word only contains letters.
    */
    public static boolean isAlpha( String word ) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if ((alphabet.indexOf(c)) < 0) {
                return false;
            }
        }
        return true; 
    }
    
    
    /*
    * helper function to determine if a word only contains letters (used in check spelling method) 
    * @param input: a string of characters
    * @return true the string only contains spaces, otherwise false
    */
    public static boolean isSpace( String input ) {
        String space = " ";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ((space.indexOf(c)) < 0) {
                return false;
            }
        }
        return true; 
    }
    
    
    /*
    * Takes an array list of all words, checks to see which are in the dictionary
    * Marks those words that are not in the dictionary as incorrect.
    * Reinserts all words into a new array list.
    * @param wordList: an array list of words in input file
    * @param wordHashTable: a WordList object of all words in the dictionary
    * @return an array list of marked words
    */
    public static ArrayList checkSpellingMark(ArrayList wordList, WordList wordHashTable) {
        ArrayList correctedWordList = new ArrayList();
        String word;
        String prevWord;
        String nextWord;
        
        // for each word, add it directly to the new markedWords array if it is
        // in the dictionary hashtable.  Otherwise, mark it incorrect first by
        // surrounding it with equals signs, and then add it.
        for (int i = 0; i < wordList.size(); i++) {
            word = (String) wordList.get(i);
            if (wordHashTable.search(word.toLowerCase())) {
                correctedWordList.add(word);
            }
            else {
                //correctedWordList.add(markIncorrect(word));
                // only apply to actual words, not punctuation or spaces
                if (word.length() > 1) {
                    correctedWordList.add(markIncorrect(word));
                }
                // Handling single characters.
                else {
                   // check for single letter char at beggining
                   if (i == 0) {
                       nextWord = (String) wordList.get(i + 1);
                       if (isSpace(nextWord)) {
                            correctedWordList.add(markIncorrect(word));
                        }  
                   }
                   // check for single letter char at end
                   else if (i == (wordList.size() - 1)) {
                       prevWord = (String) wordList.get(i - 1);
                       if (isSpace(prevWord)) {
                            correctedWordList.add(markIncorrect(word));
                       } 
                   }
                   // check for single letter char in middle of essay
                   else {
                       prevWord = (String) wordList.get(i - 1);
                       nextWord = (String) wordList.get(i + 1);
                       if (isSpace(prevWord) && isSpace(nextWord)) {
                            correctedWordList.add(markIncorrect(word));
                       } 
                       // if it's punctuation not surrounded by spaces (don't mark incorrect)
                       else if (!isAlpha(word)) {
                           correctedWordList.add(word);
                       }
                       // if it's a single letter surrounded by a non-letter on either side (i.e. contraction)
                       // don't mark this incorrect
                       else if (isAlpha(word) && !isAlpha(prevWord) && !isAlpha(nextWord)) {
                           correctedWordList.add(word);
                       }
                   }     
                }
            }
        }      
        return correctedWordList;
    }
    
   
    /*
    * Takes an array list of all words, checks to see which are in the dictionary
    * Presents options to the user to correct misspelled words.
    * Reinserts all corrected words into a new array list.
    * @param wordList: an array list of words in input file
    * @param wordHashTable: a WordList object of all words in the dictionary
    * @return an array list of marked words
    */
    public static ArrayList checkSpellingFix(ArrayList wordList, WordList wordHashTable) {
        ArrayList correctedWordList = new ArrayList(); // to hold all marked and unmarked words
        String word;
        String prevWord;
        String nextWord;
        
        // for each word, add it directly to the new markedWords array if it is
        // in the dictionary hashtable.  Otherwise, mark it incorrect first by
        // surrounding it with equals signs, and then add it.
        for (int i = 0; i < wordList.size(); i++) {
            word = (String) wordList.get(i);
           
            // if word is in dictionary, add it to revised list right away (it's correctly spelled).
            if (wordHashTable.search(word.toLowerCase())) {
                correctedWordList.add(word);
            }
            else {
                // only apply to actual words, not punctuation or spaces
                if (word.length() > 1) {
                    word = fixSpelling(word, wordHashTable);
                    correctedWordList.add(word);
                }
                // Handling single characters.
                else {
                   // check for single letter char at beggining
                   if (i == 0) {
                       nextWord = (String) wordList.get(i + 1);
                       if (isSpace(nextWord)) {
                            word = fixSpelling(word, wordHashTable); 
                            correctedWordList.add(word);
                        }  
                   }
                   // check for single letter char at end
                   else if (i == (wordList.size() - 1)) {
                       prevWord = (String) wordList.get(i - 1);
                       if (isSpace(prevWord)) {
                            word = fixSpelling(word, wordHashTable); 
                            correctedWordList.add(word);
                       } 
                   }
                   // check for single letter char in middle of essay
                   else {
                       prevWord = (String) wordList.get(i - 1);
                       nextWord = (String) wordList.get(i + 1);
                       if (isSpace(prevWord) && isSpace(nextWord)) {
                            word = fixSpelling(word, wordHashTable); 
                            correctedWordList.add(word);
                       } 
                       // if it's punctuation not surrounded by spaces
                       else if (!isAlpha(word)) {
                           correctedWordList.add(word);
                       }
                       // if it's a single letter surrounded by a non-letter on either side (i.e. contraction)
                       else if (isAlpha(word) && !isAlpha(prevWord) && !isAlpha(nextWord)) {
                           correctedWordList.add(word);
                       }
                   }      
                }
            }
        }
                
        return correctedWordList;
    }
    
    
    /*
    * Creates a list of replacement words from the dictionary hash table for a particular
    * word.  Then, gets user feedback to determine how to fix the misspelled word (no action is possible).
    * @param word: a string representing the misspelled word.
    * @param wordHashTable: a wordList object representing the dictionary.
    */
    public static String fixSpelling( String word, WordList wordHashTable ) {
        // get the replacement words
        ArrayList replacementWords = replacementWord(word, wordHashTable);
        
        System.out.println(); // line break after selection for better user interface
     
        // get user feedback to determine how to address the issue
        String newWord = getFeedback(word, replacementWords);
        
        return newWord;
    }
    
    
    public static String getFeedback( String word, ArrayList<String> replacementWords ) {
        Scanner userInput = new Scanner(System.in); // to get the name of the file.
        String option; // the user's selection
        String optionsMenu = ""; // list of options to show the user
        String newWord; // to hold the adjusted word
        int wordCount = 0; // count the potential replacement words
        int addOptionsCount = 0; // number the  additional options (no change, choose own word)
        
        // text to print for each option
        while ( wordCount < replacementWords.size()) {
            optionsMenu += ("[" + wordCount + "] " + replacementWords.get(wordCount) + " ");
            wordCount++;
        }
        
        addOptionsCount = wordCount;
        // add option to change nothing or enter own choice
        optionsMenu += ("[" + addOptionsCount + "] " + "No Change ");
        addOptionsCount++;
        optionsMenu += ("[" + addOptionsCount + "] " + "Enter Your Own Choice ");
        
        // get the option selection from the user.
        System.out.println("How should we fix the misspelled word: " + word + " ?");
        System.out.println(optionsMenu);
        option = userInput.nextLine();  
        
        
        try {
            // store the number for re-use below.  Catch number format exception and re-call
            // function if invalid input.
            int optionNum = Integer.valueOf(option);

            // Options for all suggested replacement words
            for (int i = 0; i < wordCount; i++) {
                if (optionNum == i) {
                    newWord = replacementWords.get(i);
                    System.out.println("Thank you.  The original word will be replaced with: " + newWord);
                    System.out.println(); // line break before final message
                    return newWord;
                }
            }
        
            // No Change option
            if (optionNum == (addOptionsCount - 1)) {
                newWord = word;
                System.out.println("Thank you.  The original word will not be replaced.");            
            }
            // Allow user to enter a custom replacement word
            else if (optionNum == addOptionsCount) {
                newWord = ownWordChoice();
                System.out.println("Thank you.  The original word will be replaced with: " + newWord);
            }
            // if something else is entered, retain the existing word 
            // and leave a message to the user saying so.
            else {
                newWord = word;
                System.out.println("Not a valid option.  The original word will not be replaced.");
            }
            System.out.println(); // line break before final message
    
        }
        // if they enter invalid input, leave the word unchanged
        catch (NumberFormatException e) {
            System.out.println("Your selection was invalid.  The original word will remain in place.");
            newWord = word;
        }
        
        return newWord; 
    }
    
    
    /*
    * Helper method to get a custom write-in word from the user.
    * @return word: the word entered by the user.
    */
    public static String ownWordChoice() {
        Scanner userInput = new Scanner(System.in); // to get the name of the file.
        System.out.println("Enter your own word: ");
        String word = userInput.nextLine();
        return word;
    }
    
    
    /*
    * creates a list of viable replacement words from the dictionary for a given misspelled word.
    * @param word: the misspelled word to generate replacement words for.
    * @param wordHashTable: the hash table holding the dictionary of words to check for replacmements in.
    * @return an array list of potential replacement words.
    */
    public static ArrayList replacementWord(String word, WordList wordHashTable) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz"; // to swap out characters with to find replacements
        ArrayList replaceWords = new ArrayList(); // to hold all potential replacements
        int wordCount = 0;
        
        for (int i = 0; i < word.length(); i++) {
            
            // reset the word to begin replacing each letter at the next index.
            StringBuilder newWord = new StringBuilder(word);
            
            for (int j = 0; j < alphabet.length(); j++) {
                char c = alphabet.charAt(j);
                newWord.setCharAt(i, c);
                String finalWord = newWord.toString();
                
                if (wordHashTable.search(finalWord.toLowerCase())) {
                    replaceWords.add(finalWord);
                    wordCount++;
                    
                    // if we have 5 replacement words already, return the array
                    if (wordCount > 4) {
                        return replaceWords;
                    }  
                }    
            }
            
        }
        
        return replaceWords; 
    }
    
    
    /*
    * Create a hashtable from the words in the file myDict.dat
    * @param fileName: a string representing the name of the file
    * @return wordlist object with all of the words from the dictionary
    */
    public static WordList buildWordHashTable( String fileName ) {
        // start with an array of size 1000.  Will double when necessary.
        WordList wordHashTable = new WordList(1000);
        
        // each line repreasents a word
        String line; 
        
        try {
            // read the file
            BufferedReader br = new BufferedReader(new FileReader(fileName)); 
            
            // insert each word into the hash table
            while ((line = br.readLine()) != null) { 
                wordHashTable.insert(line);
            } 
            
            // close the file
            br.close(); 
        }
        catch(IOException e) { 
            System.out.println("File Not Found");
        }
        
        return wordHashTable;
    }
    
  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // build the dictionary hash table from the dictionary file
        WordList wordHashTable = buildWordHashTable("myDict.dat");
     
        // get the file from the user, check the spelling of each word
        String fileName = getFileName();
        String fileContents = fileRead(fileName);
        
        // store words from input file and mark incorrect ones
        ArrayList words = createWordList(fileContents); // list of words in the essay
        ArrayList markedWords = checkSpellingMark(words, wordHashTable); // words marked incorrect
        
        // get out file object and out file name
        File outFile = createFile("myEssay.out");
        String outFileName = outFile.getName();
        
        // write to the myEssay.out file to show the incorrect words
        fileWrite(markedWords, outFileName);
        
        // write to the myEssay.out with the corrected words.
        ArrayList fixedWords = checkSpellingFix(words, wordHashTable); // fixed words
        fileWrite(fixedWords, outFileName);
        
        // signal to the user that the process is finished
        System.out.println("Spell-checking process complete!");
        
    }
    
}
