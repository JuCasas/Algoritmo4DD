package com.paqhoy.algoritmoAlgorutas.repository;

import com.paqhoy.algoritmoAlgorutas.model.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PedidoRepository extends CrudRepository<Pedido, Integer> {

    @Query(value="select * from pedido where estado_id = 1 and fecha_pedido <= now()",nativeQuery = true)
    List<Pedido> findPedidosPorAtender();



}
