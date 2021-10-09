package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "intervalo_bloqueo")
@Table(name = "intervalo_bloqueo")
public class Intervalo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime inicio;
    private LocalDateTime fin;
}
