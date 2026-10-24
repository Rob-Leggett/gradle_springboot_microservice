package au.com.example.boot.starter.web.http;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;
import java.util.Objects;

import static java.util.Arrays.stream;

/**
 * {@code ErrorCodeResolver} is a support class that resolves error codes and returns them.
 *
 * @author Robert Leggett
 */
public class ErrorCodeResolver {

  private static final Object[] EMPTY_ARRAY = new Object[0];

  private final MessageSource messageSource;

  public ErrorCodeResolver(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public String resolve(String[] codes) {
    return stream(codes)
            .map(this::resolveCode)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("CONSTRAINT_VIOLATION");
  }

  private String resolveCode(String code) {
    try {
      return messageSource.getMessage(code + ".code", EMPTY_ARRAY, Locale.ENGLISH);
    } catch (NoSuchMessageException e) {
      return null;
    }
  }
}
