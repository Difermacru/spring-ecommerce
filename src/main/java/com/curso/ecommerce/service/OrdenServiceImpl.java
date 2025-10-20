package com.curso.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {
	
	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";
		
		List<Orden> ordenes = findAll();                                               // 1) Trae TODAS las órdenes
		List<Integer> numeros = new ArrayList<Integer>();                              // 2) Guardará los números ya usados
		
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));   // 3) Convierte el campo getNumero() de cada orden (String) a int y lo mete en 'numeros'
		
		 // 4) Si no hay órdenes, empieza en 1. Si hay, toma el máximo y súmale 1.
		if(ordenes.isEmpty()) {
			numero=1;
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		 // 5) Formatea con ceros a la izquierda (intenta devolver un ancho fijo)
		if(numero<10) {
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="000000000"+String.valueOf(numero);
		}
		
		return numeroConcatenado;
	}
}
