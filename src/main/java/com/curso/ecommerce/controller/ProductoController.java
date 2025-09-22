package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	//MODEL PERMITE ENVIAR DATOS DESDE EL BACKEND HACIA LA VISTA
	public String show(Model model) {
		
		//DEVUELVE TODOS LA LISTA DE PRODUCTOS
		model.addAttribute("productos", productoService.findAll());
		
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
	
	//SE CREA EL METODO GET CON SU RUTA
	@GetMapping("/edit/{id}")
	//SE CREA EL METODO EDIT Y SE LE PASA COMO PAMETRO EL ID POR EL CUAL SE BUSCARA
	public String edit(@PathVariable Integer id, Model model){
		
		//SE CREA UN NUEVO OBJETO DE TIPO Producto, SERA EL QUE ALMACENARA EL PRODUCTO QUE SE BUSCA POR EL ID
		Producto producto= new Producto();
		
		//SE USA EL METODO get DE ProductoService PARA BUSCAR EL PRDUCTO POR EL ID 
		Optional<Producto> optionalProducto=productoService.get(id);
		
		//CUANDO ENCUENTRA EL PRODUCTO SE ALMACENA EN LA VARIABLE producto
		producto= optionalProducto.get();
		
		LOGGER.info("Producto buscado: {}",producto);
		
		//MODEL ENVIARA DATOS DESDE EL BACKEND HACIA LA VISTA
		model.addAttribute("producto", producto);
		
		//RENDERIZA EL ARCHIVO edit
		return "productos/edit";
	}
	
	//SE CREA EL METODO POST CON SU RUTA
	@PostMapping("/update")
	//SE CREA EL METODO update SE PASA COMO PARAMETRO EL OBJETO Producto
	public String update (Producto producto) {
		
		//SE LLAMA EL METODO update DEFINIDO EN EL ProductoService Y SE LE PASA EL PRODUCTO A MODIFICAR
		productoService.update(producto);
		
		//Y DESPUES DE ACTUALIZAR SE REDICCIONA HACIA LA RUTA productos
		return"redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		productoService.delete(id);
		return "redirect:/productos";
	}
	
}
