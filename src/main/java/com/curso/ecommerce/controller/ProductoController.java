package com.curso.ecommerce.controller;

import org.slf4j.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;

//4.SE CREA EL ProductoController Y SE IMPLEMENTAN LOS METODOS
@Controller

//SE CREA LA RUTA
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	//SE CREA EL METODO GET
	@GetMapping("")
	public String show() {
		//SE REDIRECCIONA HACIA LA CARPETA productos Y SE RENDERIZA el archivo show
		return "productos/show";
	}
	
	//SE CREA EL METODO GET EL CUAL REDIRECCIONARA Y REDERIZARA EL ARCHIVO create
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	//SE CREA EL METODO POST CON SU RUTA
	@PostMapping("/save")
	//SE CREA EL METODO SAVE Y SE PASARA UN OBJETO DE CLASE Producto COMO PARAMETRO
	public String save(Producto producto) {
		
		LOGGER.info("este es el objeto producto {}",producto);
		//SE CREA UN NUEVO OBJETO DE TIPO USUARIO, ES EL USUARIO QUE SE ASOCIARA AL PRODUCTO
		Usuario u= new Usuario(1, "", "", "", "", "", "", "");
		
		//SE RELACIONA EL producto CON EL Usuario (u), DE ESTA MANERA QUED VINCULADO A UN Usuario 
		producto.setUsuario(u);
		
		//SE LLAMA EL METODO save DE productoService PARA GUARDAR LOS DATOS DEL PRODUCTO JUNTO CON EL USUARIO ASIGNADO 
		productoService.save(producto);
		
		//DESPUES DE GUARDAR EL Producto SE REDIRECCIONA A HACIA LA RUTA productos
		return "redirect:/productos";
	}
	
}
