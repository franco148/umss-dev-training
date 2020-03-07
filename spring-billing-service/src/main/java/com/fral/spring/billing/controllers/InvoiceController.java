package com.fral.spring.billing.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fral.spring.billing.models.entity.Client;
import com.fral.spring.billing.models.entity.Invoice;
import com.fral.spring.billing.models.entity.InvoiceItem;
import com.fral.spring.billing.models.entity.Product;
import com.fral.spring.billing.services.ClientService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/invoices")
@SessionAttributes("invoice")
public class InvoiceController {

	@Autowired
	private ClientService clientService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Invoice factura = clientService.fetchInvoiceByIdWithClientWhithInvoiceItemsWithProduct(id); //clientService.findInvoiceById(id);

		if (factura == null) {
			flash.addFlashAttribute("error", "Invoice does not exist in the system!");
			return "redirect:/listar";
		}

		model.addAttribute("invoice", factura);
		model.addAttribute("title", "Invoice: ".concat(factura.getDescription()));
		return "invoice/ver";
	}

	@GetMapping("/form/{clientId}")
	public String crear(@PathVariable(value = "clientId") Long clientId, Map<String, Object> model,
						RedirectAttributes flash) {

		Client cliente = clientService.findOne(clientId);

		if (cliente == null) {
			flash.addFlashAttribute("error", "Client does not exist in the system.");
			return "redirect:/listar";
		}

		Invoice factura = new Invoice();
		factura.setClient(cliente);

		model.put("invoice", factura);
		model.put("title", "Create Invoice");

		return "invoice/form";
	}

	@GetMapping(value = "/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> cargarProductos(@PathVariable String term) {
		return clientService.findProductByName(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Invoice factura, BindingResult result, Model model,
			              @RequestParam(name = "item_id[]", required = false) Long[] itemId,
			              @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			              SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Create Invoice");
			return "invoice/form";
		}

		if (itemId == null || itemId.length == 0) {
			model.addAttribute("title", "Create Invoice");
			model.addAttribute("error", "Error: Invoice should have lines!");
			return "invoice/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Product producto = clientService.findProductoById(itemId[i]);

			InvoiceItem linea = new InvoiceItem();
			linea.setAmount(cantidad[i]);
			linea.setProduct(producto);
			factura.addItem(linea);

			log.info("ID: " + itemId[i].toString() + ", amount: " + cantidad[i].toString());
		}

		clientService.saveInvoice(factura);
		status.setComplete();

		flash.addFlashAttribute("success", "Invoice created successfully!");

		return "redirect:/ver/" + factura.getClient().getId();
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		Invoice factura = clientService.findInvoiceById(id);

		if (factura != null) {
			clientService.deleteInvoice(id);
			flash.addFlashAttribute("success", "Invoice removed successfully!");
			return "redirect:/ver/" + factura.getClient().getId();
		}
		flash.addFlashAttribute("error", "Invoice does not exist in the system, no items to delete!");

		return "redirect:/listar";
	}
}
