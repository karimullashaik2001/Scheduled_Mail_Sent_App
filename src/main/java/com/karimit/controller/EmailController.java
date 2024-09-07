package com.karimit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.karimit.dto.EmailFormDTO;
import com.karimit.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendEmail")
	public ResponseEntity<Map<String, String>> handleFormData(@RequestBody EmailFormDTO emailFormData) {
		// Handle form data
		Map<String, String> response = emailService.sendEmail(emailFormData);
		return ResponseEntity.ok(response);
	}
}
