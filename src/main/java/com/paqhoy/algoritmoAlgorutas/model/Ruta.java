package com.paqhoy.algoritmoAlgorutas.model;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    public List<Integer> recorrido;
    public List<Integer> retorno;
    public List<APedido> pedidos;
    public AVehiculo vehiculo;
    public int tiempoMin;
    public Usuario chofer;
    public int capacidad;

    public Ruta(){
        this.recorrido = new ArrayList< Integer >();
        this.retorno = new ArrayList< Integer >();
        this.pedidos = new ArrayList< APedido >();
        this.tiempoMin = Integer.MAX_VALUE;
        this.chofer = null;
        this.capacidad = 0;
        this.vehiculo = new AVehiculo();
    }

    public Ruta(AVehiculo vehiculo, int capacidad){
        this.recorrido = new ArrayList< Integer >();
        this.retorno = new ArrayList< Integer >();
        this.pedidos = new ArrayList< APedido >();
        this.tiempoMin = Integer.MAX_VALUE;
        this.chofer = null;
        this.capacidad = capacidad;
        this.vehiculo = new AVehiculo();
        this.vehiculo = vehiculo;
    }

    public void addPedido(APedido pedido){
        if(pedido.minFaltantes < tiempoMin) tiempoMin = pedido.minFaltantes;
        pedidos.add(pedido);
    }

    public void addNodo(int idNodo){
        recorrido.add(idNodo);
    }

    public void addNodoRetorno(int idNodo){
        retorno.add(idNodo);
    }
}
