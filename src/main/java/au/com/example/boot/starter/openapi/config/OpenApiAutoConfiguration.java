package au.com.example.boot.starter.openapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code OpenApiAutoConfiguration} is an auto configuration that when either imported or activated
 * via enable auto configuration will populate the OpenAPI (springdoc) api information loaded from application.yaml.
 *
 * @author Robert Leggett
 */
@Configuration
public class OpenApiAutoConfiguration {

  @Value("${api.title:}")
  private String apiTitle;

  @Value("${api.description:}")
  private String apiDescription;

  @Value("${api.terms:}")
  private String apiTerms;

  @Value("${api.contact.name:}")
  private String apiContactName;

  @Value("${api.contact.url:}")
  private String apiContactUrl;

  @Value("${api.contact.email:}")
  private String apiContactEmail;

  @Value("${api.license.type:}")
  private String apiLicenseType;

  @Value("${api.license.url:}")
  private String apiLicenseUrl;

  @Value("${api.version:1.0.0}")
  private String apiVersion;

  @Bean
  public OpenAPI restApi() {
    return new OpenAPI().info(new Info()
            .title(apiTitle)
            .description(apiDescription)
            .termsOfService(emptyToNull(apiTerms))
            .contact(new Contact().name(emptyToNull(apiContactName)).url(emptyToNull(apiContactUrl)).email(emptyToNull(apiContactEmail)))
            .license(apiLicenseType.isEmpty() ? null : new License().name(apiLicenseType).url(emptyToNull(apiLicenseUrl)))
            .version(apiVersion));
  }

  private static String emptyToNull(String value) {
    return value == null || value.isEmpty() ? null : value;
  }
}
