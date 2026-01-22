package com.example.railway.services;

import com.example.railway.dto.UserRegistrationDto;
import com.example.railway.model.entities.User;

public interface AuthService {
    void register(UserRegistrationDto registrationDTO);

    User getUser(String username);
}
