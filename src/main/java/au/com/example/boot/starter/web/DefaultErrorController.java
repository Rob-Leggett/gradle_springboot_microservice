package au.com.example.boot.starter.web;

import au.com.example.boot.starter.web.http.HttpStatusHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Default global error {@link RestController} controller, rendering {@link ErrorAttributes}. This class is mostly a copy of
 * Spring's {@link org.springframework.boot.autoconfigure.web.BasicErrorController} but it ensures that the responses
 * are only JSON (i.e. no redirects to HTML pages, etc).
 * <p>
 * Developers can provide their own error controllers instead of this one by simply implementing the {@link ErrorController}
 * interface.
 */
@RestController
public class DefaultErrorController implements ErrorController {

  @Value("${error.path:/error}")
  private String errorPath;

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
    RequestAttributes requestAttributes = new ServletRequestAttributes(request);
    return this.errorAttributes.getErrorAttributes(requestAttributes, false);
  }

  @Override
  public String getErrorPath() {
    return errorPath;
  }

}
