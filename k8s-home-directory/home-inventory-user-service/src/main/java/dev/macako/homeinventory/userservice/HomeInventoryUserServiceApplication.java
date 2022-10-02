package dev.macako.homeinventory.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HomeInventoryUserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(HomeInventoryUserServiceApplication.class, args);
  }
}
