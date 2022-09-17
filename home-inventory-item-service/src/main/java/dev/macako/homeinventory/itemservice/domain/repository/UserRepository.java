package dev.macako.homeinventory.itemservice.domain.repository;

import dev.macako.homeinventory.itemservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
