package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
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
}
