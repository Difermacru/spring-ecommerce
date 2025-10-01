package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;
	
	//ITEMS DEL CARRITO (AGREGADOS O QUITADOS POR EL USUARIO)
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//SON LOS DATOS DE LA ORDEN
	Orden orden = new Orden();
	
	@GetMapping("")
	//MODEL TRAERA TODOS LOS DATOS DE LA BASE DE DATOS Y LA MOSTRARA EN EL FORMULARIO
	public String home(Model model) {
		
		//findAll ES EL METODO QUE TRAERA TODOS LOS PRODUCTOS DE LA BASE DE DATOS Y LOS PONDRA EN LA VARIABLE productos
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}
	
	//SE CREA UN METODO GET CON SU RUTA LA CUAL RECIBE UN PARAMETRO ID
	@GetMapping("productohome/{id}")
	//SE CREA EL METODO productoHome EL CUAL RECIBE UN ID Y REDIRIGE A LA PAGINA DE productohome
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parametro {}", id);
		
		Producto producto = new Producto();
		Optional<Producto> productoOptional= productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		//AL ENVIAR UN ID SE MUESTRA LA VISTA DE productohome
		return "usuario/productohome";
	}
	
	//SE CREA UN METODO POST CON SU RUTA 
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto>optionalProducto = productoService.get(id);
		log.info("Prooducto añadido: {}", optionalProducto.get());
		log.info("cantidad: {}", cantidad);
		//RETORNARA LA PAGINA DE CARRITO
		return "usuario/carrito";
	}

}
