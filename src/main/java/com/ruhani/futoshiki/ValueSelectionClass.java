package com.ruhani.futoshiki;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruhani
 */
public class ValueSelectionClass {
    
    int size;
    int[][] board;

    public ValueSelectionClass(int size, int[][] board) {
        this.size = size;
//        this.board = board;
        this.board = new int [this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, this.size);
        }

    }
    
    public int[][] recursion (int[][] array, int n)
    {
        if(arrayCount(array)) return array;
        
        int selected = firstValueSelect(array);
        for (int i = 1; i <= n; i++) {
            if(consistencyCheck(array, (selected/n), (selected%n),i))
            {
                int previous = array[selected/n][selected%n];
                array[selected/n][selected%n]=i;
                array = recursion(array, n);
                if (result(array)) {
                    return array;
                }
                array[selected/n][selected%n]=previous;
            }
            
        }
        return array;
        
    }
    
    public void showArray(int[][] a)
    {        
        System.out.println("_________________");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(a[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("_________________");
    }
    
    public boolean result (int[][] a)
    {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(!consistencyCheck(a, i, j,a[i][j])) return false;
            }
        }
        return true;
        
    }
    
    public boolean consistencyCheck(int[][] a,int row, int col, int val)
    {        
            for (int i = 0; i < this.size ; i++) {
                if (i==col) continue;
                if (a[row][i]==val) return false;
            }        
            for (int i = 0; i < this.size ; i++) {
                if (i==row) continue;
                if (a[i][col]==val) return false;
            }        
        return true;
    }
    public int firstValueSelect(int [][] a){
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(a[i][j]==0)  return (i*this.size+j);
            }
        }        
        return -1;
    }

    public boolean arrayCount(int[][] a) {
        int count=0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(a[i][j]!=0)  count++;
            }
        }
        if(count==this.size*this.size) return true;
        else return false;
        
    }
    
    

    
}
