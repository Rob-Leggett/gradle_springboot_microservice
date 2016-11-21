package au.com.example.microservice.payslip.exception;

import au.com.example.microservice.ApplicationException;

/**
 * {@code UnsupportedFileTypeException} is an unchecked exception that should be thrown
 * when an unsupported file type is detected.
 *
 * @author Robert Leggett
 */
public class UnsupportedFileTypeException extends ApplicationException {
  public UnsupportedFileTypeException(String message) {
    super(message);
  }
}
