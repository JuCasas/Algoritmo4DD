package com.paqhoy.algoritmoAlgorutas.controller;

import com.paqhoy.algoritmoAlgorutas.model.Bloqueado;
import com.paqhoy.algoritmoAlgorutas.model.CallesBloqueadas;
import com.paqhoy.algoritmoAlgorutas.service.CallesBloqueadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/callesBloqueadas")
public class CallesBloqueadasController {
    @Autowired
    private CallesBloqueadasService callesBloqueadasService;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file){
        return new ResponseEntity<>(callesBloqueadasService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping(value = "/getBloqueadas")
    public ResponseEntity<List<CallesBloqueadas>> obtenerCallesBloqueadas(){
        return new ResponseEntity<>(callesBloqueadasService.obtenerCallesBloqueadas(), HttpStatus.OK);
    }

    @GetMapping(value = "/getBloqueadasActuales")
    public ResponseEntity<List<CallesBloqueadas>> obtenerCallesBloqueadasActuales(){
        return new ResponseEntity<>(callesBloqueadasService.obtenerCallesBloqueadasActuales(), HttpStatus.OK);
    }
}
