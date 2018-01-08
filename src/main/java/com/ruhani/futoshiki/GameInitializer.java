package com.ruhani.futoshiki;

import com.sun.org.apache.xpath.internal.axes.NodeSequence;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruhani
 */
public class GameInitializer {
    Node[][] gameBoard;
    int boardSize;
    int totalFilled;
    int valueOrdering;
    int variableOrdering;

    public GameInitializer(Node[][] gameBoard, int boardSize) {
        this.boardSize = boardSize;
        this.gameBoard = new Node[boardSize][boardSize];
        this.gameBoard = Node.node2dArrayCopy(gameBoard, boardSize);        
        this.totalFilled = 0;
        this.valueOrdering = 0;
        this.variableOrdering = 0;
    } 
    
    public void gameInitialize(GameInitializer gameInitializer)
    {
        gameInitializer.gameBoard = initializeStates(gameInitializer.gameBoard, gameInitializer.boardSize);
//        showing initial possible array list
//        showPossibleValues(gameInitializer.gameBoard);
        gameInitializer.gameBoard = backtracing_search(gameInitializer.gameBoard, gameInitializer.boardSize);
//        Possible value array showing...
//        System.out.println("Possible Array List");
//        showPossibleValues(gameInitializer.gameBoard);
        System.out.println("Variable Ordering : "+this.variableOrdering);
        System.out.println("Value Ordering : "+this.valueOrdering );
    }
    
    public void showValues(Node[][] board)
    {
        for (int i = 0; i < this.boardSize; i++) {
            System.out.print("____");
        }
        System.out.println("_");
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("| ");
                if(board[i][j].value!=0)
                    System.out.print(board[i][j].value+" ");
                else System.out.print("  ");
            }
            System.out.println("|");
            if(i==this.boardSize-1)  continue;
            System.out.print(" ");
            for (int k = 0; k < this.boardSize; k++) {
                System.out.print("___ ");
            }
            System.out.println("");
        }
        for (int i = 0; i < this.boardSize; i++) {
            System.out.print("____");
        }
        System.out.println("_");
        
    }

    public void showPossibleValues(Node[][] board)
    {
        for (int i = 0; i < this.boardSize; i++) {
            System.out.print("__");
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("_");
            }
            System.out.print("_");
        }
        System.out.println("_");
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("| ");
                if(board[i][j].possibleValues.size()==0)
                {
                    for (int k = 0; k <this.boardSize ; k++) {
                        System.out.print(" ");
                    }
                }
                else
                {
                    for (Iterator<Integer> iterator = board[i][j].possibleValues.iterator(); iterator.hasNext();) {
                        Integer next = iterator.next();
                        System.out.print(next+"");
                    }
                    for (int k = 0; k < this.boardSize - board[i][j].possibleValues.size(); k++) {
                        System.out.print(" ");
                    }

                }
                System.out.print(" ");
            }
            System.out.println("|");
            if(i==this.boardSize-1)  continue;
            System.out.print(" ");
            for (int l = 0; l < this.boardSize; l++) {
                System.out.print("_");
                for (int m = 0; m < this.boardSize; m++) {
                    System.out.print("_");
                }
                System.out.print("_ ");
            }
            System.out.println("");
        }
        for (int i = 0; i < this.boardSize; i++) {
            System.out.print("__");
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("_");
            }
            System.out.print("_");
        }
        System.out.println("_");
        
    }
    
    public Node[][] initializeStates(Node[][] nodes, int n)
    {        
//        System.out.println("Initialize");
        Node[][] tempNode = new Node[n][n];
        tempNode = Node.node2dArrayCopy(nodes, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {                
                //main search
                if(tempNode[i][j].value!=0){
                    tempNode[i][j].possibleValues.removeAllElements();

                }
                tempNode = updatePossibleValues(tempNode, n, i, j, tempNode[i][j].value);                    
                
            }
        }
//        showOnlyPossibleValues(tempNode);
//        System.out.println("Initialize");
        return tempNode;
    }
        
    public Node[][] updatePossibleValues(Node[][] nodes, int n, int row , int col, int val)
    {
        Node[][] tempNode = new Node[n][n];        
        tempNode = Node.node2dArrayCopy(nodes, n);

        tempNode[row][col].value = val;
        //delete row element
        for (int k = 0; k < n; k++) {
            tempNode[row][k].possibleValues.removeElement(val);                        
        }
        //delete column element
        for (int k = 0; k < n; k++) {
            tempNode[k][col].possibleValues.removeElement(val);                        
        }        

        tempNode = maintainGreaterThan(tempNode, n);
        tempNode = maintainLessThan(tempNode, n);
//        showOnlyPossibleValues(tempNode);

        return tempNode;

    }
       
    public boolean check_a_value(Node[][] nodeses, int n, int row, int col, int val)
    {
        for (int i = 0; i < n; i++) {
            if(col == i) continue;
            if(nodeses[row][i].value == val) return false;
        }
        
        for (int i = 0; i < n; i++) {
            if(row == i) continue;
            if(nodeses[i][col].value == val) return false;
        }
        
        return true;        
    }
    
    public boolean checkresult(Node[][] nodeses, int n)
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!check_a_value(nodeses, n, i, j, nodeses[i][j].value))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean completeAssignment(Node[][] nodeses, int n)
    {
        int count=0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(nodeses[i][j].value!=0)
                {
                    count++;
                }
            }
        }
        this.totalFilled = count;
        if (count==n*n) return true;
        else return false;
    }
        
    public boolean checkConsistency(Node[][] nodes, int  n, int row , int col, int val)
    {
        Node[][] tempNode = new Node[n][n];
        tempNode = Node.node2dArrayCopy(nodes, n);
        
        //row check
        for (int i = 0; i < n; i++) {
            if(i!=col){
                if(tempNode[row][i].value == val){
                    return false;
                }

                if (tempNode[row][i].value==0) {
                    if (tempNode[row][i].possibleValues.size()==1 && tempNode[row][i].possibleValues.contains(val)) {
                        return false;
                    }
                }            
            }
        }
        

        //column check
        for (int i = 0; i < n; i++) {
            if(i!=row){
                if(tempNode[i][col].value == val){
                    return false;
                }

                if (tempNode[i][col].value==0) {
                    if (tempNode[i][col].possibleValues.size()==1 && tempNode[i][col].possibleValues.contains(val)) {
                        return false;
                    }
                }            
            }
        }
        
        return true;
        
    }
    
    public Node[][] backtracing_search(Node[][] nodes, int n)
    {
        if(completeAssignment(nodes, n)){ 
            System.out.print("Completed Assignment");
            showValues(nodes);
//            return nodes;
        }

        int selectedVariable;

        //using minimum ramianing value and degree heuristic
        selectedVariable = mrv(nodes, n);
        
        //selection of the first available variable
//        selectedVariable = select1stValue(nodes, n);
        
        //random
//        selectedVariable = randomVariable(nodes,n);
        
        this.variableOrdering++;
        
        if(selectedVariable==-1) return nodes;
        
        int minRow = (selectedVariable-1)/n;
        int minCol = (selectedVariable-1)%n;
        
//        System.out.println(selectedVariable+" = "+minRow+" , "+minCol+" ");
        
        int size = nodes[minRow][minCol].possibleValues.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i]=0;
        }

        //value ordering : selection of the first available value 
        for (int i = 0; i < size; i++) {
            array[i] = nodes[minRow][minCol].possibleValues.get(i);
        }
        
        //value ordering : random
