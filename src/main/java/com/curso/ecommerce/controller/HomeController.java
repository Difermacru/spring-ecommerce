package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired 
	private IDetalleOrdenService detalleOrdenService;
	
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
	//REQUEST ID Y CANTIDAD: RECIBE EL ID Y LA CANTIDAD DEL PRODUCTO SELECCIONADO POR EL USUARIO
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		//USA EL METODO GET PARA BUSCAR UN PRODUCTO POR SU ID 
		Optional<Producto>optionalProducto = productoService.get(id);
		log.info("Prooducto añadido: {}", optionalProducto.get());
		log.info("cantidad: {}", cantidad);
		producto=optionalProducto.get();
		
		//CON GETTER OBTIENE EL VALOR Y SETTER PERMITE ACTUALIZAR LOS VALORES 
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		//SE REALIZA LA OPERACION PARA OBTENER EL TOTAL Y SE LE ASIGNA A SETTOTAL
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		//MUESTRA TODOS LOS DATOS DEL PRODUCTO
		detalleOrden.setProducto(producto);
		
		//VALIDAR QUE EL PRODUCTO NO SE AGREGUE DOS VECES
		// id del producto que se intenta agregar
		Integer idProducto = producto.getId();
		
//detalles.stream()recorre la lista detalles      //anyMatch(...): devuelve true si existe al menos un DetalleOrden cuyo producto.id coincide con idProducto
		boolean ingresado = detalles.stream().anyMatch(p->p.getProducto().getId()==idProducto);
		
		// si no está, lo agrega a detalleOrden
		if(!ingresado) {
			//AGREGA LOS VALORES PRECIO,NOMBRE Y PRODUCTO A LA VARIABLE DETALLES
			detalles.add(detalleOrden);
		}
		
		//CONVIERTE LA LISTA DETALLES EN UN STREAM, TOMA DE CADA DETALLE ORDEN SU TOTAL, SUMA TODO ESOS TOTALES 
		//Y GUARDA EL RESULTADO EN SUMATOTAL
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		//ASIGNA LA SUMATOTAL A SETTOTAL DE ORDEN
		orden.setTotal(sumaTotal);
		
		//SE ENCARGARA DE MOSTRAR TODOS LOS DATOS DE LA LISTA DETALLES EN LA VISTA CART
		model.addAttribute("cart", detalles);
		//SE ENCARGARA DE MOSTRAR TODOS LOS DATOS DE LA ORDEN EN LA VISTA ORDEN
		model.addAttribute("orden",orden);
		
		
		//RETORNARA LA PAGINA DE CARRITO
		return "usuario/carrito";
			
	}
	
	//QUITAR UN PRODUCTO DEL CARRITO
	//SE LLAMA AL METODO GET Y RECIBIRA COMO PARAMETRO UN ID
	@GetMapping("/delete/cart/{id}")
	//SE CREA EL METODO DELETE CON EL PARAMETRO ID Y EL METODO MODEL QUE MOSTRARA LOS DATOS A ELIMINAR EN EL FORMULARIO
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		
	
		List<DetalleOrden>ordenesNueva= new ArrayList<DetalleOrden>();
		
		//FILTRA EL CARRITO(lista detalles) DEJANDO SOLO LOS ITEMS CUYO PRODUCTO NO TENGAN EL ID RECIBIDO
		for(DetalleOrden detalleOrden : detalles) {
			if(detalleOrden.getProducto().getId() != id) {
				
				//LOS PRODUCTOS QUE QUEDAN EN detalles PASAN A LA NUEVA LISTA ordenesNuevas
				ordenesNueva.add(detalleOrden);
			}
		}
		
		//SE REEMPLAZA LA LISTA detalles POR ordenesNueva
		detalles = ordenesNueva;
		
		//SE RECALCULA EL TOTAL 
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		//PASA LOS DATOS A LA VISTA 
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		//Y MUESTRA LA PAGINA DEL CARRITO
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model) {
		
		Usuario usuario = usuarioService.findById(1).get();
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		return "usuario/resumenorden";
		
	}
	
	//guardar la orden
	@GetMapping("/saveOrder")
	public String saveOrder() {
		
		//Toma la fecha y hora actual del sistema.
		Date fechaCreacion = new Date();
		
		//Guarda la marca de tiempo en la entidad orden.
		orden.setFechaCreacion(fechaCreacion);
		
		//Pide al servicio el número correlativo para la orden (usual con ceros a la izquierda).
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//usuario
		Usuario usuario = usuarioService.findById(1).get();
		
		//Asocia la orden al usuario.
		orden.setUsuario(usuario);
		
		//Toma este objeto orden, conviértelo en una fila y guárdalo físicamente en la tabla orden
		ordenService.save(orden);
		
		//guardar detalles
		//Recorre la lista detalles (cada elemento es un DetalleOrden).
		for (DetalleOrden dt:detalles) {
			
			//le pone a cada detalle la referencia a la orden “padre”, Esto llena la FK (orden_id) cuando se guarde en la BD.
			dt.setOrden(orden);
			
			//guarda cada detalle en la base de datos (inserta una fila en la tabla de detalles con su orden_id).
			detalleOrdenService.save(dt);
		}
		
		//limpiar lista y orden
		orden = new Orden();
		detalles.clear();
		
		return "redirect:/";	
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model){
		log.info("Nombre del producto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream().filter(p->p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "usuario/home";
	}
}
