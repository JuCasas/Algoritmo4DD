package com.paqhoy.algoritmoAlgorutas.model;


public class SRuta implements Comparable<SRuta>{
    public Integer id;
    public Integer tiempoMinutosFin;
    public Integer tipoVehiculo;
    public Integer recorridoEnKm;

    @Override
    public int compareTo(SRuta ruta) {
        return this.tiempoMinutosFin.compareTo(ruta.tiempoMinutosFin);
    }
}
