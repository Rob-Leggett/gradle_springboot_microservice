package au.com.example.boot.starter.web.http;

import javaslang.control.Try;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static java.util.Arrays.stream;
import static javaslang.control.Try.success;

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
            .map(code -> Try.of(() -> messageSource.getMessage(code + ".code", EMPTY_ARRAY, Locale.ENGLISH)))
            .filter(Try::isSuccess)
            .findFirst()
            .orElse(success("CONSTRAINT_VIOLATION"))
            .get();
  }
}
