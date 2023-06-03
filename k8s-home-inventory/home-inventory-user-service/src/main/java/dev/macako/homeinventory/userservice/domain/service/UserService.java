package dev.macako.homeinventory.userservice.domain.service;

import dev.macako.homeinventory.userservice.domain.model.Item;
import dev.macako.homeinventory.userservice.domain.model.User;
import dev.macako.homeinventory.userservice.domain.repository.ItemRepository;
import dev.macako.homeinventory.userservice.domain.repository.UserRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

  public UserService(UserRepository userRepository, ItemRepository itemRepository) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> findUserById(int id) {
    return userRepository.findById(id);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Retry(name = "default")
  public void deleteUserById(int id) {
    userRepository.deleteById(id);
  }

  public List<Item> findItemsByUserId(int id) {
    return itemRepository.findByUserId(id);
  }

  public List<User> hardcodedResponse(Exception ex) {
    return emptyList();
  }

  public void deleteItemsByUserId(int id) {
    itemRepository.deleteItemsByUserId(id);
  }
}
