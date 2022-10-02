package dev.macako.homeinventory.itemservice.domain.service;

import dev.macako.homeinventory.itemservice.domain.model.Item;
import dev.macako.homeinventory.itemservice.domain.model.User;
import dev.macako.homeinventory.itemservice.domain.repository.ItemRepository;
import dev.macako.homeinventory.itemservice.domain.repository.UserRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ItemService {
  private final UserRepository userRepository;

  private final ItemRepository itemRepository;

  public ItemService(UserRepository userRepository, ItemRepository itemRepository) {
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
  }

  public Optional<User> findUserById(int id) {
    return userRepository.findById(id);
  }

  @Retry(name = "default")
  public List<Item> findItemsByUserId(int id) {
    return itemRepository.findByUserId(id);
  }

  @Retry(name = "default", fallbackMethod = "hardcodedResponse")
  public List<Item> findAllItems() {
    return itemRepository.findAll();
  }

  @Retry(name = "default")
  public Item saveItem(User user, Item item) {
    item.setUser(user);

    return itemRepository.save(item);
  }

  public List<Item> hardcodedResponse(Exception ex) {
    return emptyList();
  }
}
