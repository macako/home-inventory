package dev.macako.homeinventory.itemservice.infrastructure.repository;

import dev.macako.homeinventory.itemservice.domain.model.User;
import dev.macako.homeinventory.itemservice.domain.repository.UserRepository;
import dev.macako.homeinventory.itemservice.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
    name = "home-inventory-user-service",
    url = "${http.homeInventoryUserService.host}",
    configuration = FeignConfiguration.class)
public interface UserFeignClientRepository extends UserRepository {

  @GetMapping("/users/{id}")
  public Optional<User> findById(@PathVariable int id);
}
