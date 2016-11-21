package au.com.example.boot.starter.web.http;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

/**
 * {@code ResponseBuilder} is a support class that builds the responses that are returned to the consumer
 * for all error scenarios.
 *
 * @author Robert Leggett
 */
public class ResponseBuilder {

  private final ErrorCodeResolver errorCodeResolver;

  private final DefaultMessageCodesResolver messageCodesResolver;

  public ResponseBuilder(MessageSource messageSource) {
    this.errorCodeResolver = new ErrorCodeResolver(messageSource);
    this.messageCodesResolver = new DefaultMessageCodesResolver();
  }

  public Response buildResponse(BindingResult result) {
    return buildResponse(UUID.randomUUID(), result);
  }

  public Response buildResponse(UUID id, BindingResult result) {
    Stream<Error> objectErrorStream = result.getGlobalErrors().stream()
            .map(error -> new Error(id, errorCodeResolver.resolve(error.getCodes()), error.getDefaultMessage()));

    Stream<Error> fieldErrorStream = result.getFieldErrors().stream()
            .map(it -> new Error(id, errorCodeResolver.resolve(it.getCodes()), it.getDefaultMessage()).withPointer(it.getField()));

    List<Error> allErrors = concat(objectErrorStream, fieldErrorStream).collect(toList());

    return buildResponse(allErrors);
  }

  public Response buildResponse(String code, String detail) {
    return buildResponse(new Error(code, detail));
  }

  public Response buildResponse(Error... errors) {
    return buildResponse(asList(errors));
  }

  public Response buildResponse(List<Error> errors) {
    return new Response(errors);
  }

  public Response buildResponse(Set<ConstraintViolation<?>> violations) {
    return buildResponse(UUID.randomUUID(), violations);
  }

  public Response buildResponse(UUID id, Set<ConstraintViolation<?>> violations) {
    final ConstraintViolationToErrorFunction function = new ConstraintViolationToErrorFunction(
            id,
            errorCodeResolver,
            messageCodesResolver);

    return buildResponse(violations.stream().map(function).collect(toList()));
  }
}
