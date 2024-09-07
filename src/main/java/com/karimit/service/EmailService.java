package com.karimit.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.karimit.dto.EmailFormDTO;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	static Map<String, String> response = new HashMap<>();

	public Map<String, String> sendEmail(EmailFormDTO emailFormData) {
		response.clear(); // Clear previous responses

		// Validate email and other fields
		if (emailFormData.getEmail() == null || emailFormData.getEmail().isEmpty()) {
			response.put("error", "Email address is required.");
		} else if (!emailFormData.getEmail().contains("@")) {
			response.put("error", "Invalid email address.");
		} else if (!emailFormData.isSendCheckbox()) {
			response.put("error", "Please select the Check Box.");
		} else if (emailFormData.getDate() == null || emailFormData.getDate().isEmpty()) {
			response.put("error", "Please select the Date.");
		} else if (emailFormData.getTime() == null || emailFormData.getTime().isEmpty()) {
			response.put("error", "Please select the Time.");
		}

		if (response.isEmpty()) {
			// Immediate response for successful scheduling
			response.put("success", "Email scheduled successfully!");

			// Schedule the email to be sent at the specified date and time
			scheduleEmail(emailFormData);
		}
		return response;
	}

	private void scheduleEmail(EmailFormDTO emailFormData) {
		Timer timer = new Timer();

		// Convert date and time to Date object
		String dateTimeString = emailFormData.getDate() + " " + emailFormData.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date scheduledDateTime;

		try {
			scheduledDateTime = dateFormat.parse(dateTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
			// Handle parsing error
			response.put("error", "Error parsing date and time.");
			return;
		}

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Logic to send email
				sendActualEmail(emailFormData.getEmail());
			}
		}, scheduledDateTime);
	}

	private void sendActualEmail(String email) {
		// Create a SimpleMailMessage
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("Scheduled Email");
		mailMessage.setText("This is a test email sent at the scheduled time.");

		// Send the email
		try {
			mailSender.send(mailMessage);
			System.out.println("Email sent to: " + email);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle email sending error
			response.put("error", "Failed to send email.");
		}
	}
}