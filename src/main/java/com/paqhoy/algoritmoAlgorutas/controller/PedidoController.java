package com.paqhoy.algoritmoAlgorutas.controller;

import com.paqhoy.algoritmoAlgorutas.model.AVehiculo;
import com.paqhoy.algoritmoAlgorutas.model.Pedido;
import com.paqhoy.algoritmoAlgorutas.model.Usuario;
import com.paqhoy.algoritmoAlgorutas.repository.VehiculoRepository;
import com.paqhoy.algoritmoAlgorutas.service.AlgoritmoService;
import com.paqhoy.algoritmoAlgorutas.service.PedidoService;
import com.paqhoy.algoritmoAlgorutas.service.UsuarioService;
import com.paqhoy.algoritmoAlgorutas.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VehiculoService vehiculoService;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file){
        return new ResponseEntity<>(pedidoService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping(value = "/porAtender")
    public ResponseEntity<List<Pedido>> getPedidosPorAtender(){
        return new ResponseEntity<>(pedidoService.getPedidosPorAtender(),HttpStatus.OK);
    }

    @GetMapping(value = "/autos")
    public ResponseEntity<List<Usuario>> getConductoresAutoDisponibles(){
        return new ResponseEntity<>(usuarioService.getConductoresAutoDisponibles(),HttpStatus.OK);
    }

    @GetMapping(value = "/motos")
    public ResponseEntity<List<Usuario>> getConductoresMotoDisponibles(){
        return new ResponseEntity<>(usuarioService.getConductoresMotoDisponibles(),HttpStatus.OK);
    }

    @GetMapping(value = "/vmotos")
    public ResponseEntity<List<AVehiculo>> getMotosDisponibles(){
        return new ResponseEntity<>(vehiculoService.getMotosDisponibles(),HttpStatus.OK);
    }

    @GetMapping(value = "/vautos")
    public ResponseEntity<List<AVehiculo>> getAutosDisponibles(){
        return new ResponseEntity<>(vehiculoService.getAutosDisponibles(),HttpStatus.OK);
    }
}
