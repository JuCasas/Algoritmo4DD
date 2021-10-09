package com.paqhoy.algoritmoAlgorutas.repository;

import com.paqhoy.algoritmoAlgorutas.model.Bloqueado;
import com.paqhoy.algoritmoAlgorutas.model.IntervaloNodo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface IntervaloNodoRepository extends CrudRepository<IntervaloNodo, Integer> {

}
