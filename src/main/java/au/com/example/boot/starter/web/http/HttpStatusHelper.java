package au.com.example.boot.starter.web.http;


import javaslang.control.Try;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * {@code HttpStatusHelper} is a support class that returns the status for a request.
 *
 * @author Robert Leggett
 */
public final class HttpStatusHelper {

  private HttpStatusHelper() {
  }

  public static HttpStatus getStatus(HttpServletRequest request) {
    return Try.of(() -> HttpStatus.valueOf((Integer) request.getAttribute("javax.servlet.error.status_code")))
            .getOrElse(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static HttpStatus getStatus(RequestAttributes requestAttributes) {
    return getStatus(((ServletRequestAttributes) requestAttributes).getRequest());
  }
}
