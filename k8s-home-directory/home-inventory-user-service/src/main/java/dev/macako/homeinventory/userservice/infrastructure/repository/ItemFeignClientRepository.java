package dev.macako.homeinventory.userservice.infrastructure.repository;

import dev.macako.homeinventory.userservice.FeignConfiguration;
import dev.macako.homeinventory.userservice.domain.model.Item;
import dev.macako.homeinventory.userservice.domain.repository.ItemRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "home-inventory-item-service",
    url = "${http.homeInventoryItemService.host}",
    configuration = FeignConfiguration.class)
public interface ItemFeignClientRepository extends ItemRepository {

  @GetMapping("/items/user/{id}")
  List<Item> findByUserId(@PathVariable int id);

  @DeleteMapping("/items/user/{id}")
  void deleteItemsByUserId(@PathVariable int id);
}
