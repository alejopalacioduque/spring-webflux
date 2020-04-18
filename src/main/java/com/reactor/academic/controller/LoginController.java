package com.reactor.academic.controller;

import java.util.Date;

import com.reactor.academic.security.AuthRequest;
import com.reactor.academic.security.AuthResponse;
import com.reactor.academic.security.ErrorLogin;
import com.reactor.academic.security.JWTUtil;
import com.reactor.academic.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class LoginController {

    private JWTUtil jwtUtil;

    private IUserService service;

    @Autowired
    public LoginController(JWTUtil jwtUtil, IUserService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return service.findByUser(ar.getUsername()).map((userDetails) -> {
            String token = jwtUtil.generateToken(userDetails);
            Date expiracion = jwtUtil.getExpirationDateFromToken(token);

            if (BCrypt.checkpw(ar.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(token, expiracion));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorLogin("wrong credentials", new Date()));
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/v2/login")
    public Mono<ResponseEntity<?>> login(@RequestHeader("usuario") String usuario, @RequestHeader("clave") String clave) {

        return service.findByUser(usuario).map((userDetails) -> {

            String token = jwtUtil.generateToken(userDetails);
            Date expiracion = jwtUtil.getExpirationDateFromToken(token);

            if (BCrypt.checkpw(clave, userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(token, expiracion));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorLogin("wrong credentials", new Date()));
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
