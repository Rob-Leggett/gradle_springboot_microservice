package au.com.example.boot.starter.web;

import au.com.example.boot.starter.web.http.HttpStatusHelper;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Default global error {@link RestController} controller, rendering {@link ErrorAttributes}. This class is mostly a copy of
 * Spring's {@link org.springframework.boot.webmvc.autoconfigure.error.BasicErrorController} but it ensures that the responses
 * are only JSON (i.e. no redirects to HTML pages, etc).
 * <p>
 * Developers can provide their own error controllers instead of this one by simply implementing the {@link ErrorController}
 * interface.
 */
@RestController
public class DefaultErrorController implements ErrorController {

  private ErrorAttributes errorAttributes;

  public DefaultErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  @RequestMapping(value = "${error.path:/error}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> onError(HttpServletRequest request) {
    Map<String, Object> body = getErrorAttributes(request);
    HttpStatus status = HttpStatusHelper.getStatus(request);
    return new ResponseEntity<>(body, status);
  }

  private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
    return this.errorAttributes.getErrorAttributes(new ServletWebRequest(request), ErrorAttributeOptions.defaults());
  }

}
