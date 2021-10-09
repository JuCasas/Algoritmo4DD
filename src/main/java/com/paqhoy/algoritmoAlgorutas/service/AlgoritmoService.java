package com.paqhoy.algoritmoAlgorutas.service;

import com.paqhoy.algoritmoAlgorutas.algoritmo.Algoritmo;
import com.paqhoy.algoritmoAlgorutas.model.Configuraciones;
import com.paqhoy.algoritmoAlgorutas.model.UbicacionAlmacen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class AlgoritmoService {

    @Autowired
    private Algoritmo algoritmo;

    public String cambiarAlmacen(UbicacionAlmacen almacen){
        Configuraciones.almacenX = almacen.almacenX;
        Configuraciones.almacenY = almacen.almacenY;
        Configuraciones.almacen = almacen.almacenX + almacen.almacenY * 71 + 1;
        return "Done!";
    }

    public HashMap<String, Integer> getAlmacen(){
        HashMap<String, Integer> almacen = new HashMap<>();
        almacen.put("almacenX", Configuraciones.almacenX);
        almacen.put("almacenY", Configuraciones.almacenY);
        return almacen;
    }

    public ArrayList getListaAutos(){
        algoritmo.inicializar();
        return (ArrayList) algoritmo.listaVehiculoTipo2;
    }

    public ArrayList getListaMotos(){
        algoritmo.inicializar();
        return (ArrayList) algoritmo.listaVehiculoTipo1;
    }

    public ArrayList getListaChoferesMoto(){
        algoritmo.inicializar();
        return (ArrayList) algoritmo.listaChoferesMoto;
    }

    public ArrayList getListaChoferesAuto(){
        algoritmo.inicializar();
        return (ArrayList) algoritmo.listaChoferesAuto;
    }

    public ArrayList getListaPedidos(){
        algoritmo.inicializar();
        return (ArrayList) algoritmo.listaPedidos;
    }

    public String getRutasPlanificadas(){
        String respuesta = algoritmo.inicializar();
        if(respuesta != "correcto") return respuesta;
        return algoritmo.generarRutas();
    }

}
