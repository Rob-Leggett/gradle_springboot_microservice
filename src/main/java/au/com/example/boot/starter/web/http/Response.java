package au.com.example.boot.starter.web.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

  private List<Error> errors;

  //Private no-arg constructor to allow deserialization of the object.
  //The deserialization would be useful if this object needs to be serialized and
  //read back (deserialized) by the SAME microservice.
  //This is not meant to be used as shared interface object between different microservices,
  //to prevent version dependencies on the starter libraries between the different services.
  private Response() {
  }

  Response(List<Error> errors) {
    this.errors = errors;
  }

  public List<Error> getErrors() {
    return errors;
  }

}
