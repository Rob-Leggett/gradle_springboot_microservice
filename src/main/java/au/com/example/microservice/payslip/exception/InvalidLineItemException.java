package au.com.example.microservice.payslip.exception;

import au.com.example.microservice.ApplicationException;

/**
 * {@code InvalidLineItemException} is an unchecked exception that should be thrown
 * when an invalid line item is detected.
 *
 * @author Robert Leggett
 */
public class InvalidLineItemException extends ApplicationException {
  public InvalidLineItemException(String message) {
    super(message);
  }
}