/*
        Random random = new Random(size);
        Vector v = new Vector();
        for (int i = 0; i < size; i++) {
            v.addElement(nodes[minRow][minCol].possibleValues.get(i));
        }
        for (int i = 0; i< size;i++) {
            int ran = random.nextInt(v.size());
                array[i] = (int) v.get(ran);
                v.remove(ran);
        }

*/
        
        
        

        for (int i = 0 ; i<size ; i++) {            
            this.valueOrdering++;

            int tempValue = array[i];

            if (checkConsistency(nodes, n, minRow, minCol, tempValue)) {
                
                Node[][] tempNodes = new Node[n][n];
                tempNodes = Node.node2dArrayCopy(nodes, n);

//                System.out.println("nodes["+minRow+"]["+minCol+"] = "+ nodes[minRow][minCol].value);

                tempNodes = updatePossibleValues(tempNodes, n, minRow, minCol, tempValue);
//                showOnlyPossibleValues(tempNodes);
                
                tempNodes = backtracing_search(tempNodes, n);

                if (checkresult(tempNodes, n)) {
//                    System.out.println("Returned");
                    return tempNodes;
                }
              
            }
            
        }        
        return nodes;
    }
    
    public int select1stValue(Node[][] nodes, int n)
    {
        Node[][] tempNode  =  new Node[n][n];
        tempNode = nodes;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int pos = tempNode[i][j].value;
                if(pos==0){
                    return tempNode[i][j].position;
                }
                
            }
        }

        return -1;
        
    }

    public int randomVariable(Node[][] nodes,int n)
    {
        Node[][] tempNode  =  new Node[n][n];
        tempNode = nodes;
        Random random = new Random();
        int pos = -1;
        Vector<Integer> v = new Vector<Integer>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(tempNode[i][j].value==0){
                    v.addElement(tempNode[i][j].position);
                }                
            }
        }

        if(v.isEmpty()) return -1;

        pos = random.nextInt(v.size());
                    
        
        return v.get(pos);
        
    }
    
    public int maxim (Vector<Integer>v)
    {
        int max = 0;
        for (Iterator<Integer> iterator = v.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            if(max < next)  max  = next;
        }
        return max;
    }

    public int minim (Vector<Integer>v)
    {
        int min = 10;
        for (Iterator<Integer> iterator = v.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            if(min > next)  min  = next;
        }
        return min;
    }
    
    public int mrv(Node[][] nodes, int n)
    {
        Vector<Integer>minimunRemainingVector = new Vector<Integer>();
        int minremain = 10;
        int mrValue = -1;
//        showOnlyActualValues();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(nodes[i][j].value != 0)
                    continue;
                int size = nodes[i][j].possibleValues.size();
                if(size==0 && nodes[i][j].value == 0)
                {
                    return -1;
                }
                if(size>0)
                {
                    if(minremain > size) 
                    {
                        
                        minremain = size;
                        mrValue = i*n+j+1;
                    }
                }
                
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(nodes[i][j].possibleValues.size()== minremain && nodes[i][j].value==0 )
                {
                    minimunRemainingVector.addElement(nodes[i][j].position);
                }
            }
        }
        return mrValue;
    }
    
    public Node[][] maintainLessThan(Node[][] tempNode, int n){
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempNode = maintainLessThanSingleOperation(tempNode, n, i, j);
            }
        }
        return tempNode;
        
    }
    
    public Node[][] maintainLessThanSingleOperation(Node[][] tempNode, int n, int i, int j)
    {
        
                if(tempNode[i][j].totalDependent!=0)
                {
                    for (Iterator it = tempNode[i][j].lessThanPosition.iterator(); it.hasNext();) {
                        int next = (int)it.next();
                        next--;
                        int row , col;
                        row = next/n;
                        col = next%n; 
                            
                        //i,j er value less than row,col er value   [i][j]<[row][col]
                        //so i,j er actual value theke 1 porjonto sob delete kore dibo row,col er theke
                        if (tempNode[i][j].value !=0) {
                            for (int k = tempNode[i][j].value; k > 0; k--) {
                                tempNode[row][col].possibleValues.removeElement(k);
                            }
                        }
                                               
                        int max=0;
                        //jodi [row][col] e kono value thake
                        if(tempNode[row][col].value!=0)
                            max = tempNode[row][col].value;
                        //[row][col] e value na thakle maximum koto hote pare
                        else
                        {
                            if(!tempNode[row][col].possibleValues.isEmpty())
                            {
                                max = maxim(tempNode[row][col].possibleValues);
                            }                            
                        }

                        //[row][col] e possible maximum value theke n porjonto sob delete kore dibo [i][j] theke
                        for (int k = max; k <= n; k++) {
                            tempNode[i][j].possibleValues.removeElement(k);
                        }
                    }
                               
                }
                return tempNode;
    }
    
    public Node[][] maintainGreaterThan(Node[][] tempNode, int n)
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempNode = maintainGreaterThanSingleOperation(tempNode, n, i, j);
            }
        }
        return tempNode;
        
    }

    public Node[][] maintainGreaterThanSingleOperation(Node[][] tempNode, int n,int i,int j)
    {
        
                if(tempNode[i][j].totalDependent!=0)
                {
                    for (Iterator it = tempNode[i][j].greaterThanPosition.iterator(); it.hasNext();) {
                        int next = (int)it.next();
                        next--;
                        int row , col;
                        row = next/n;
                        col = next%n; 

                        //i,j er value greater than row,col er value   [i][j]>[row][col]
                        //so i,j er actual value the n porjonto sob delete kore dibo row,col er theke
                        if (tempNode[i][j].value !=0) {
                            for (int k = tempNode[i][j].value; k <= n; k++) {
                                tempNode[row][col].possibleValues.removeElement(k);
                            }
                        }

                        
                        int min=0;

                        //jodi [row][col] e kono value thake
                        if(tempNode[row][col].value!=0)
                            min = tempNode[row][col].value;
                        //[row][col] e value na thakle minimum koto hote pare
                        else
                        {
                            if(!tempNode[row][col].possibleValues.isEmpty())
                            {
                                min = minim(tempNode[row][col].possibleValues);
                            }                            
                        }

                        //[row][col] e possible minimum value theke 1 porjonto sob delete kore dibo [i][j] theke
                        for (int k = min; k > 0; k--) {
                            tempNode[i][j].possibleValues.removeElement(k);
                        }
                    }
                
                }
                
                return tempNode;
    }
    
    
}
