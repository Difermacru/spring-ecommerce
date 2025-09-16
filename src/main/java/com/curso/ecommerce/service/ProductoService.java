package com.curso.ecommerce.service;

import java.util.Optional;

import com.curso.ecommerce.model.Producto;

public interface ProductoService {

	//2.SE CREAN LOS METODOS PARA GUARDAR, BUSCAR, ACTUALIZAR Y BORRAR PARA EL OBJETO PRODUCTO
	
	//extrae un objeto de tipo Producto y guarda ese producto 
	public Producto save(Producto producto);
	//con Optional trae un Producto si el id existe, y si el id es null no trae nada
	public Optional<Producto>get(Integer id);
	//actualiza un objeto de tipo producto
	public void update(Producto producto);
	//borra un registro por su id
	public void delete(Integer id);
}
