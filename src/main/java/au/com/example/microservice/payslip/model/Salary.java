package au.com.example.microservice.payslip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Salary {

  @JsonProperty(value = "gross")
  private Integer gross;

  @JsonProperty(value = "net")
  private Integer net;

  @JsonProperty(value = "tax")
  private Integer tax;

  @JsonProperty(value = "superannuation")
  private Integer superannuation;

  public Salary() {
    this(null, null, null, null);
  }

  public Salary(final Integer gross, final Integer net, final Integer tax, final Integer superannuation) {
    this.gross = gross;
    this.net = net;
    this.tax = tax;
    this.superannuation = superannuation;
  }

  public Integer getGross() {
    return gross;
  }

  public void setGross(Integer gross) {
    this.gross = gross;
  }

  public Integer getNet() {
    return net;
  }

  public void setNet(Integer net) {
    this.net = net;
  }

  public Integer getTax() {
    return tax;
  }

  public void setTax(Integer tax) {
    this.tax = tax;
  }

  public Integer getSuperannuation() {
    return superannuation;
  }

  public void setSuperannuation(Integer superannuation) {
    this.superannuation = superannuation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Salary salary = (Salary) o;

    if (gross != null ? !gross.equals(salary.gross) : salary.gross != null) return false;
    if (net != null ? !net.equals(salary.net) : salary.net != null) return false;
    if (tax != null ? !tax.equals(salary.tax) : salary.tax != null) return false;
    return superannuation != null ? superannuation.equals(salary.superannuation) : salary.superannuation == null;

  }

  @Override
  public int hashCode() {
    int result = gross != null ? gross.hashCode() : 0;
    result = 31 * result + (net != null ? net.hashCode() : 0);
    result = 31 * result + (tax != null ? tax.hashCode() : 0);
    result = 31 * result + (superannuation != null ? superannuation.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Salary{" +
            "gross=" + gross +
            ", net=" + net +
            ", tax=" + tax +
            ", superannuation=" + superannuation +
            '}';
  }
}
