package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.Request.LoginRequest;
import com.zstan.kafka.st.Request.RegisterRequest;
import com.zstan.kafka.st.Service.Impl.UserDetailServiceImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Регистрация нового пользователя.
     *
     * @param registerRequest JSON с username и password.
     * @return Успешная регистрация или ошибка.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();

        // Проверка на null или пустой пароль
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Пароль не может быть пустым");
        }
        System.out.println("Получен запрос на регистрацию: " + registerRequest.getUsername() + " " + registerRequest.getPassword());

        // Создание нового пользователя
        userDetailsService.createUser(
                registerRequest.getUsername(),
                passwordEncoder.encode(password), // Пароль шифруется
                "USER"
        );

        return ResponseEntity.ok("Пользователь зарегистрирован: " + registerRequest.getUsername());
    }

    /**
     * Логин пользователя.
     *
     * @param loginRequest JSON с username и password.
     * @return Успешный логин или ошибка.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Аутентификация пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Установка контекста безопасности
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Успешный вход: " + loginRequest.getUsername());
    }
}



