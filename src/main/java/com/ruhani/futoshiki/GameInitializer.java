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
        this.gameBoard = new Node[boardSize][boardSize];
        for (int j = 0; j < boardSize; j++) {
            System.arraycopy(gameBoard[j], 0, this.gameBoard[j], 0, boardSize);

        }
        this.boardSize = boardSize;
        this.totalFilled = 0;
        this.valueOrdering = 0;
        this.variableOrdering = 0;
//        showAll(gameBoard);

        Node[][] nodes;// = new Node[boardSize][boardSize];
        nodes = gameBoard;
        nodes = check(nodes, boardSize);
        nodes = maintainLessThan(nodes, boardSize);
        nodes = maintainGreaterThan(nodes, boardSize);
//        showAll(this.gameBoard);
//        showAll(nodes);

        nodes = backtracing_search(nodes, boardSize);
        System.out.print(this.variableOrdering+" ");
        System.out.println(""+this.valueOrdering );
//        showAll();
    }

    public void showAll()
    {
        System.out.println("\n__________________________________________________________________________________________");
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("|");
//                System.out.print("pos = "+this.gameBoard[i][j].position +" \t");
//                System.out.println("dep = "+this.gameBoard[i][j].totalDependent);
//                System.out.println("val = "+this.gameBoard[i][j].actualValue);
                if(this.gameBoard[i][j].possibleValues.isEmpty())   System.out.print(this.gameBoard[i][j].actualValue+"");
                showVector(this.gameBoard[i][j].possibleValues);
//                System.out.print("\t\t");
//                showVector(this.gameBoard[i][j].greaterThanPosition);
//                showVector(this.gameBoard[i][j].lessThanPosition);
            }
            System.out.println("");
        }
        System.out.println("__________________________________________________________________________________________");
    }

    public void showAll(Node[][] nodes)
    {
        System.out.println("\n__________________________________________________________________________________________");
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("|");
//                System.out.print("pos = "+this.gameBoard[i][j].position +" \t");
//                System.out.println("dep = "+this.gameBoard[i][j].totalDependent);
//                System.out.println("val = "+this.gameBoard[i][j].actualValue);
                if(nodes[i][j].possibleValues.isEmpty())   System.out.print(nodes[i][j].actualValue+"");
                showVector(nodes[i][j].possibleValues);
//                System.out.print("\t\t");
//                showVector(this.gameBoard[i][j].greaterThanPosition);
//                showVector(this.gameBoard[i][j].lessThanPosition);
            }
            System.out.println("");
        }
        System.out.println("__________________________________________________________________________________________");
    }

    public void showOnlyPossibleValues(Node[][] nodes)
    {
        System.out.println("\n__________________________________________________________________________________________");
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("|");
//                System.out.print("pos = "+this.gameBoard[i][j].position +" \t");
//                System.out.println("dep = "+this.gameBoard[i][j].totalDependent);
//                System.out.println("val = "+this.gameBoard[i][j].actualValue);
//                if(this.gameBoard[i][j].possibleValues.isEmpty())   System.out.print(this.gameBoard[i][j].actualValue+"");
                showVector(nodes[i][j].possibleValues);
//                System.out.print("\t\t");
//                showVector(this.gameBoard[i][j].greaterThanPosition);
//                showVector(this.gameBoard[i][j].lessThanPosition);
            }
            System.out.println("");
        }
        System.out.println("__________________________________________________________________________________________");
    }

    public void showOnlyActualValues(Node[][] nodes)
    {
        System.out.println("\n__________________________________________________________________________________________");
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print("|");
//                System.out.print("pos = "+this.gameBoard[i][j].position +" \t");
//                System.out.println("dep = "+this.gameBoard[i][j].totalDependent);
//                System.out.println("val = "+this.gameBoard[i][j].actualValue);
                if(nodes[i][j].actualValue!=0)   System.out.print(nodes[i][j].actualValue+" ");
                else System.out.print("  ");
//                showVector(this.gameBoard[i][j].possibleValues);
//                System.out.print("\t\t");
//                showVector(this.gameBoard[i][j].greaterThanPosition);
//                showVector(this.gameBoard[i][j].lessThanPosition);
            }
            System.out.println("");
        }
        System.out.println("__________________________________________________________________________________________");
    }

    public void showVector(Vector<Integer>v){
//        System.out.print("hel = ");
        if(v.isEmpty()) System.out.print("        ");
        else
            for (int i = 0; i < this.boardSize - v.size(); i++) {
                System.out.print("  ");
            }

        for (Iterator iterator = v.iterator(); iterator.hasNext();) {
            int a = (int)iterator.next();
            System.out.print(a+"");
            if(iterator.hasNext()) System.out.print(" ");

        }
//        System.out.println("");
    }

    public Node[][] check(Node[][] nodes, int n)
    {

        Node[][] tempNode = new Node[n][n];
        tempNode = nodes;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                //main search
                int a = tempNode[i][j].actualValue;
//                System.out.println(a);
                if(a!=0){
                    tempNode[i][j].possibleValues.removeAllElements();
                    tempNode = updatePossibleValues(tempNode, n, i, j, a);

                }
                a=0;
            }
        }
        return tempNode;
    }



    public Node[][] updatePossibleValues(Node[][] nodes, int n, int row , int col, int val)
    {
        Node[][] tempNode = new Node[n][n];
//        tempNode = nodes;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempNode[i][j] = new Node();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempNode[i][j].actualValue = nodes[i][j].actualValue;
                tempNode[i][j].position = nodes[i][j].position;
                tempNode[i][j].totalDependent = nodes[i][j].totalDependent;
                for (Iterator iterator = nodes[i][j].possibleValues.iterator(); iterator.hasNext();) {
                    int next = (int) iterator.next();
                    tempNode[i][j].possibleValues.addElement(next);
                }
                for (Iterator iterator = nodes[i][j].greaterThanPosition.iterator(); iterator.hasNext();) {
                    int next = (int) iterator.next();
                    tempNode[i][j].greaterThanPosition.addElement(next);
                }
                for (Iterator iterator = nodes[i][j].lessThanPosition.iterator(); iterator.hasNext();) {
                    int next = (int) iterator.next();
                    tempNode[i][j].lessThanPosition.addElement(next);
                }
            }
