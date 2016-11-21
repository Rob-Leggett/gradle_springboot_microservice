package au.com.example.microservice.payslip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayslipSummariesWrapper {

  @JsonProperty(value = "payslipSummaries")
  private List<PayslipSummary> payslipSummaries;

  public PayslipSummariesWrapper() {}

  public PayslipSummariesWrapper(List<PayslipSummary> payslipSummaries) {
    this.payslipSummaries = payslipSummaries;
  }

  public List<PayslipSummary> getPayslipSummaries() {
    return payslipSummaries;
  }

  public void setPayslipSummaries(List<PayslipSummary> payslipSummaries) {
    this.payslipSummaries = payslipSummaries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PayslipSummariesWrapper that = (PayslipSummariesWrapper) o;

    return payslipSummaries != null ? payslipSummaries.equals(that.payslipSummaries) : that.payslipSummaries == null;

  }

  @Override
  public int hashCode() {
    return payslipSummaries != null ? payslipSummaries.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "PayslipSummariesWrapper{" +
            "payslipSummaries=" + payslipSummaries +
            '}';
  }
}
