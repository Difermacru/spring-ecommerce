package com.curso.ecommerce.service;

public interface IOrdenService {
	List<Orden>finAll();
	Orden save (Orden orden);
	String generarNumeriOrden();
}