//            System.arraycopy(nodes[i], 0, tempNode[i], 0, n);
        }
        tempNode[row][col].actualValue = val;
        //delete row element
        for (int k = 0; k < n; k++) {
            tempNode[row][k].possibleValues.removeElement(val);
        }
        //delete column element
        for (int k = 0; k < n; k++) {
            tempNode[k][col].possibleValues.removeElement(val);
        }
//                    tempNode = maintainGreaterThanSingleOperation(tempNode, n, row, col);
//                    tempNode = maintainLessThanSingleOperation(tempNode, n, row, col);

        tempNode = maintainGreaterThan(tempNode, n);
        tempNode = maintainLessThan(tempNode, n);
//                    showOnlyPossibleValues();
        return tempNode;

    }


    public boolean check_a_value(Node[][] nodeses, int n, int row, int col, int val)
    {
        for (int i = 0; i < n; i++) {
            if(col == i) continue;
            if(nodeses[row][i].actualValue == val) return false;
        }

        for (int i = 0; i < n; i++) {
            if(row == i) continue;
            if(nodeses[i][col].actualValue == val) return false;
        }

        return true;
    }

    public boolean checkresult(Node[][] nodeses, int n)
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!check_a_value(nodeses, n, i, j, nodeses[i][j].actualValue))
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
                if(nodeses[i][j].actualValue!=0)
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
        tempNode = nodes;

        //row check
        for (int i = 0; i < n; i++) {
            if(i!=col){
                if(tempNode[row][i].actualValue == val){
                    return false;
                }

                if (tempNode[row][i].actualValue==0) {
                    if (tempNode[row][i].possibleValues.size()==1 && tempNode[row][i].possibleValues.contains(val)) {
                        return false;
                    }
                }
            }
        }


        //column check
        for (int i = 0; i < n; i++) {
            if(i!=row){
                if(tempNode[i][col].actualValue == val){
                    return false;
                }

                if (tempNode[i][col].actualValue==0) {
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



//        System.out.println(this.totalFilled);
        if(completeAssignment(nodes, n)){
            System.out.println("Completed Assignment");
            showOnlyActualValues(nodes);
//            return nodes;
        }

        int selectedVariable;

        //using minimum ramianing value and degree heuristic
        selectedVariable = mrv(nodes, n);
//        showOnlyActualValues(nodes);
//        showOnlyPossibleValues(nodes);


        //selection of the first available variable
//        selectedVariable = select1stValue(nodes, n);
//        System.out.print(selectedVariable+" ");


        //random
//        selectedVariable = randomVariable(nodes,n);

        this.variableOrdering++;

        if(selectedVariable==-1) return nodes;

        int minRow = (selectedVariable-1)/n;
        int minCol = (selectedVariable-1)%n;



//        System.out.println(selectedVariable+" = "+minRow+" , "+minCol+" ");

        int size = nodes[minRow][minCol].possibleValues.size();
//        System.out.println(selectedVariable);
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i]=0;
        }
//        Vector v = new Vector();
        for (int i = 0; i < size; i++) {
//            v.addElement(nodes[minRow][minCol].possibleValues.get(i));
        }

        //value ordering : selection of the first available value
        for (int i = 0; i < size; i++) {
            array[i] = nodes[minRow][minCol].possibleValues.get(i);
        }

        //value ordering : least constraing value





        //value ordering : random
/*
        Random random = new Random(size);
        Vector v = new Vector();
        for (int i = 0; i < size; i++) {
            v.addElement(nodes[minRow][minCol].possibleValues.get(i));
        }
//        System.out.print("Size = "+size+" ran = ");
        for (int i = 0; i< size;i++) {
            int ran = random.nextInt(v.size());
//            System.out.print(v.get(ran));
                array[i] = (int) v.get(ran);
                v.remove(ran);
            }
//        System.out.println("");

*/




        for (int i = 0 ; i<size ; i++) {
            this.valueOrdering++;
//        for (int i = size-1 ; i>=0 ; i--) {
//            System.out.println("nodes["+minRow+"]["+minCol+"] = "+ nodes[minRow][minCol].actualValue);
            int tempValue = array[i];
//            int tempValue = (int)v.get(i);
//            v.removeElementAt(i);

//         System.out.println(tempValue);

            if (checkConsistency(nodes, n, minRow, minCol, tempValue)) {
//                System.out.println("true");
                int previous = nodes[minRow][minCol].actualValue;
//                nodes[minRow][minCol].actualValue = tempValue;



                Node[][] tempNodes = new Node[n][n];
//                tempNodes=nodes;
                for (int j = 0; j < n; j++) {
                    System.arraycopy(nodes[j], 0, tempNodes[j], 0, n);

                }
//                System.out.println("nodes["+minRow+"]["+minCol+"] = "+ nodes[minRow][minCol].actualValue);
                tempNodes = updatePossibleValues(tempNodes, n, minRow, minCol, tempValue);

//                showOnlyActualValues(tempNodes);
//                showOnlyPossibleValues(nodes);
//                showOnlyPossibleValues(tempNodes);


                tempNodes = backtracing_search(tempNodes, n);
//                v.addElement(tempValue);



//                System.out.println("Hello");;


                if (checkresult(tempNodes, n)) {
                    return tempNodes;
                }
//                return nodes;
                for (int j = 0; j < n; j++) {
//                    System.arraycopy(nodes[j], 0, tempNodes[j], 0, n);
                }

//                tempNodes = removePreviousSelection(nodes, n, minRow, minCol, tempValue, previous);

//                System.out.println("Backtracing");
//                showOnlyActualValues(tempNodes);
//                showOnlyPossibleValues(tempNodes);

//                return tempNodesBeforeUpdate;

            }

//            System.out.println("After If");
//            nodes = removePreviousSelection(nodes, n, minRow, minCol, tempValue);
//            showOnlyActualValues(nodes);
//            showOnlyPossibleValues(nodes);

//            showOnlyPossibleValues(nodes);
//            showOnlyActualValues(nodes);

//            return tempNodesBeforeUpdate;
        }

//                showOnlyPossibleValues(tempNodesBeforeUpdate);
        return nodes;
    }



    public boolean checkActual(Node[][] nodes, int n, int row,int col , int val)
    {
        for (int k = 0; k < n; k++) {
//                        if(nodes[row][k].actualValue==val)
//                            return false;
        }

        //delete column element
        for (int k = 0; k < n; k++) {
//                        if(nodes[k][col].actualValue==val)
//                            return false;
        }
        return true;

    }


    public int select1stValue(Node[][] nodes, int n)
    {
        Node[][] tempNode  =  new Node[n][n];
        tempNode = nodes;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int pos = tempNode[i][j].actualValue;
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
                if(tempNode[i][j].actualValue==0){
                    v.addElement(tempNode[i][j].position);
                }
            }
        }

        if(v.isEmpty()) return -1;

        pos = random.nextInt(v.size());


        return v.get(pos);

    }


    public Node[][] reverseUpdate(Node[][] nodes, int n)
    {
        Node[][] tempNode  =  new Node[n][n];
        tempNode = nodes;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int max = maxim(tempNode[i][j].possibleValues);
                int min = minim(tempNode[i][j].possibleValues);
                for (Iterator iterator = tempNode[i][j].lessThanPosition.iterator(); iterator.hasNext();) {
                    int next = (int)iterator.next();
                    next--;
                    for (int k = min+1; k <= n; k++) {
                        if(checkActual(nodes, n, next/n, next%n, k) && !tempNode[next/n][next%n].possibleValues.contains(k))
                            tempNode[next/n][next%n].possibleValues.addElement(k);
                    }
                    for (int k = min; k > 0; k--) {
//                            tempNode[next/n][next%n].possibleValues.removeElement(k);
                    }

                }

                for (Iterator iterator = tempNode[i][j].greaterThanPosition.iterator(); iterator.hasNext();) {
                    int next = (int)iterator.next();
                    next--;
                    for (int k = max-1; k > 0; k--) {
                        if(checkActual(nodes, n, next/n, next%n, k) && !tempNode[next/n][next%n].possibleValues.contains(k))
                            tempNode[next/n][next%n].possibleValues.addElement(k);
                    }
                    for (int k = max; k <= n; k++) {
//                            tempNode[next/n][next%n].possibleValues.removeElement(k);
                    }

                }


            }

        }

        return tempNode;

    }

    public Node[][] removePreviousSelection(Node[][] nodes, int n, int row, int col, int val, int pre)
    {
        Node[][] tempNode = new Node[n][n];
        tempNode=nodes;
        for (int i = 0; i < n; i++) {
//            System.arraycopy(nodes[i], 0, tempNode[i], 0, n);
        }

        //row check
        tempNode[row][col].actualValue = pre;

        for (int i = 0; i < n; i++) {
            if(!tempNode[row][i].possibleValues.contains(val))
                tempNode[row][i].possibleValues.addElement(val);
        }


        //column check
        for (int i = 0; i < n; i++) {
            if(!tempNode[i][col].possibleValues.contains(val))
                tempNode[i][col].possibleValues.addElement(val);
        }

        tempNode = reverseUpdate(tempNode, n);




        return tempNode;
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
                if(nodes[i][j].actualValue != 0)
                    continue;
                int size = nodes[i][j].possibleValues.size();
                if(size==0 && nodes[i][j].actualValue == 0)
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
                if(nodes[i][j].possibleValues.size()== minremain && nodes[i][j].actualValue==0 )
                {
                    minimunRemainingVector.addElement(nodes[i][j].position);
                }
            }
        }

