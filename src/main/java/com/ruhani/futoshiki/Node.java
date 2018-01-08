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
    int actualValue;
    int totalDependent;
    Vector<Integer>greaterThanPosition;
    Vector<Integer>lessThanPosition;

    Node() {
        this.position = 0;
        this.possibleValues = new Vector<Integer>();
        this.actualValue = 0;
        this.totalDependent = 0;
        this.greaterThanPosition = new Vector<Integer>();
        this.lessThanPosition = new Vector<Integer>();
    }

    public Node(int position, Vector<Integer> possibleValues, int actualValue, int totalDependent, Vector<Integer> greaterThanPosition, Vector<Integer> lessThanPosition) {
        this.position = position;
        this.possibleValues = possibleValues;
        this.actualValue = actualValue;
        this.totalDependent = totalDependent;
        this.greaterThanPosition = greaterThanPosition;
        this.lessThanPosition = lessThanPosition;
    }
}
