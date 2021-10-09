package com.paqhoy.algoritmoAlgorutas.controller;

import com.paqhoy.algoritmoAlgorutas.model.Ruta;
import com.paqhoy.algoritmoAlgorutas.model.SimulacionParametros;
import com.paqhoy.algoritmoAlgorutas.model.UbicacionAlmacen;
import com.paqhoy.algoritmoAlgorutas.service.AlgoritmoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/algoritmo")
public class AlgoritmoController {

    @Autowired
    private AlgoritmoService algoritmoService;

    @GetMapping(value = "/getListaAutos")
    public ResponseEntity<ArrayList> getListaAutos(){
        return new ResponseEntity<>(algoritmoService.getListaAutos(), HttpStatus.OK);
    }

    @GetMapping(value = "/getListaMotos")
    public ResponseEntity<ArrayList> getListaMotos(){
        return new ResponseEntity<>(algoritmoService.getListaMotos(), HttpStatus.OK);
    }

    @GetMapping(value = "/getListaChoferesMoto")
    public ResponseEntity<ArrayList> getListaChoferesMoto(){
        return new ResponseEntity<>(algoritmoService.getListaChoferesMoto(), HttpStatus.OK);
    }

    @GetMapping(value = "/getListaChoferesAuto")
    public ResponseEntity<ArrayList> getListaChoferesAuto(){
        return new ResponseEntity<>(algoritmoService.getListaChoferesAuto(), HttpStatus.OK);
    }

    @GetMapping(value = "/getListaPedidos")
    public ResponseEntity<ArrayList> getListaPedidos(){
        return new ResponseEntity<>(algoritmoService.getListaPedidos(), HttpStatus.OK);
    }

    @GetMapping(value = "/getRutasPlanificadas")
    public ResponseEntity<String> getRutasPlanificadas(){
        return new ResponseEntity<>(algoritmoService.getRutasPlanificadas(), HttpStatus.OK);
    }

    @PostMapping(value = "/cambiarAlmacen")
    public ResponseEntity<String> cambiarAlmacen(@RequestBody UbicacionAlmacen parametros){
        return new ResponseEntity<>(algoritmoService.cambiarAlmacen(parametros), HttpStatus.OK);
    }

    @GetMapping(value = "/getAlmacen")
    public ResponseEntity<HashMap<String, Integer>> getAlmacen(){
        return new ResponseEntity<>(algoritmoService.getAlmacen(), HttpStatus.OK);
    }
}
