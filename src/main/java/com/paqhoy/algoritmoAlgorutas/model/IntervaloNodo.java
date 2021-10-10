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

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNodo_id() {
        return this.nodo_id;
    }

    public void setNodo_id(Integer nodo_id) {
        this.nodo_id = nodo_id;
    }

    public Integer getIntervalo_id() {
        return this.intervalo_id;
    }

    public void setIntervalo_id(Integer intervalo_id) {
        this.intervalo_id = intervalo_id;
    }
}
