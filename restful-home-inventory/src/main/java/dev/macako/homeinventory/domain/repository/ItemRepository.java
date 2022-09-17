package dev.macako.homeinventory.domain.repository;

import dev.macako.homeinventory.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {}
