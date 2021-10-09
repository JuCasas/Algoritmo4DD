package com.paqhoy.algoritmoAlgorutas.service;

import com.paqhoy.algoritmoAlgorutas.model.AVehiculo;
import com.paqhoy.algoritmoAlgorutas.repository.VehiculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<AVehiculo> getAutosDisponibles(){
        return vehiculoRepository.getAutosDisponibles();
    }

    public List<AVehiculo> getMotosDisponibles(){
        return vehiculoRepository.getMotosDisponibles();
    }
}
