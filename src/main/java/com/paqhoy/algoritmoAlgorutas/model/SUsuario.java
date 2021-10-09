package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class SUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer estado;
    private Integer horario_id;
    private Integer rol_id;
    private Integer ocupadoHasta = -1;

    @Override
    public String toString(){
        String tipo = rol_id == 2 ? "Auto" : "Moto";
        return "Id chofer: " + id + " Tipo de chofer: " + tipo;
    }
}
