package com.paqhoy.algoritmoAlgorutas.service;

import com.paqhoy.algoritmoAlgorutas.model.Configuraciones;
import com.paqhoy.algoritmoAlgorutas.model.Usuario;
import com.paqhoy.algoritmoAlgorutas.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getConductoresAutoDisponibles(){
        Integer turno = calcularTurno(-1);
        return  usuarioRepository.findConductoresAutoDisponibles(turno);
    }

    public List<Usuario> getConductoresMotoDisponibles(){
        Integer turno = calcularTurno(-1);
        return usuarioRepository.findConductoresMotoDisponibles(turno);
    }

    public List<Usuario> getConductoresDisponibles(){
        Integer turno = calcularTurno(-1);
        return usuarioRepository.findConductoresDisponibles(turno);
    }

    private Integer calcularHoraRefrigerio(Integer turno){
        return (8*(turno-1) + 6 + Configuraciones.horaRefrigerio) % 24;
    }

    private Integer calcularTurno(Integer hora){
        if (hora == -1) hora = LocalDateTime.now().minus(Duration.ofHours(5)).getHour();
        Integer turno;
        if(hora >= 6 && hora < 14) turno = 1;
        else if(hora >= 14 && hora < 22) turno = 2;
        else turno = 3;
        return turno;
    }
}
