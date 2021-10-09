package com.paqhoy.algoritmoAlgorutas.model;


public class SPedido implements Comparable<SPedido>{
    public Integer id;
    public Integer tiempoMinutosEntrega;
    public Integer tiempoMinutosLimite;
    public Integer cantidad;

    @Override
    public int compareTo(SPedido p) {
        if(this.tiempoMinutosEntrega.equals(p.tiempoMinutosEntrega)){
            return p.cantidad.compareTo(this.cantidad);
        }
        else if( this.tiempoMinutosEntrega > p.tiempoMinutosEntrega ) return 1;
        else return -1;
    }
}
