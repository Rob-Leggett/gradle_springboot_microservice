package au.com.example.microservice.payslip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Month;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncomeSummary {

  @Valid
  @JsonProperty(value = "employee")
  @NotNull(message = "{error.employee.notnull}")
  private Employee employee;

  @Valid
  @JsonProperty(value = "month")
  @NotNull(message = "{error.month.notnull}")
  private Month month;

  @Valid
  @JsonProperty(value = "income")
  @NotNull(message = "{error.income.notnull}")
  private Income income;

  public IncomeSummary() {
    this(null, null, null);
  }

  public IncomeSummary(final Employee employee, final Month month, final Income income) {
    this.employee = employee;
    this.month = month;
    this.income = income;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Month getMonth() {
    return month;
  }

  public void setMonth(Month month) {
    this.month = month;
  }

  public Income getIncome() {
    return income;
  }

  public void setIncome(Income income) {
    this.income = income;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    IncomeSummary that = (IncomeSummary) o;

    if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
    if (month != null ? !month.equals(that.month) : that.month != null) return false;
    return income != null ? income.equals(that.income) : that.income == null;

  }

  @Override
  public int hashCode() {
    int result = employee != null ? employee.hashCode() : 0;
    result = 31 * result + (month != null ? month.hashCode() : 0);
    result = 31 * result + (income != null ? income.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "IncomeSummary{" +
            "employee=" + employee +
            ", month=" + month +
            ", income=" + income +
            '}';
  }
}
