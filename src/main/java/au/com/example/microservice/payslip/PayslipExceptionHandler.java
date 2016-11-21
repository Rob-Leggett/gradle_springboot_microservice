package au.com.example.microservice.payslip;

import au.com.example.boot.starter.web.http.Response;
import au.com.example.boot.starter.web.http.ResponseBuilder;
import au.com.example.microservice.payslip.exception.InvalidLineItemException;
import au.com.example.microservice.payslip.exception.UnsupportedFileTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {@code PayslipExceptionHandler} handles payslip exceptions that may be thrown by application
 * MVC controllers , i.e. classes annotated with @Controller or @RestController.
 * This controller advice is set to highest precedence to ensure that application specific exception handling is triggered, if
 * implemented.
 *
 * @author Robert Leggett
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
public final class PayslipExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(PayslipExceptionHandler.class);

  private static final String VALIDATION_ERROR = "VALIDATION_ERROR";

  @Autowired
  private ResponseBuilder responseBuilder;

  @ExceptionHandler(UnsupportedFileTypeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response handleUnsupportedFileTypeException(final UnsupportedFileTypeException exc) {

    LOG.debug("exception: {}, detail: {}", VALIDATION_ERROR, exc.getMessage());

    return responseBuilder.buildResponse(VALIDATION_ERROR, exc.getMessage());
  }

  @ExceptionHandler(InvalidLineItemException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response handleInvalidLineItemException(final InvalidLineItemException exc) {

    LOG.debug("exception: {}, detail: {}", VALIDATION_ERROR, exc.getMessage());

    return responseBuilder.buildResponse(VALIDATION_ERROR, exc.getMessage());
  }
}
