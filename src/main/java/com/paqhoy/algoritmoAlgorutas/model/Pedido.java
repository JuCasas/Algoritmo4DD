package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer cantidad;
    private LocalDateTime fecha_pedido;
    private LocalDateTime fecha_limite;
    private LocalDateTime fecha_entrega;
    private Double precio;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;
    private Integer cliente_id;
    private Integer direccion_id;
    private Integer estado_id;
    private Integer tipo_id;
}
