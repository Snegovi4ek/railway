package com.example.railway.repository;

import com.example.railway.model.entities.Role;
import com.example.railway.model.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findRoleByName(UserRoles role);
}
