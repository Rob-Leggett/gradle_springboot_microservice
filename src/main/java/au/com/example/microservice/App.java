package au.com.example.microservice;

import au.com.example.boot.starter.openapi.config.OpenApiAutoConfiguration;
import au.com.example.boot.starter.web.config.WebAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

/**
 * {@code App} is to run a the spring boot application.
 *
 * @author Robert Leggett
 */
@SpringBootApplication
@Import(value = {WebAutoConfiguration.WebConfig.class, WebAutoConfiguration.class, OpenApiAutoConfiguration.class})
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
    return builder.sources(App.class);
  }

  public static void main(final String[] args) {
    SpringApplication.run(App.class, args);
  }
}
