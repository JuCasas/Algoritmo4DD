package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class SVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer tipo_id;
    private Integer capacidad;
    private Double velocidad;
    private Double costo_km;
    private Integer ocupadoHasta = -1;
}
