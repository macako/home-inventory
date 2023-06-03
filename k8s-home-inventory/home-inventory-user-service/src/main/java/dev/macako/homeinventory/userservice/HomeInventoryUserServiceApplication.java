package dev.macako.homeinventory.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class HomeInventoryUserServiceApplication {

  public static void main(String[] args) {
    log.info("HOME INVENTORY USER SERVICE STARTING");
    SpringApplication.run(HomeInventoryUserServiceApplication.class, args);
    log.info("HOME INVENTORY USER SERVICE STARTED");
  }
}
