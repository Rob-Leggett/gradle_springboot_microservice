package au.com.example.microservice.payslip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Income {

  @JsonProperty(value = "gross")
  @NotNull(message = "{error.gross.notnull}")
  @Min(value = 0, message = "{error.gross.min}")
  private Integer gross;

  @JsonProperty(value = "superannuationPercentage")
  @NotNull(message = "{error.superannuation.notnull}")
  @Min(value = 0, message = "{error.superannuation.min}")
  @Max(value = 50, message = "{error.superannuation.max}")
  private Integer superannuationPercentage;

  public Income() {
    this(null, null);
  }

  public Income(final Integer gross, final Integer superannuationPercentage) {
    this.gross = gross;
    this.superannuationPercentage = superannuationPercentage;
  }

  public Integer getGross() {
    return gross;
  }

  public void setGross(Integer gross) {
    this.gross = gross;
  }

  public Integer getSuperannuationPercentage() {
    return superannuationPercentage;
  }

  public void setSuperannuationPercentage(Integer superannuationPercentage) {
    this.superannuationPercentage = superannuationPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Income income = (Income) o;

    if (gross != null ? !gross.equals(income.gross) : income.gross != null) return false;
    return superannuationPercentage != null ? superannuationPercentage.equals(income.superannuationPercentage) : income.superannuationPercentage == null;

  }

  @Override
  public int hashCode() {
    int result = gross != null ? gross.hashCode() : 0;
    result = 31 * result + (superannuationPercentage != null ? superannuationPercentage.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Income{" +
            "gross=" + gross +
            ", superannuationPercentage=" + superannuationPercentage +
            '}';
  }
}
