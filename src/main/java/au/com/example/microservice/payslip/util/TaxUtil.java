package au.com.example.microservice.payslip.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.List;
import java.util.Optional;

/**
 * {@code TaxUtil} is a support class that retrieves the correct tax bracket/s and returns them.
 *
 * @author Robert Leggett
 */
@Component
public class TaxUtil {

  @Autowired
  private TaxRatesConfiguration taxRatesConfiguration;

  public List<TaxRatesConfiguration.Bracket> getTaxBrackets(final Month month) {
    return isPreviousTaxYear(month) ? taxRatesConfiguration.getPrevious() : taxRatesConfiguration.getCurrent();
  }

  public TaxRatesConfiguration.Bracket determineTaxBracket(final Integer income, final List<TaxRatesConfiguration.Bracket> brackets) {

    Optional<TaxRatesConfiguration.Bracket> result =
            brackets.stream().filter(bracket -> (income >= bracket.getMin() && (bracket.getMax() == null || income <= bracket.getMax()))).findFirst();

    return result.isPresent() ? result.get() : null;
  }

  private boolean isPreviousTaxYear(final Month month) {
    return (month.getValue() < Month.JULY.getValue());
  }
}
