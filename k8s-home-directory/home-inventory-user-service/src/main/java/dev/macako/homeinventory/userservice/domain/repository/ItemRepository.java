package dev.macako.homeinventory.userservice.domain.repository;

import dev.macako.homeinventory.userservice.domain.model.Item;

import java.util.List;

public interface ItemRepository {
  List<Item> findByUserId(int id);

  void deleteItemsByUserId(int id);
}
