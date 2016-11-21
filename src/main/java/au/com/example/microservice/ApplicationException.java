package au.com.example.microservice;

/**
 * {@code InvalidLineItemException} is the superclass exception that is an unchecked exception all other
 * exceptions in the application should extend.
 *
 * @author Robert Leggett
 */
public abstract class ApplicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ApplicationException(final String message) {
    super(message);
  }
}
