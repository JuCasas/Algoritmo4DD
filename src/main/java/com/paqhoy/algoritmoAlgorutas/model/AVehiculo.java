package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class AVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String placa;
    private Integer tipo_id;
    private String tipo;
    private Integer capacidad;
    private Double velocidad;
    private Double peso;
}
