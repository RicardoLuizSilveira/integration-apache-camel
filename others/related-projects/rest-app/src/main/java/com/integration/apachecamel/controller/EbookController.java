package com.integration.apachecamel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webservices/ebook")
public class EbookController {
	
	@GetMapping("/item")
	public ResponseEntity<String> find(@RequestParam(value = "ebookId", required = false) String ebookId,
									   @RequestParam(value = "pedidoId", required = false) Long pedidoId,
									   @RequestParam(value = "clienteId", required = false) String clienteId) {
		System.out.println("ebookId: " + ebookId +" - pedidoId: " + pedidoId + " - clienteId: " + clienteId);
		return ResponseEntity.ok("it's working!");
	}
	
	@PostMapping("/item")
	public ResponseEntity<String> post(@RequestBody String msg) {
		System.out.println(msg);
		return ResponseEntity.ok().build();
	}
}
