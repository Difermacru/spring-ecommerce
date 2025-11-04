package com.curso.ecommerce.controller;

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

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@GetMapping("/registro")
	public String create(){
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	//Usuario usuario:llena este objeto con los campos del formulario (p. ej., email, password si existiera).
	//HttpSession session:sesión HTTP para guardar datos del usuario logueado (estado entre requests).
	public String acceder(Usuario usuario, HttpSession session){
		logger.info("Accesos : {}", usuario);
		
		//Retorna un Optional<Usuario> que puede estar presente (usuario existe) o vacío (no existe).
		Optional<Usuario>user = usuarioService.findByEmail(usuario.getEmail());
		//logger.info("Usuario de db: {}", user.get());
		
		//Si el usuario existe en BD
		if(user.isPresent()) {
			
			//Guarda en la sesión el id del usuario con la variable "idusuario". Así puedes identificar al usuario en próximas peticiones.
			session.setAttribute("idusuario", user.get().getId());
			
//toma el objeto user, despues toma el objeto Tipo para acceder al tipo de usuario ("admin" o "user") 
//con equals compara el Tipo con la palabra admin y si el Tipo es igual a admin devuelvo true y entra al if y se ejecuta la condicion
			if(user.get().getTipo().equals("admin")) {
				
				//Si es admin, redirige al panel de administración.
				return "redirect:/administrador";
			}else {
				
				//Si no es admin, redirige al home.
				return "redirect:/";
			}			
		}
		else{
			
			//Si no se encontró el usuario por email, lo registra en logs.
			logger.info("Usuario no existe");
		}
		
		//En cualquier caso (usuario no existe), redirige al home.
		return "redirect:/";
	}
	
	@GetMapping("/compras")
	//Model model: objeto que se usa para enviar datos a la vista (al HTML).
	//HttpSession session: te permite acceder a la sesión del usuario (datos guardados entre peticiones, como idusuario).
	public String obtenerComprar(Model model, HttpSession session) {
		
		//Lees de la sesión el atributo llamado "idusuario". Lo agregas al model con el nombre "session".
		//Eso hace que en la vista (por ejemplo en Thymeleaf) puedas acceder a ese valor con ${session}.
		model.addAttribute("session", session.getAttribute("idusuario"));
		session.getAttribute("idusuario");

//Llamas al servicio para buscar en la base de datos el usuario con ese id.//Obtiene de la sesión el valor guardado bajo la clave "idusuario" (normalmente el id del usuario logueado).
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
//Llamas al servicio de órdenes. usa el repositorio (Spring Data) para buscar todas las órdenes que pertenecen a ese Usuario.
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes",ordenes);
		
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		logger.info("Id de la orden: {}, id");
		
		Optional<Orden> orden=ordenService.findById(id);
		logger.info("Cantidad de detalles: {}", orden.get().getDetalle().size());
		
		model.addAttribute("detalles", orden.get().getDetalle());
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/detallecompra";
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");
		return"redirect:/";
	}
}
