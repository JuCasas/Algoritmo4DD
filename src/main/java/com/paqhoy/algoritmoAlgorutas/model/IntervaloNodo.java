package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "nodo_intervalo")
@Table(name = "nodo_intervalo")
public class IntervaloNodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer nodo_id;
    private Integer intervalo_id;
}
