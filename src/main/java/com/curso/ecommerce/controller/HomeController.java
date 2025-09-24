package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("")
	public String home(Model model) {
		
		//EL METODO TRAERA TODOS LOS PRODUCTOS DE LA BASE DE DATOS Y LOS PONDRA EN LA VARIABLE productos
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}

}
