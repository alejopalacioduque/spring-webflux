package com.reactor.academic.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorLogin {

	private String message;
	private Date timestamp;

}
