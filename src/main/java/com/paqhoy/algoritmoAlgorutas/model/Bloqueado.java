package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Bloqueado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String nodos;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getInicio() {
        return this.inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return this.fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public String getNodos() {
        return this.nodos;
    }

    public void setNodos(String nodos) {
        this.nodos = nodos;
    }
}
