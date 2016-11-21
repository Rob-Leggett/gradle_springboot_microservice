package au.com.example.boot.starter.swagger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;

/**
 * {@code SwaggerAutoConfiguration} is an auto configuration that when either imported or activated
 * via enable auto configuration will enable swagger and populate api information loaded from application.yaml.
 *
 * @author Robert Leggett
 */
@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {

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

  @Value("${api.urls:.*}")
  private String apiUrls;

  @Bean
  public Docket restApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(any())
            .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title(apiTitle)
            .description(apiDescription)
            .termsOfServiceUrl(apiTerms)
            .contact(new Contact(apiContactName, apiContactUrl, apiContactEmail))
            .license(apiLicenseType)
            .licenseUrl(apiLicenseUrl)
            .version(apiVersion)
            .build();
  }
}
