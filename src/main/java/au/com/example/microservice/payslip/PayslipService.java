package au.com.example.microservice.payslip;


import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummary;

/**
 * Calculates payslip summaries.
 */
public interface PayslipService {
  PayslipSummary calculate(IncomeSummary incomeSummary);
}
