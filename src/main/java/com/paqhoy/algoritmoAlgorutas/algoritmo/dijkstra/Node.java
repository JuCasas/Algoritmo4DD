package com.paqhoy.algoritmoAlgorutas.algoritmo.dijkstra;

//En el caso de java usamos una clase que representara el pair de C++
public class Node implements Comparable<Node>{
    int first, second;

    //constructor
    public Node( int d , int p ){
        this.first = d;
        this.second = p;
    }

    //es necesario definir un comparador para el correcto funcionamiento del PriorityQueue
    public int compareTo( Node other){
        if( second > other.second ) return 1;
        if( second == other.second ) return 0;
        return -1;
    }
};
