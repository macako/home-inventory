package dev.macako.homeinventory.itemservice;

import feign.Logger.Level;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static feign.Logger.Level.BASIC;
import static feign.form.util.CharsetUtil.UTF_8;

@Configuration
public class FeignConfiguration {
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor("username", "password", UTF_8);
  }

  @Bean
  Level feignLoggerLevel() {
    return BASIC;
  }
}
