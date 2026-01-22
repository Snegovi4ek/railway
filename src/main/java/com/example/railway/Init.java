package com.example.railway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.railway.model.entities.Role;
import com.example.railway.model.entities.User;
import com.example.railway.model.enums.UserRoles;
import com.example.railway.repository.UserRepository;
import com.example.railway.repository.UserRoleRepository;

import java.util.List;

@Slf4j
@Component
public class Init implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword = "12345";

    public Init(UserRepository userRepository,
                UserRoleRepository userRoleRepository,
                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        log.info("Init компонент инициализирован");
    }

    @Override
    public void run(String... args) {
        log.info("Запуск инициализации начальных данных");
        initRoles();
        initUsers();
        log.info("Инициализация завершена");
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            log.info("Создание базовых ролей...");
            userRoleRepository.saveAll(List.of(
                    new Role(UserRoles.ADMIN),
                    new Role(UserRoles.USER) // модератора нет
            ));
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            log.info("Создание пользователей по умолчанию...");

            var adminRole = userRoleRepository.findRoleByName(UserRoles.ADMIN).orElseThrow();
            var userRole = userRoleRepository.findRoleByName(UserRoles.USER).orElseThrow();

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setEmail("admin@railway.com");
            admin.setFullName("Admin Adminovich");
            admin.setRoles(List.of(adminRole));
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setEmail("user@railway.com");
            user.setFullName("User Userovich");
            user.setRoles(List.of(userRole));
            userRepository.save(user);
        }
    }
}
