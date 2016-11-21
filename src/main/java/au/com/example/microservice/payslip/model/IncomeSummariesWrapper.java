package au.com.example.microservice.payslip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncomeSummariesWrapper {

  @Valid
  @JsonProperty(value = "incomeSummaries")
  @NotNull(message = "{error.income.summaries.notnull}")
  private List<IncomeSummary> incomeSummaries;

  public List<IncomeSummary> getIncomeSummaries() {
    return incomeSummaries;
  }

  public void setIncomeSummaries(List<IncomeSummary> incomeSummaries) {
    this.incomeSummaries = incomeSummaries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    IncomeSummariesWrapper that = (IncomeSummariesWrapper) o;

    return incomeSummaries != null ? incomeSummaries.equals(that.incomeSummaries) : that.incomeSummaries == null;

  }

  @Override
  public int hashCode() {
    return incomeSummaries != null ? incomeSummaries.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "IncomeSummariesWrapper{" +
            "incomeSummaries=" + incomeSummaries +
            '}';
  }
}
