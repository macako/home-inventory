package dev.macako.homeinventory.namingserver.homeinventorynamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class HomeInventoryNamingServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(HomeInventoryNamingServerApplication.class, args);
  }
}
