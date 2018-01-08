package com.ruhani.futoshiki;

import java.util.Iterator;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruhani
 */
public class Node {
    int position;
    Vector<Integer>possibleValues;
    int value;
    int totalDependent;
    Vector<Integer>greaterThanPosition;
    Vector<Integer>lessThanPosition;

    Node() {
        this.position = 0;
        this.possibleValues = new Vector<Integer>();
        this.value = 0;
        this.totalDependent = 0;
        this.greaterThanPosition = new Vector<Integer>();
        this.lessThanPosition = new Vector<Integer>();        
    }

    Node(int position, Vector<Integer> possibleValues, int actualValue, int totalDependent, Vector<Integer> greaterThanPosition, Vector<Integer> lessThanPosition) {
        this.position = position;
        this.possibleValues = possibleValues;
        this.value = actualValue;
        this.totalDependent = totalDependent;
        this.greaterThanPosition = greaterThanPosition;
        this.lessThanPosition = lessThanPosition;
    }
    
    Node(Node node)
    {
        this.position = node.position;
        this.possibleValues = new Vector<Integer>();
        for (Iterator<Integer> iterator = node.possibleValues.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.possibleValues.addElement(next);
        }
        this.value = node.value;
        this.totalDependent = node.totalDependent;
        this.greaterThanPosition = new Vector<Integer>();
        for (Iterator<Integer> iterator = node.greaterThanPosition.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.greaterThanPosition.addElement(next);
        }
        this.lessThanPosition = new Vector<Integer>();        
        for (Iterator<Integer> iterator = node.lessThanPosition.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.lessThanPosition.addElement(next);
        }
        
    }
    
    public static Node[][] node2dArrayCopy(Node[][] node, int size)
    {
        Node[][] temp = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp[i][j] = new Node(node[i][j]);
            }
        }
        return temp;
    }

    
    
    
    
}
