package au.com.example.boot.starter.web.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@code HttpStatusHelper} is a support class that returns the status for a request.
 *
 * @author Robert Leggett
 */
public final class HttpStatusHelper {

  private HttpStatusHelper() {
  }

  public static HttpStatus getStatus(HttpServletRequest request) {
    if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) instanceof Integer statusCode) {
      HttpStatus status = HttpStatus.resolve(statusCode);
      if (status != null) {
        return status;
      }
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public static HttpStatus getStatus(RequestAttributes requestAttributes) {
    return getStatus(((ServletRequestAttributes) requestAttributes).getRequest());
  }
}
