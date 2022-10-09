package dev.macako.homeinventory.itemservice.domain.repository;

import dev.macako.homeinventory.itemservice.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
  List<Item> findByUserItemsId(Integer id);
  long deleteByUserItemsId(Integer id);
}
