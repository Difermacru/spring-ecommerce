package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.ProductoRepository;

//3.SE IMPLEMENTAN LOS METODOS QUE SE CREARON EN ProductoService
@Service
public class ProductoServiceImpl implements ProductoService{
	
	//se usa para poder usar los metodos de JpaRepository que se invocaron en la interfaz ProductoRepository
	@Autowired
	private ProductoRepository productoRepository;

	@Override
	public Producto save(Producto producto) {
		//se usara el metodo guardar de la clase PoductoRepository, guardara un objeto de tipo Producto
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		//se usara el metodo fondById de la clase PoductoRepository, buscara un objeto de tipo Producto por id
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		//se usara el metodo guardar de la clase PoductoRepository, para actualizar una clase de tipo Producto
		productoRepository.save(producto);
		
	}

	@Override
	public void delete(Integer id) {
		//se usara el metodo delete de la clase PoductoRepository, borrara un objeto de tipo Producto por id
		productoRepository.deleteById(id);
		
	}

}
