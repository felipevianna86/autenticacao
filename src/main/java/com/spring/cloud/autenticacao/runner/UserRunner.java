package com.spring.cloud.autenticacao.runner;

import com.spring.cloud.autenticacao.entity.Permission;
import com.spring.cloud.autenticacao.entity.User;
import com.spring.cloud.autenticacao.repository.PermissionRepository;
import com.spring.cloud.autenticacao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserRunner implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

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
        if(userFound == null){
            userRepository.save(user);
        }
    }
}
