package com.paqhoy.algoritmoAlgorutas.model;

public class Configuraciones {
    //parametros
    public static int almacenX = 12; //coordenadaX del almancen
    public static int almacenY = 8; //coordenadaY del almancen
    public static int almacen = almacenX + almacenY * 71 + 1; //id del nodo del almacen
    public static int horaRefrigerio = 3; //luego de la tercera hora se puede tener el refrigerio es decir, si empieza a las 6, desde las 9
    public static int V = 3621; //cantidad de vertices del grafo
    public static int E = 14240; //cantidad de aristas del grafo
    public static double penalidad = 20.0;
    public static double precio = 200.0;
    public static double costoKmAuto = 5.0;
    public static double costoKmMoto = 3.0;
}
