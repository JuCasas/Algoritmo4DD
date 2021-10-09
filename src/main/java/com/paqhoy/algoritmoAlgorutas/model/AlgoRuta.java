package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "ruta")
@Table(name = "ruta")
public class AlgoRuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private double distancia;
    private double costo;
    private Integer usuario_id;
    private Integer vehiculo_id;
    private Integer estado_id;
}
