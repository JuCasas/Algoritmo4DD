package com.paqhoy.algoritmoAlgorutas;

import com.paqhoy.algoritmoAlgorutas.algoritmo.Algoritmo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlgoritmoAlgorutasApplication {

	public static void main(String[] args) {
//		SpringApplication.run(AlgoritmoAlgorutasApplication.class, args);
		Algoritmo algoritmo = new Algoritmo();
		algoritmo.inicializar();
		algoritmo.generarRutas();
	}

}
