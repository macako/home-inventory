package dev.macako.homeinventory.userservice;

import feign.Logger;
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
  Logger.Level feignLoggerLevel() {
    return BASIC;
  }
}
