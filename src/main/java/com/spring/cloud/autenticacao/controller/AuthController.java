package com.spring.cloud.autenticacao.controller;

import com.spring.cloud.autenticacao.jwt.JwtTokenProvider;
import com.spring.cloud.autenticacao.repository.UserRepository;
import com.spring.cloud.autenticacao.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"} ,
    consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> login(@RequestBody UserVO userVO){

        try {

            var username = userVO.getUsername();
            var password = userVO.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

            var user = userRepository.findByUsername(username);

            var token = "";

            if(user != null){
                token = jwtTokenProvider.createToken(username, user.getRoles());
            }else {
                throw new UsernameNotFoundException("User not found");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);

            return ResponseEntity.ok(model);

        }catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @RequestMapping("/testeSecurity")
    public String teste(){
        return "testado";
    }

}
