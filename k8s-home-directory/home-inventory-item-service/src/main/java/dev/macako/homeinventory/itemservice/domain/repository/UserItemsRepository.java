package dev.macako.homeinventory.itemservice.domain.repository;

import dev.macako.homeinventory.itemservice.domain.model.UserItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemsRepository extends JpaRepository<UserItems, Integer> {}
