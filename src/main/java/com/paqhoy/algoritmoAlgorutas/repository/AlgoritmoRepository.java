package com.paqhoy.algoritmoAlgorutas.repository;

import com.paqhoy.algoritmoAlgorutas.model.AlgoRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AlgoritmoRepository extends JpaRepository<AlgoRuta,String> {

    @Modifying
    @Query(value="update pedido set estado_id = 2 where id = ?1", nativeQuery = true)
    void cambiarEstadoPedido(int idPedido);

    @Modifying
    @Query(value="update vehiculo set estado = 0 where id = ?1", nativeQuery = true)
    void cambiarEstadoVehiculo(int idVehiculo);

    @Modifying
    @Query(value="insert into ruta_nodo (orden,recorrido,ruta_id,nodo_id) values(?3,0,?1,?2)", nativeQuery = true)
    void insertarNodoRuta(int idRuta, int idNodo, int orden);

    @Modifying
    @Query(value="insert into pedido_ruta (orden,estado,pedido_id,ruta_id) values(?3,0,?2,?1)", nativeQuery = true)
    void insertarPedidoRuta(int idRuta, int idPedido, int orden);
}
