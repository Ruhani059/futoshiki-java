package com.ruhani.futoshiki;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruhani
 */
public class Futoshiki{

    int boardSize;
    int gameBoard[][];
    int constraintNumber;
    Vector<Relation>constraintRelations;
    int[] smallerConstraint;
    int[] largerConstraint;


    Futoshiki(int boardSize, int[][] gameBoard, int constraintNumber, int[] constraintBoard, int[] larger) {
        this.boardSize = boardSize;
        this.gameBoard = gameBoard;
        this.constraintNumber = constraintNumber;
        this.smallerConstraint = constraintBoard;
        this.largerConstraint = larger;
    }

    Futoshiki() {

    }

    Futoshiki(int boardSize, int[][] gameBoard) {
        this.boardSize = boardSize;
        this.gameBoard = gameBoard;
    }

    public void showGameBoard(){
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                System.out.print(gameBoard[i][j]+" ");
            }
            System.out.println("");
        }
        for (int i = 0; i < this.boardSize*this.boardSize; i++) {
            if(smallerConstraint[i]!=0){
                int x1 = (smallerConstraint[i]-1)/boardSize;
                int y1 = (smallerConstraint[i]-1)%boardSize;
                int x2 = (i-1)/boardSize;
                int y2 = (i-1)%boardSize;

                System.out.println("("+x1+","+y1+")>("+x2+","+y2+")");
            }
            if(largerConstraint[i]!=0){
                int x1 = (largerConstraint[i]-1)/boardSize;
                int y1 = (largerConstraint[i]-1)%boardSize;
                int x2 = (i-1)/boardSize;
                int y2 = (i-1)%boardSize;
//                System.out.println("("+x1+","+y1+")<("+x2+","+y2+")");
            }
        }
    }

    public static void main(String[] args) {
        String str;
        String inputFileName;
        for(int i=1;i<9;i++)
        {
            inputFileName = "/futoshikiInputFile/input" +String.valueOf(i)+".txt";
            str = "input0"+Integer.toString(i)+".txt";
            System.out.println("Processing Input File : "+str);
            new Futoshiki().Solver(inputFileName);
        }
        for(int i=11;i<13;i++)
        {
            inputFileName = "/futoshikiInputFile/input" +String.valueOf(i)+".txt";
            str = "input"+Integer.toString(i)+".txt";
            System.out.println("Processing Input File : "+str);
            new Futoshiki().Solver(inputFileName);
        }
        for(int i=21;i<23;i++)
        {
            inputFileName = "/futoshikiInputFile/input" +String.valueOf(i)+".txt";
            str = "input"+Integer.toString(i)+".txt";
            System.out.println("Processing Input File : "+str);
            new Futoshiki().Solver(inputFileName);
        }
    }

    public void Solver(String inputFileName) {

//            Scanner in1 = new Scanner(System.in);
//            String str = in1.nextLine();
//            str = "input"+str+".txt";
            InputStream is = this.getClass().getResourceAsStream(inputFileName);
            Scanner in = new Scanner(is);

//            Scanner in = new Scanner(new File("input01.txt"));
//            Scanner in = new Scanner(new File("input02.txt"));
//            Scanner in = new Scanner(new File("input03.txt"));
//            Scanner in = new Scanner(new File("input04.txt"));
//            Scanner in = new Scanner(new File("input11.txt"));
            int n = in.nextInt();
            int board [][] = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    board[i][j] = in.nextInt();
                }
            }

            int constraintNumber = in.nextInt();
            int constraintBoard[] = new int[n*n];
            int constraintBoard2[] = new int[n*n];
            for (int i = 0; i < n*n; i++) {
                constraintBoard[i] = 0;
                constraintBoard2[i] = 0;
            }

            Vector<Relation>constraints = new Vector<Relation>();
            int[] dependant = new int[n*n+1];
            Vector less = new Vector<Integer>();
            Vector more = new Vector<Integer>();
            Vector possible = new Vector<Integer>();
            for (int i = 0; i <= n*n; i++) {
                dependant[i] = 0;
                less.add(new Vector<Integer>());
                more.add(new Vector<Integer>());
                possible.add(new Vector<Integer>());
            }
            for (int j = 0; j <= n*n; j++) {
                for (int i = 0; i < n; i++) {

                    ((Vector)possible.get(j)).add(i+1);
                }

            }
            for (int i = 0; i < constraintNumber; i++) {
                int pos1 = in.nextInt();
                int pos2 = in.nextInt();
                dependant[pos1]++;
                dependant[pos2]++;
                ((Vector)less.get(pos2)).add(pos1);
                ((Vector)more.get(pos1)).add(pos2);
            }
            Node[][] gameNode = new Node[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int pos = i*n+j+1;
                    gameNode[i][j] = new Node(pos, (Vector) possible.get(pos), board[i][j], dependant[pos], (Vector) more.get(pos), (Vector) less.get(pos));
                }
            }
            ValueSelectionClass game = new ValueSelectionClass(n, board);
//            board[1][0] = 3;
//            board = game.recursion(board, n);
            GameInitializer gameInitializer = new GameInitializer(gameNode, n);


/*
            for (int i = 0; i < constraintNumber; i++) {
                int x1 = in.nextInt();
                int y1 = in.nextInt();
                int x2 = in.nextInt();
                int y2 = in.nextInt();
                SinglePosition morePosition = new SinglePosition(x1, y1);
                SinglePosition lessPosition = new SinglePosition(x2, y2);
                Relation relation = new Relation(morePosition,lessPosition);
                constraints.add(relation);
            }

            for (Iterator it = constraints.iterator(); it.hasNext();) {
                Vector<Relation>v = new Vector<Relation>();
                Relation relation = (Relation)it.next();
                System.out.println("("+relation.largePosition.x+","+relation.largePosition.y+")>("
                +relation.smallPosition.x+","+relation.smallPosition.y+")");
            }
*/
            Futoshiki futoshiki = new Futoshiki(n, board, constraintNumber, constraintBoard,constraintBoard2);
//            futoshiki.showGameBoard();

    }

}
class SinglePosition{
    int x,y;
    SinglePosition(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

class Relation{
    SinglePosition smallPosition;
    SinglePosition largePosition;

    public Relation(SinglePosition largePosition, SinglePosition smallPosition) {
        this.smallPosition = smallPosition;
        this.largePosition = largePosition;
    }
}
