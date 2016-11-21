package au.com.example.microservice.payslip;

import au.com.example.microservice.payslip.model.Employee;
import au.com.example.microservice.payslip.model.Income;
import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummary;
import au.com.example.microservice.payslip.model.Salary;
import au.com.example.microservice.payslip.util.SalaryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;

@Service
public class PayslipServiceImpl implements PayslipService {

  private static final Logger LOG = LoggerFactory.getLogger(PayslipServiceImpl.class);

  @Autowired
  private SalaryUtil salaryUtil;

  @Override
  public PayslipSummary calculate(final IncomeSummary incomeSummary) {

    LOG.debug("IncomeSummary {}", incomeSummary.toString());

    final Employee employee = incomeSummary.getEmployee();
    final Month month = incomeSummary.getMonth();
    final Income income = incomeSummary.getIncome();

    final Integer gross = salaryUtil.calculateMonthlyGrossIncome(income.getGross());
    final Integer tax = salaryUtil.calculateMonthlyIncomeTax(income.getGross(), month);
    final Integer net = salaryUtil.calculateNetIncome(gross, tax);
    final Integer superannuation = salaryUtil.calculateSuperannuation(gross, income.getSuperannuationPercentage());

    final PayslipSummary payslipSummary = new PayslipSummary(
            new Employee(employee.getFirstName(), employee.getLastName()),
            month,
            new Salary(gross, net, tax, superannuation));

    LOG.debug("Payslip Summary {}", payslipSummary);

    return payslipSummary;
  }
}
