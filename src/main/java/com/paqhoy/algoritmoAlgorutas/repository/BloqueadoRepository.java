package com.paqhoy.algoritmoAlgorutas.repository;

import com.paqhoy.algoritmoAlgorutas.model.Bloqueado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BloqueadoRepository extends JpaRepository<Bloqueado,String> {
    @Query(value = "select I.*, group_concat(NI.nodo_id) as nodos from intervalo_bloqueo I, nodo_intervalo NI\n" +
            "where I.fin > date_sub(now(), interval 5 hour)\n" +
            "\tand I.inicio < date_add(now(), interval 7 hour)\n" +
            "    and NI.intervalo_id = I.id\n" +
            "group by I.id",nativeQuery = true)
    List<Bloqueado> obtenerCallesBloqueadas();

    @Query(value = "select I.*, group_concat(NI.nodo_id) as nodos from intervalo_bloqueo I, nodo_intervalo NI\n" +
            "where I.fin > date_sub(now(), interval 5 hour)\n" +
            "\tand I.inicio <= date_sub(now(), interval 5 hour)\n" +
            "    and NI.intervalo_id = I.id\n" +
            "group by I.id",nativeQuery = true)
    List<Bloqueado> obtenerCallesBloqueadasActuales();
}
