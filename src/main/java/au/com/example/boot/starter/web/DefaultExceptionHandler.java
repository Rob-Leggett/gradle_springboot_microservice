package au.com.example.boot.starter.web;

import au.com.example.boot.starter.web.http.Response;
import au.com.example.boot.starter.web.http.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Locale;

/**
 * <p>
 * Handle common exceptions that may be thrown by application MVC controllers , i.e. classes annotated with @Controller or @RestController.
 * This controller advice is set to lowest precedence to ensure that application specific exception handling is triggered, if
 * implemented.
 * </p>
 * The order of exception handling execution is as follows:
 * <ul>
 * <li>Exception handlers defined in the controller</li>
 * <li>Exception handlers defined in the @ControllerAdvice annotated class within the application
 * (provided order precedence is higher than this class)</li>
 * <li>Exception handlers defined in this class</li>
 * <li>{@link org.springframework.boot.autoconfigure.web.BasicErrorController} handles anything that is not handled by the above</li>
 * </ul>
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

  @Autowired
  private ResponseBuilder responseBuilder;

  @Autowired
  private MessageSource messageSource;

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UnsatisfiedServletRequestParameterException.class, MissingServletRequestParameterException.class,
          HttpMessageNotReadableException.class, ServletRequestBindingException.class, MethodArgumentTypeMismatchException.class})
  public Response handleException(Exception e) {
    LOG.info("handle client exception: {}", e.getMessage());
    return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST.name(), e.getMessage());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response handleException(MethodArgumentNotValidException e) {
    LOG.info("handle validation exception: {}", e.getMessage());
    return responseBuilder.buildResponse(e.getBindingResult());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public Response handleException(ConstraintViolationException e) {
    LOG.info("handle validation exception: {}", e.getMessage());
    return responseBuilder.buildResponse(e.getConstraintViolations());
  }

  @ResponseBody
  @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public Response handleUnSupportedMediaType(HttpMediaTypeNotSupportedException e) {
    LOG.info("handle client exception: {}", e.getMessage());
    return responseBuilder.buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name(),
            messageSource.getMessage("messages.error.unsupportedMediaTypeMessage", null, Locale.getDefault()));
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Response handleNonSupportedRequestMethod(HttpRequestMethodNotSupportedException e) {
    return responseBuilder.buildResponse(HttpStatus.METHOD_NOT_ALLOWED.name(), e.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public Response handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
    LOG.info("handle client exception: {}", e.getMessage());
    return responseBuilder.buildResponse(HttpStatus.NOT_ACCEPTABLE.name(),
            messageSource.getMessage("messages.error.unsupportedMediaTypeMessage", null, Locale.getDefault()));
  }


  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Response> handleException(Throwable t) {
    ResponseStatus responseStatus = t.getClass().getAnnotation(ResponseStatus.class);
    return responseStatus != null ? handleExpectedException(responseStatus, t) : handleUnexpectedException(t);
  }

  private ResponseEntity<Response> handleExpectedException(ResponseStatus responseStatus, Throwable t) {
    boolean hasReason = StringUtils.hasText(responseStatus.reason());
    String reason = hasReason ? responseStatus.reason() : t.getMessage();
    LOG.info("handle expected application exception: {}", reason);
    return new ResponseEntity<Response>(responseBuilder.buildResponse(responseStatus.value().name(), reason),
            responseStatus.value());
  }

  private ResponseEntity<Response> handleUnexpectedException(Throwable t) {
    LOG.error("handle unexpected exception:", t);
    return new ResponseEntity<>(
            this.responseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                    messageSource.getMessage("messages.error.processingErrorMessage", null, Locale.getDefault())),
            HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
