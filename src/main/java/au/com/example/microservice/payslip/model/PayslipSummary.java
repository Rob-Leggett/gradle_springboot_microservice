package au.com.example.microservice.payslip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Month;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayslipSummary {

  @JsonProperty(value = "employee")
  private Employee employee;

  @JsonProperty(value = "month")
  private Month month;

  @JsonProperty(value = "salary")
  private Salary salary;

  public PayslipSummary() {
    this(null, null, null);
  }

  public PayslipSummary(final Employee employee, final Month month, final Salary salary) {
    this.employee = employee;
    this.month = month;
    this.salary = salary;
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

  public Salary getSalary() {
    return salary;
  }

  public void setSalary(Salary salary) {
    this.salary = salary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PayslipSummary that = (PayslipSummary) o;

    if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
    if (month != null ? !month.equals(that.month) : that.month != null) return false;
    return salary != null ? salary.equals(that.salary) : that.salary == null;

  }

  @Override
  public int hashCode() {
    int result = employee != null ? employee.hashCode() : 0;
    result = 31 * result + (month != null ? month.hashCode() : 0);
    result = 31 * result + (salary != null ? salary.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PayslipSummary{" +
            "employee=" + employee +
            ", month=" + month +
            ", salary=" + salary +
            '}';
  }
}
