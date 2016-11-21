package au.com.example.microservice;

import au.com.example.boot.starter.web.http.Response;
import au.com.example.boot.starter.web.http.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * {@code ApplicationExceptionHandler} handles common exceptions that may be thrown by application
 * MVC controllers , i.e. classes annotated with @Controller or @RestController.
 * This controller advice is set to lowest precedence to ensure that application specific exception handling is triggered, if
 * implemented.
 *
 * @author Robert Leggett
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@ResponseBody
public final class ApplicationExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

  private static final String SYSTEM_ERROR = "SYSTEM_ERROR";

  private static final String VALIDATION_ERROR = "VALIDATION_ERROR";

  @Autowired
  private ResponseBuilder responseBuilder;

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response handleValidationException(final ConstraintViolationException exc) {

    LOG.debug("exception: {}, detail: {}", VALIDATION_ERROR, exc.getConstraintViolations());

    return responseBuilder.buildResponse(exc.getConstraintViolations());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response handleMethodArgumentException(final MethodArgumentNotValidException exc) {

    LOG.debug("exception: {}, detail: {}", VALIDATION_ERROR, exc.getBindingResult());

    return responseBuilder.buildResponse(exc.getBindingResult());
  }

  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response handleApplicationException(final ApplicationException e) {
    String message = e.getMessage();

    LOG.error("exception: {}, detail: {}", SYSTEM_ERROR, message);
    LOG.error("Unknown error occurred: " + e);

    return responseBuilder.buildResponse(SYSTEM_ERROR, "Unknown error occured");
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response handleExceptions(final Throwable e) {
        /*
         * Handles all other un-handled exceptions including
         * "Resource access exceptions" which wrap the Socket timeouts & Unknown
         * hosts thrown when accessing downstream systems.
         */
    String message = e.getMessage();

    LOG.error("exception: {}, detail: {}", SYSTEM_ERROR, message);
    LOG.error("Unknown error occurred: " + e);

    return responseBuilder.buildResponse(SYSTEM_ERROR, "Unknown error occured");
  }

}
