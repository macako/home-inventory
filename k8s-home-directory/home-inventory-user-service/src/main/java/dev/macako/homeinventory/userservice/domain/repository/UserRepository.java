package dev.macako.homeinventory.userservice.domain.repository;


import dev.macako.homeinventory.userservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
