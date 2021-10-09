package com.paqhoy.algoritmoAlgorutas.repository;

import com.paqhoy.algoritmoAlgorutas.model.AVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<AVehiculo,String> {

    @Query(value = "select V.id, V.placa, V.tipo_id, TV.tipo, TV.capacidad, TV.velocidad, TV.costo_km from vehiculo V, tipo_vehiculo TV\n" +
            "where V.tipo_id = TV.id and V.estado = 1 and V.tipo_id = 1;", nativeQuery = true)
    List<AVehiculo> getAutosDisponibles();

    @Query(value = "select V.id, V.placa, V.tipo_id, TV.tipo, TV.capacidad, TV.velocidad, TV.costo_km from vehiculo V, tipo_vehiculo TV\n" +
            "where V.tipo_id = TV.id and V.estado = 1 and V.tipo_id = 2;", nativeQuery = true)
    List<AVehiculo> getMotosDisponibles();
}
