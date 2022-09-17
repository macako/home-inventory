package dev.macako.homeinventory.domain.service;

import dev.macako.homeinventory.domain.model.Item;
import dev.macako.homeinventory.domain.model.User;
import dev.macako.homeinventory.domain.repository.ItemRepository;
import dev.macako.homeinventory.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserItemsService {
  private final UserRepository userRepository;

  private final ItemRepository itemRepository;

  public UserItemsService(UserRepository userRepository, ItemRepository itemRepository) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> findUserById(int id) {
    return userRepository.findById(id);
  }

  public void deleteUserById(int id) {
    userRepository.deleteById(id);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public Item saveItem(User user, Item item) {
    item.setUser(user);

    return itemRepository.save(item);
  }
}
