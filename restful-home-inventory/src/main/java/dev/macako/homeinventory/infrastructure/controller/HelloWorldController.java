package dev.macako.homeinventory.infrastructure.controller;

import dev.macako.homeinventory.infrastructure.model.HelloWorldBean;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static java.lang.String.format;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@RestController
public class HelloWorldController {

  private final MessageSource messageSource;

  public HelloWorldController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping(path = "/hello-world")
  public String helloWorld() {
    return "Hello World";
  }

  @GetMapping(path = "/hello-world-bean")
  public HelloWorldBean helloWorldBean() {
    return new HelloWorldBean("Hello World");
  }

  @GetMapping(path = "/hello-world/path-variable/{name}")
  public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
    return new HelloWorldBean(format("Hello World, %s", name));
  }

  @GetMapping(path = "/hello-world-internationalized")
  public String helloWorldInternationalized() {
    Locale locale = getLocale();
    return messageSource.getMessage("good.morning.message", null, "Default Message", locale);
  }
}
