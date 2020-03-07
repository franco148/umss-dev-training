package com.fral.spring.billing.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fral.spring.billing.models.entity.Client;
import com.fral.spring.billing.services.ClientService;
import com.fral.spring.billing.services.UploadFileService;
import com.fral.spring.billing.utils.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private UploadFileService uploadFileService;
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	
	
	@Secured({"ROLE_USER"})
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> seePhoto(@PathVariable String filename) {
		
		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")//or hasAnyRole if we want to specify more than a role.
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Client cliente = clientService.fetchByIdWithInvoices(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "Client does not exist in the system.");
			return "redirect:/listar";
		}

		model.put("client", cliente);
		model.put("title", "Client detail: " + cliente.getName());
		return "ver";
	}
	
	@GetMapping(value= {"/listar", "/"})
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model,
						Authentication authentication, HttpServletRequest request) {
		
		//FIRST APPROACH for getting authenticated user name;
		if(authentication != null) {
			logger.info("Hi authenticated user, your username is: ".concat(authentication.getName()));
		}
		
		//SECOND APPROACH for getting authenticated user name;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null) {
			logger.info("Using static approach SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: ".concat(auth.getName()));
		}

		//GETTING ROLES FIRST APPROACH
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hi ".concat(auth.getName()).concat(" you have access!"));
		} else {
			logger.info("Hi ".concat(auth.getName()).concat(" you have NO access!"));
		}

		/***
		 * GETTING ROLES SECOND APPROACH
		 * 
		 * Second parameter for SecurityContextHolder would be "ROLE_", then in if clause you
		 * will only need to ask for "ADMIN". If second parameter is empty, then in if
		 * clause you will need to verify "ROLE_ADMIN"
		 */
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
		
		if(securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("Approach with SecurityContextHolderAwareRequestWrapper: Hi ".concat(auth.getName()).concat(" you have access!"));
		} else {
			logger.info("Approach with SecurityContextHolderAwareRequestWrapper: Hi ".concat(auth.getName()).concat(" you have no access!"));
		}

		//GETTING ROLES THRID APPROACH
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Approach with HttpServletRequest: Hi ".concat(auth.getName()).concat(" you have access!"));
		} else {
			logger.info("Approach with HttpServletRequest: Hi ".concat(auth.getName()).concat(" you have no access!"));
		}

		
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Client> clientes = clientService.findAll(pageRequest);
		
		PageRender<Client> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("title", "Clients List");
		model.addAttribute("clients", clientes);
		model.addAttribute("page", pageRender);
		
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {

		Client cliente = new Client();
		model.put("client", cliente);
		model.put("title", "Create Client");
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Client cliente = null;
		
		if(id > 0) {
			cliente = clientService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "The client ID does not exist in the system!");
				return "redirect:/listar";
			}

		} else {
			flash.addFlashAttribute("error", "The client ID can not be zero!");
			return "redirect:/listar";
		}
		model.put("client", cliente);
		model.put("title", "Edit a Cliente");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String guardar(@Valid Client cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile photo,
						  RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			return "form";
		}
		
		if (!photo.isEmpty()) {
			
			if(cliente.getId() !=null 
					&& cliente.getId() > 0
					&& cliente.getPhoto()!=null
					&& cliente.getPhoto().length() > 0) {
				
				uploadFileService.delete(cliente.getPhoto());
			}
			
			String uniqueFileName = null;
				
			try {

				uniqueFileName = uploadFileService.copy(photo);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Photo loaded successfully '" + uniqueFileName + "'");

			cliente.setPhoto(uniqueFileName);;

		}
		
		String flashMessage = (cliente.getId() != null)? "Client has been edited successfully!" : "Client has been created successfully!";

		clientService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);

		return "redirect:listar";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash) {
		
		if(id > 0) {
			
			Client cliente = clientService.findOne(id);

			clientService.delete(id);
			flash.addFlashAttribute("success", "Client deleted successfully!");

			if (uploadFileService.delete(cliente.getPhoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getPhoto() + " eliminada con exito!");
			}


		}
		return "redirect:/listar";
	}
	
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		// SECOND APPROACH
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*
		 * FIRST APPROACH
		 * for(GrantedAuthority authority: authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info("Hi User ".concat(auth.getName()).concat(" your role is: ".concat(authority.getAuthority())));
				return true;
			}
		}
		
		return false;
		*/
		
	}

}
