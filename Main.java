/*
*   Created By: Rohan Amjad
*   File: Main.java
*/

import java.util.Scanner;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        binarySearchTree wordTree = new binarySearchTree();
        String inputFileName;
        String word;

        System.out.print(">Enter the input file name: ");
        inputFileName = s.nextLine();

        try {
            File inputFile = new File(inputFileName);
            Scanner reader = new Scanner(inputFile);
            reader.useDelimiter("[^'a-zA-Z]");

            //fill BST with data
            while (reader.hasNext()) {
                word = reader.next();
                word = word.toLowerCase();
                if (!word.equals("")) {
                    wordTree.addNode(word);
                }
            }

            //print BST information
            System.out.println("\n>Total number of words in " + inputFileName + " = " + wordTree.getNumberOfNodes());
            System.out.println("\n>Number of unique words in " + inputFileName + " = " + wordTree.getNumberOfUniqueWords(wordTree.root));
            System.out.println("\n>The word(s) which occur(s) most often and the number of times that it/they occur(s) =");
            wordTree.calculateHighestFrequency(wordTree.root);
            wordTree.getMostOccuringWords(wordTree.root);

            String mainMenuInput;

            //Menu
            do{
                System.out.println("\n>To find a word enter 1");
                System.out.println(">To print the BST enter 2");
                System.out.println(">To exit the program enter 3");
                mainMenuInput = s.next();
                //main menu switch
                switch(mainMenuInput){
                    case "1":
                        String wordToFind;
                        do{
                            System.out.print("\n>Enter the word you are looking for in " + inputFileName + ". Or enter 0 to return to the main menu: ");
                            wordToFind = s.next();
                            if(!wordToFind.equals("0") && !wordToFind.equals("")){
                                wordTree.findWord(wordTree.root, wordToFind);
                            }
                        }
                        while(!wordToFind.equals("0"));
                        break;
                    case "2":
                        String menuInput;
                        //Printing sub menu
                        do {
                            System.out.print("\n>Enter the BST traversal method (1 = IN-ORDER, 2 = PRE-ORDER, 3 = POST-ORDER) for " + inputFileName + ". Or enter 0 to return to the main menu: ");
                            menuInput = s.next();
                            //printing BST switch
                            switch (menuInput){
                                case "1":
                                    System.out.print(">IN-ORDER output: ");
                                    wordTree.printInOrder(wordTree.root);
                                    System.out.println();
                                    break;
                                case "2":
                                    System.out.print(">PRE-ORDER output: ");
                                    wordTree.printPreOrder(wordTree.root);
                                    System.out.println();
                                    break;
                                case "3":
                                    System.out.print(">POST-ORDER output: ");
                                    wordTree.printPostOrder(wordTree.root);
                                    System.out.println();
                                    break;
                            }
                        }
                        while(!menuInput.equals("0"));
                        break;
                }
            }
            while(!mainMenuInput.equals("3"));
            
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!");
            e.printStackTrace();
        }

    }

}

class binarySearchTree {

    Node root;
    int numberOfNodes;
    int numberOfUniqueNodes;
    int highestFrequency;

    //constructor
    public binarySearchTree() {
        this.root = null;
        numberOfNodes = 0;
    }

    //add node
    public void addNode(String word) {
        if(root == null) {
            root = new Node(word);
            numberOfNodes++;
        }
        else {
            Node current = root;
            Node parent = null;

            while(true) {
                if(word.compareToIgnoreCase(current.word) > 0) {
                    parent = current;
                    current = current.rightChild;
                    if(current == null) {
                        parent.rightChild = new Node(word);
                        numberOfNodes++;
                        return;
                    }
                }
                else if(word.compareToIgnoreCase(current.word) < 0) {
                    parent = current;
                    current = current.leftChild;
                    if(current == null) {
                        parent.leftChild = new Node(word);
                        numberOfNodes++;
                        return;
                    }
                }
                else if(word.compareToIgnoreCase(current.word) == 0) {
                    current.frequency++;
                    return;
                }
            }
        }
    }

    //get number of nodes
    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    //find number of unique words
    private void calculateNumberOfUniqueWords(Node root) {
        if(root == null) {
            return;
        }
        if(root.frequency == 1) {
            numberOfUniqueNodes++;
        }
        calculateNumberOfUniqueWords(root.leftChild);
        calculateNumberOfUniqueWords(root.rightChild);
    }
    public int getNumberOfUniqueWords(Node root) {
        calculateNumberOfUniqueWords(root);
        return numberOfUniqueNodes;
    }

    //find highest frequency words
    public void calculateHighestFrequency(Node root) {
        if(root == null) {
            return;
        }
        if(root.frequency > highestFrequency) {
            highestFrequency = root.frequency;
        }
        calculateHighestFrequency(root.leftChild);
        calculateHighestFrequency(root.rightChild);
    }
    public void getMostOccuringWords(Node root) {
        if(root == null) {
            return;
        }
        if (root.frequency == highestFrequency) {
            System.out.println(root.word + " = " + root.frequency + " times");
        }
        getMostOccuringWords(root.leftChild);
        getMostOccuringWords(root.rightChild);
    }

    //find the node containing the word
    private Node findNode(Node root, String word) {
        if(root == null){
            return null;
        }
        if(word.compareToIgnoreCase(root.word) == 0) {
            return root;
        }
        else if(word.compareToIgnoreCase(root.word) < 0) {
            return findNode(root.leftChild, word);
        }
        else if(word.compareToIgnoreCase(root.word) > 0){
            return findNode(root.rightChild, word);
        }
        else{
            return null;
        }
    }
    public void findWord(Node root, String word) {
        Node temp = findNode(root, word);
        if (temp == null) {
            System.out.println(">Word Not Found!");
        }
        else{
            System.out.println(">Found! It appears " + temp.frequency + " times in the input text file");
        }
    }

    //Print InOrder, PreOrder, and PostOrder
    public void printInOrder(Node root) {
        if (root == null) {
            return;
        }
        printInOrder(root.leftChild);
        System.out.print(root.word + " ");
        printInOrder(root.rightChild);
    }
    public void printPreOrder(Node root) {
        if (root == null) {
            return;
        }
        System.out.print(root.word + " ");
        printPreOrder(root.leftChild);
        printPreOrder(root.rightChild);
    }
    public void printPostOrder(Node root) {
        if (root == null){
            return;
        }
        printPostOrder(root.leftChild);
        printPostOrder(root.rightChild);
        System.out.print(root.word + " ");
    }

}

class Node {
    int frequency;
    String word;

    Node leftChild;
    Node rightChild;

    //constructors
    Node(String word) {
        this.word = word;
        this.frequency = 1;
        leftChild = null;
        rightChild = null;
    }

    Node() {
        leftChild = null;
        rightChild = null;
    }
    // end of constructors
}

