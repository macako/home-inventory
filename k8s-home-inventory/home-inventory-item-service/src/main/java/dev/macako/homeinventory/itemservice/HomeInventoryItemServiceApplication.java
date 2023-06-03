package dev.macako.homeinventory.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HomeInventoryItemServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(HomeInventoryItemServiceApplication.class, args);
  }
}
