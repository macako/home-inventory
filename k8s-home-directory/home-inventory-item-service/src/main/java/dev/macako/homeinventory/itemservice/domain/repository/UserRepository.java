package dev.macako.homeinventory.itemservice.domain.repository;

import dev.macako.homeinventory.itemservice.domain.model.User;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findById(int id);
}