/*
        System.out.print(minremain + " = ");
        showVector(minimunRemainingVector);
        System.out.println("");
        showOnlyPossibleValues(nodes);
*/
        return mrValue;
    }




    public Node[][] maintainLessThan(Node[][] nodes, int n){
        Node[][] tempNode = new Node[n][n];
//        tempNode = nodes;
        for (int k = 0; k < n; k++) {
            System.arraycopy(nodes[k], 0, tempNode[k], 0, n);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempNode = maintainLessThanSingleOperation(nodes, n, i, j);
            }
        }
        return tempNode;

    }

    public Node[][] maintainLessThanSingleOperation(Node[][] nodes, int n, int i, int j)
    {
        Node[][] tempNode = new Node[n][n];
//        tempNode = nodes;
        for (int k = 0; k < n; k++) {
            System.arraycopy(nodes[k], 0, tempNode[k], 0, n);
        }
//        showAll(tempNode);
        if(tempNode[i][j].totalDependent!=0)
        {
            for (Iterator it = tempNode[i][j].lessThanPosition.iterator(); it.hasNext();) {
                int next = (int)it.next();
//                        System.out.println("Less Than"+i+"."+j+" = " +next);
                int row , col;
                row = (next-1)/n;
                col = (next-1)%n;


                if (tempNode[i][j].actualValue !=0) {
                    for (int k = tempNode[i][j].actualValue; k > 0; k--) {
                        tempNode[row][col].possibleValues.removeElement(k);
                    }


                }



                int max=0;
                if(tempNode[row][col].actualValue!=0)
                    max = tempNode[row][col].actualValue;
                else
                {
                    if(!tempNode[row][col].possibleValues.isEmpty())
                    {
//                                Collections.sort(tempNode[row][col].possibleValues);
//                                max = tempNode[row][col].possibleValues.lastElement();
                        max = maxim(tempNode[row][col].possibleValues);
                        //       showVector(nodes[row][col].possibleValues);
                    }
                }
//                        System.out.println(max);
                for (int k = max; k <= n; k++) {
                    tempNode[i][j].possibleValues.removeElement(k);
                }


            }


        }
        return tempNode;
    }


    public Node[][] maintainGreaterThan(Node[][] nodes, int n)
    {
        Node[][] tempNode = new Node[n][n];
//        tempNode = nodes;
        for (int i = 0; i < n; i++) {
            System.arraycopy(nodes[i], 0, tempNode[i], 0, n);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempNode = maintainGreaterThanSingleOperation(nodes, n, i, j);
            }
        }
        return tempNode;

    }
    public Node[][] maintainGreaterThanSingleOperation(Node[][] nodes, int n,int i,int j)
    {
        Node[][] tempNode = new Node[n][n];
//        tempNode = nodes;
        for (int k = 0; k < n; k++) {
            System.arraycopy(nodes[k], 0, tempNode[k], 0, n);
        }

        if(tempNode[i][j].totalDependent!=0)
        {
//                    showVector(nodes[i][j].greaterThanPosition);
            for (Iterator it = tempNode[i][j].greaterThanPosition.iterator(); it.hasNext();) {
                int next = (int)it.next();
//                        System.out.println("\nInitialinsing   "+i+"."+j+" = " +next);
                int row , col;
                row = (next-1)/n;
                col = (next-1)%n;

                if (tempNode[i][j].actualValue !=0) {
                    for (int k = tempNode[i][j].actualValue; k <= n; k++) {
                        tempNode[row][col].possibleValues.removeElement(k);
                    }
                }


                int max=0;

                if(tempNode[row][col].actualValue!=0)
                    max = tempNode[row][col].actualValue;
                else
                {
                    if(!tempNode[row][col].possibleValues.isEmpty())
                    {
//                                Collections.sort(tempNode[row][col].possibleValues);
//                                max = tempNode[row][col].possibleValues.firstElement();
                        max = minim(tempNode[row][col].possibleValues);
//                                showVector(tempNode[row][col].possibleValues);
                    }
                }
//                        System.out.println(max);
                for (int k = max; k > 0; k--) {
                    tempNode[i][j].possibleValues.removeElement(k);
                }


            }


        }

        return tempNode;
    }

}
