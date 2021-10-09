package com.paqhoy.algoritmoAlgorutas.model;

import java.util.PriorityQueue;

public class Cluster implements Comparable< Cluster >{
    public AVehiculo vehiculo;
    public int centroideX;
    public int centroideY;
    public int centroideZ;
    public APedido firstPedido = null;
    public int capacidad = 0;
    public PriorityQueue<APedido> pedidos;

    public void setClusterNo(APedido pedido){
        pedidos.add(pedido);
        this.capacidad += pedido.cantidad;
    }

    @Override
    public int compareTo(Cluster c) {
        if( this.firstPedido.minFaltantes == c.firstPedido.minFaltantes ){
            if( this.firstPedido.cantidad == c.firstPedido.cantidad ) return 1;
            else if( this.firstPedido.cantidad < c.firstPedido.cantidad ) return 1;
            else return -1;
        }
        else if( this.firstPedido.minFaltantes > c.firstPedido.minFaltantes ) return 1;
        else return -1;
    }
}
