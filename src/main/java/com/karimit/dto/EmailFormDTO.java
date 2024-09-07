package com.karimit.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class EmailFormDTO {

	private String email;
	private boolean sendCheckbox;
	private String date;
	private String time;
}
