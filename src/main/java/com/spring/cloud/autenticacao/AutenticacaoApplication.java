package com.spring.cloud.autenticacao;

import com.spring.cloud.autenticacao.entity.Permission;
import com.spring.cloud.autenticacao.entity.User;
import com.spring.cloud.autenticacao.repository.PermissionRepository;
import com.spring.cloud.autenticacao.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class AutenticacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutenticacaoApplication.class, args);
	}


	CommandLineRunner init(UserRepository userRepository, PermissionRepository permissionRepository, BCryptPasswordEncoder passwordEncoder){
		return args -> initUsers(userRepository, permissionRepository, passwordEncoder);
	}

	private void initUsers(UserRepository userRepository, PermissionRepository permissionRepository, BCryptPasswordEncoder passwordEncoder){

		Permission permission = null;
		Permission foundPermission = permissionRepository.findByDescription("Admin");

		if(foundPermission == null){
			permission = new Permission();
			permission.setDescription("Admin");
			permission = permissionRepository.save(permission);
		}else {
			permission = foundPermission;
		}

		User user = new User();
		user.setUsername("felipevianna86");
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode("123456"));
		user.setPermissions(Arrays.asList(permission));

		User userFound = userRepository.findByUsername(user.getUsername());
		if(userFound != null){
			userRepository.save(user);
		}
	}

}
