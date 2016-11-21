package au.com.example.microservice.payslip;


import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummary;

/**
 *
 */
public interface PayslipService {
  PayslipSummary calculate(IncomeSummary incomeSummary);
}
