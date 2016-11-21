package au.com.example.microservice.payslip.util;

import au.com.example.microservice.payslip.exception.InvalidLineItemException;
import au.com.example.microservice.payslip.model.Employee;
import au.com.example.microservice.payslip.model.Income;
import au.com.example.microservice.payslip.model.IncomeSummary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.Locale;
import java.util.function.Function;

/**
 * {@code IncomeSummaryMapper} is a support class that maps data from one object to another.
 *
 * @author Robert Leggett
 */
@Component
public class IncomeSummaryMapper {

  private static final Logger LOG = LoggerFactory.getLogger(IncomeSummaryMapper.class);

  private static final int NUMBER_OF_COLUMNS = 5;

  private static final int FIRST_NAME = 0;
  private static final int LAST_NAME = 1;
  private static final int MONTH = 4;
  private static final int GROSS = 2;
  private static final int SUPERANNUATION = 3;

  @Autowired
  private MessageSource messageSource;

  /**
   * Lambda aggregate operation that will map objects to another value as specified by a Function object.
   */
  public Function<String, IncomeSummary> mapToIncomeSummary = (line) -> {

    LOG.debug("line {}", line);

    String[] summary = line.split(",");

    if (NUMBER_OF_COLUMNS == summary.length) {
      final String firstName = summary[FIRST_NAME];
      final String lastName = summary[LAST_NAME];
      final String month = summary[MONTH];
      final String gross = summary[GROSS];
      final String superannuation = summary[SUPERANNUATION];

      if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)
              && StringUtils.isNotEmpty(month) && StringUtils.isNotEmpty(gross) && StringUtils.isNotEmpty(superannuation)) {

        final Employee employee = new Employee(firstName, lastName);
        final Income income = new Income(Integer.valueOf(gross), Integer.valueOf(superannuation));

        return new IncomeSummary(employee, Month.valueOf(month), income);
      }
    }

    throw new InvalidLineItemException(messageSource.getMessage("error.line.item.invalid", new String[]{line}, Locale.getDefault()));
  };
}
