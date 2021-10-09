package com.paqhoy.algoritmoAlgorutas.repository;

import com.paqhoy.algoritmoAlgorutas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

//    select U.*, (
//    select count(*)
//    from refrigerio
//    where usuario_id = U.id and
//    inicio > date_sub(now(), interval 21 hour)
//            ) as refrigerio
//    from usuario U
//    where U.rol_id = 2 and
//    U.horario_id = 3 and
//    U.estado = 1;

    @Query(value="select *, (select count(*) from refrigerio R where R.usuario_id = U.id and inicio > date_sub(now(), interval 25 hour)) as almuerzo from usuario U\n" +
            "where U.rol_id = 2 and U.horario_id = ?1 and U.estado = 1\n" +
            "having almuerzo = 1;",nativeQuery = true)
    List<Usuario> findConductoresAutoDisponibles(Integer horario);

    @Query(value="select *, (select count(*) from refrigerio R where R.usuario_id = U.id and inicio > date_sub(now(), interval 25 hour)) as almuerzo from usuario U\n" +
            "where U.rol_id = 3 and U.horario_id = ?1 and U.estado = 1\n" +
            "having almuerzo = 1;",nativeQuery = true)
    List<Usuario> findConductoresMotoDisponibles(Integer horario);

    @Query(value="select U.*, (select count(*) from refrigerio where usuario_id = U.id and inicio > date_sub(now(), interval 21 hour)) as refrigerio from usuario U\n" +
            "where U.rol_id != 1 and U.horario_id = ?1 and U.estado = 1;",nativeQuery = true)
    List<Usuario> findConductoresDisponibles(Integer horario);

    @Modifying
    @Query(value="update usuario set estado = 0 where id = ?1", nativeQuery = true)
    int cambiarEstadoUsuario(int idUsuario);
}
