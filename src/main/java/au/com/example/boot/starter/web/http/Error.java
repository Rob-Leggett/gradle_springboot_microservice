package au.com.example.boot.starter.web.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

  private UUID id;
  private String code;
  private String detail;
  private Source source;

  private Error() {
  }

  public Error(UUID id, String code, String detail) {
    this.id = id;
    this.code = code;
    this.detail = detail;
  }

  public Error(String code, String detail) {
    this(UUID.randomUUID(), code, detail);
  }

  public Error withPointer(String pointer) {
    this.source = new Source(pointer, null);
    return this;
  }

  public Error withParameter(String parameter) {
    this.source = new Source(null, parameter);
    return this;
  }


  public UUID getId() {
    return id;
  }

  public Source getSource() {
    return source;
  }

  public String getCode() {
    return code;
  }

  public String getDetail() {
    return detail;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Source {

    private String pointer;
    private String parameter;

    private Source() {
    }

    public Source(String pointer, String parameter) {
      this.pointer = pointer;
      this.parameter = parameter;
    }

    public String getPointer() {
      return pointer;
    }

    public String getParameter() {
      return parameter;
    }
  }
}
