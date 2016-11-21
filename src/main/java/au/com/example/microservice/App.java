package au.com.example.microservice;

import au.com.example.boot.starter.swagger.config.SwaggerAutoConfiguration;
import au.com.example.boot.starter.web.config.WebAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

/**
 * {@code App} is to run a the spring boot application.
 *
 * @author Robert Leggett
 */
@SpringBootApplication
@Import(value = {WebAutoConfiguration.WebConfig.class, WebAutoConfiguration.class, SwaggerAutoConfiguration.class})
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
    return builder.sources(App.class);
  }

  public static void main(final String[] args) {
    SpringApplication.run(App.class, args);
  }
}
