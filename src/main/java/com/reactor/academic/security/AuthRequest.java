package com.reactor.academic.security;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AuthRequest {

	private String username;
	private String password;

}
