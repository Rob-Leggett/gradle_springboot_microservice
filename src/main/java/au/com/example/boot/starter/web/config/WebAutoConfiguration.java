package au.com.example.boot.starter.web.config;

import au.com.example.boot.starter.web.DefaultErrorAttributes;
import au.com.example.boot.starter.web.DefaultErrorController;
import au.com.example.boot.starter.web.DefaultExceptionHandler;
import au.com.example.boot.starter.web.http.ResponseBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Servlet;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for common,
 * web specific components, e.g. error handling.
 */
@AutoConfiguration(before = ErrorMvcAutoConfiguration.class)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication
@PropertySource({"classpath:properties/web-defaults.properties"})
public class WebAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
  public ErrorAttributes defaultErrorAttributes(ResponseBuilder responseBuilder) {
    return new DefaultErrorAttributes(responseBuilder);
  }

  @Bean
  public DefaultExceptionHandler defaultExceptionHandler() {
    return new DefaultExceptionHandler();
  }

  @Bean
  @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
  public DefaultErrorController defaultErrorController(ErrorAttributes errorAttributes) {
    return new DefaultErrorController(errorAttributes);
  }

  @Bean(name = "errorResponseBuilder")
  public ResponseBuilder getResponseBuilder(MessageSource messageSource) {
    return new ResponseBuilder(messageSource);
  }

  /**
   * Web MVC configuration providing the validator and message source used for error responses.
   */
  @Configuration
  public static class WebConfig implements WebMvcConfigurer {

    @Override
    @ConditionalOnMissingBean(value = SpringValidatorAdapter.class, search = SearchStrategy.CURRENT)
    public LocalValidatorFactoryBean getValidator() {
      LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
      localValidatorFactoryBean.setValidationMessageSource(getMessageSource());
      return localValidatorFactoryBean;
    }

    @Bean(name = "messageSource")
    @ConditionalOnMissingBean(value = AbstractMessageSource.class, search = SearchStrategy.CURRENT)
    public ResourceBundleMessageSource getMessageSource() {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasenames("properties", "properties/messages");
      messageSource.setDefaultEncoding("UTF-8");
      messageSource.setUseCodeAsDefaultMessage(false);
      return messageSource;
    }
  }

  @Bean
  @ConditionalOnMissingBean(value = MethodValidationPostProcessor.class, search = SearchStrategy.CURRENT)
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }
}
