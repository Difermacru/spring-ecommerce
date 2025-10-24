package com.curso.ecommerce.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

//4.SE CREA EL ProductoController Y SE IMPLEMENTAN LOS METODOS
@Controller

//SE CREA LA RUTA
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
	
	@Autowired
	private IUsuarioService usuarioService;
	
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
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		
		LOGGER.info("este es el objeto producto {}",producto);
		
		//SE CREA UN NUEVO OBJETO DE TIPO USUARIO, ES EL USUARIO QUE SE ASOCIARA AL PRODUCTO
		Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		//SE RELACIONA EL producto CON EL Usuario (u), DE ESTA MANERA QUEDa VINCULADO A UN Usuario 
		producto.setUsuario(u);
		
		//PARA AGREGAR IMAGENES
		if(producto.getId()==null) { //cuando se crea un producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		
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
	public String update (Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p =new Producto();
		p=productoService.get(producto.getId()).get();
		
		if(file.isEmpty()) {//se edita el producto pero no se cambia la imagen
			
			producto.setImagen(p.getImagen());
			
			//CUANDO SE EDITA LA IMAGEN
		}else {
			//Se guarda la nueva imagen subida y se asigna al producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		//SE LLAMA EL METODO update DEFINIDO EN EL ProductoService Y SE LE PASA EL PRODUCTO A MODIFICAR
		productoService.update(producto);
		
		//Y DESPUES DE ACTUALIZAR SE REDICCIONA HACIA LA RUTA productos
		return"redirect:/productos";
	}
	
	
	//SE CREA EL METODO GET CON SU RUTA
	@GetMapping("/delete/{id}")
	//SE CREA EL METODO DELETE Y SE PASA COMO PARAMETRO EL ID 
	public String delete(@PathVariable Integer id) {
		
		Producto p = new Producto();
		//productoService.get SE BUSCA EL PRODUCTO POR EL ID PARA ELIMINARLO
		p=productoService.get(id).get();
		
		//Si la imagen actual del producto no es("default.jpg"),se elimina del servidor para evitar archivos innecesarios
		if(!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		//SE LLAMA EL METODO delete Y SE LE PASA COMO PARAMETRO EL id A ELIMINAR
		productoService.delete(id);
		
		//DESPUES DE ELIMINAR EL REGISTRO SE REDIRECCIONA HACIA LA RUTA productos
		return "redirect:/productos";
	}
	
}
