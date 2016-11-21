package au.com.example.boot.starter.web.http;

import org.springframework.validation.MessageCodesResolver;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstraintViolationToErrorFunction implements Function<ConstraintViolation<?>, Error> {

  private static final Pattern ORDER = Pattern.compile("(\\d+)$");

  private final UUID id;

  private final ErrorCodeResolver errorCodeResolver;

  private final MessageCodesResolver messageCodesResolver;

  public ConstraintViolationToErrorFunction(UUID id, ErrorCodeResolver errorCodeResolver, MessageCodesResolver messageCodesResolver) {
    this.id = id;
    this.errorCodeResolver = errorCodeResolver;
    this.messageCodesResolver = messageCodesResolver;
  }

  @Override
  public Error apply(ConstraintViolation<?> violation) {
    String violationName = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
    String fullParamName = violation.getPropertyPath().toString();

    String[] codes = messageCodesResolver.resolveMessageCodes(violationName, null, fullParamName, null);
    String code = errorCodeResolver.resolve(codes);
    String name = deriveParamName(violation, fullParamName);

    return new Error(id, code, violation.getMessage()).withParameter(name);
  }

  private String extractParamName(String fullParamName) {
    int index = fullParamName.lastIndexOf('.');
    return index == -1 ? fullParamName : fullParamName.substring(index + 1);
  }

  private String extractMethodName(String fullParamName) {
    int index = fullParamName.lastIndexOf('.');
    return index == -1 ? fullParamName : fullParamName.substring(0, index);
  }

  /**
   * Derive param name via reflection. The violation object contains an instance of object, invoked method name and position
   * of param that failed validation. We iterate over all object methods to match method name and number of arguments.
   * Even if flag -parameters is set the real method arguments names are not available at this point (all arguments have generic
   * names like 'arg0', 'arg1')
   * Probably the further investigation could be done to make them available.
   */
  private String deriveParamName(ConstraintViolation<?> violation, String fullParamName) {
    try {
      int order = extractParamOrder(fullParamName);
      String methodName = extractMethodName(fullParamName);
      Method method = Arrays.stream(violation.getLeafBean().getClass().getMethods()).filter(m ->
              m.getName().equals(methodName) && m.getParameterCount() > order).findFirst().get();

      return method.getParameters()[order].getName();
    } catch (Exception e) {
      return extractParamName(fullParamName);
    }
  }

  private int extractParamOrder(String fullParamName) {
    Matcher matcher = ORDER.matcher(fullParamName);
    return matcher.find() ? Integer.parseInt(matcher.group()) : -1;
  }
}
