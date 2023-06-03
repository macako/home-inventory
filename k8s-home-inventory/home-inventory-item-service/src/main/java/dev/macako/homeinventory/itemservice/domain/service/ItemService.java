package dev.macako.homeinventory.itemservice.domain.service;

import dev.macako.homeinventory.itemservice.domain.model.Item;
import dev.macako.homeinventory.itemservice.domain.model.UserItems;
import dev.macako.homeinventory.itemservice.domain.repository.ItemRepository;
import dev.macako.homeinventory.itemservice.domain.repository.UserItemsRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ItemService {
  private final UserItemsRepository userItemsRepository;
  private final ItemRepository itemRepository;

  public ItemService(UserItemsRepository userItemsRepository, ItemRepository itemRepository) {
    this.userItemsRepository = userItemsRepository;
    this.itemRepository = itemRepository;
  }

  public Optional<Item> findItemById(int id) {
    return itemRepository.findById(id);
  }

  public Optional<UserItems> findUserById(int id) {
    return userItemsRepository.findById(id);
  }

  @Retry(name = "default")
  public List<Item> findItemsByUserId(int id) {
    return itemRepository.findByUserItemsId(id);
  }

  @Retry(name = "default", fallbackMethod = "hardcodedResponse")
  public List<Item> findAllItems() {
    return itemRepository.findAll();
  }

  @Retry(name = "default")
  public Item saveItem(UserItems userItems, Item item) {
    item.setUserItems(userItems);
    return itemRepository.save(item);
  }

  public List<Item> hardcodedResponse(Exception ex) {
    return emptyList();
  }

  public void deleteItemById(int id) {
    itemRepository.deleteById(id);
  }

  public void deleteAllItemsByUserId(int id) {
    final Optional<UserItems> userItems = userItemsRepository.findById(id);
    userItemsRepository.delete(userItems.get());
  }
}
