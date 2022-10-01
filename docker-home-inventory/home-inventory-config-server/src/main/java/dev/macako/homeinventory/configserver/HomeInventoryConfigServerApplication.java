package dev.macako.homeinventory.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class HomeInventoryConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(HomeInventoryConfigServerApplication.class, args);
  }
}
