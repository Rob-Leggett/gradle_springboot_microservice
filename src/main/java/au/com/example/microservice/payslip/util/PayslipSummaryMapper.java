package au.com.example.microservice.payslip.util;

import au.com.example.microservice.payslip.model.Employee;
import au.com.example.microservice.payslip.model.PayslipSummary;
import au.com.example.microservice.payslip.model.Salary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.function.Function;

/**
 * {@code PayslipSummaryMapper} is a support class that maps data from one object to another.
 *
 * @author Robert Leggett
 */
@Component
public class PayslipSummaryMapper {

  private static final Logger LOG = LoggerFactory.getLogger(PayslipSummaryMapper.class);

  /**
   * Lambda aggregate operation that will map objects to another value as specified by a Function object.
   */
  public Function<PayslipSummary, String> mapToString = (line) -> {

    LOG.debug("line {}", line.toString());

    final StringBuilder sb = new StringBuilder();

    final Employee employee = line.getEmployee();
    final Month month = line.getMonth();
    final Salary salary = line.getSalary();

    sb.append(employee.getFirstName());
    sb.append(",");
    sb.append(employee.getLastName());
    sb.append(",");
    sb.append(month.name());
    sb.append(",");
    sb.append(salary.getGross());
    sb.append(",");
    sb.append(salary.getTax());
    sb.append(",");
    sb.append(salary.getNet());
    sb.append(",");
    sb.append(salary.getSuperannuation());
    sb.append('\n');

    return sb.toString();
  };
}
