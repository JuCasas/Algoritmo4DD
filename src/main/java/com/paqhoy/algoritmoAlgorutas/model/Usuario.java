package com.paqhoy.algoritmoAlgorutas.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombres;
    private String apellidos;
    private String username;
    private String password;
    private String correo;
    private String dni;
    private Integer estado;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;
    private Integer horario_id;
    private Integer rol_id;
    private Integer refrigerio;

    @Override
    public String toString(){
        String tipo = rol_id == 2 ? "Auto" : "Moto";
        return "Id chofer: " + id + " Tipo de chofer: " + tipo;
    }
}
