package com.example.otzivi.services;

import com.example.otzivi.models.User;
import com.example.otzivi.models.enums.Role;
import com.example.otzivi.repositories.ProductRepository;
import com.example.otzivi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ProductService productService;

    public Long createUser(User user){
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return (long) -1;

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setFavourites(new ArrayList<>());
        // TODO: Перед защитой не забудь сделать по человечески
        user.setActive(false);
        user.getRoles().add(Role.ROLE_ADMIN);
        user.setConfirmToken(emailService.sendConfirmMessage(user.getEmail(),userRepository.save(user).getId()));
        log.info("Saving new User with email: {}", email);
        return userRepository.save(user).getId();
    }
    public boolean tryRecoverPass(String email){
        User user = userRepository.findByEmail(email);
        if (user == null)
            return false;
        user.setConfirmToken(emailService.sendRecoveryMessage(user.getEmail(),userRepository.save(user).getId()));
        return true;
    }
    public boolean recoveryPass(Long id, String code, String password)
    {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || user.getConfirmToken().equals(""))
            return false;
        if (user.getConfirmToken().equals(code)) {
            user.setPassword(passwordEncoder.encode(password));
            user.setConfirmToken("");
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public boolean confirmUser(Long id, String code)
    {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || user.getConfirmToken().equals(""))
            return false;
        if (user.getConfirmToken().equals(code)) {
            user.setActive(true);
            user.setConfirmToken("");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> list(){
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }
    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }
}
