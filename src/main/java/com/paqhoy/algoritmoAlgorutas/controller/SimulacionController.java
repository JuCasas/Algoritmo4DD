package com.paqhoy.algoritmoAlgorutas.controller;

import com.paqhoy.algoritmoAlgorutas.algoritmo.Simulacion;
import com.paqhoy.algoritmoAlgorutas.model.APedido;
import com.paqhoy.algoritmoAlgorutas.model.CallesBloqueadas;
import com.paqhoy.algoritmoAlgorutas.model.SimulacionParametros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/simulacion")
public class SimulacionController {

    @Autowired
    private Simulacion simulacion;

    @PostMapping(value = "/uploadPedidos")
    public ResponseEntity<String> uploadPedidos(@RequestParam(value = "file") MultipartFile file){
        return new ResponseEntity<>(simulacion.subirArchivoPedidos(file), HttpStatus.OK);
    }

    @GetMapping(value = "/getPedidos")
    public ResponseEntity<List<APedido>> getPedidos(){
        return new ResponseEntity<>(simulacion.getPedidos(), HttpStatus.OK);
    }

    @PostMapping(value = "/uploadCallesBloqueadas")
    public ResponseEntity<String> uploadCallesBloqueadas(@RequestParam(value = "file") MultipartFile file){
        return new ResponseEntity<>(simulacion.subirArchivoCallesBloqueadas(file), HttpStatus.OK);
    }

    @GetMapping(value = "/getListaCallesBloqueadas")
    public ResponseEntity<List<CallesBloqueadas>> getListaCallesBloqueadas(){
        return new ResponseEntity<>(simulacion.getListaCallesBloqueadas(), HttpStatus.OK);
    }

    @PostMapping(value = "/empezarSimulacion")
    public ResponseEntity<String> empezarSimulacion(@RequestBody SimulacionParametros parametros){
        simulacion.inicializar(parametros);
        return new ResponseEntity<>("Simulando", HttpStatus.OK);
    }

    @PostMapping(value = "/reiniciarSimulacion")
    public ResponseEntity<String> reiniciarSimulacion(){
        simulacion.reiniciarSimulacion();
        return new ResponseEntity<>("Reiniciado", HttpStatus.OK);
    }
}
