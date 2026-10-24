package au.com.example.boot.starter.web;

import au.com.example.boot.starter.web.http.HttpStatusHelper;
import au.com.example.boot.starter.web.http.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * Default error attributes. This class is used by the {@link DefaultErrorController}
 * when handling exceptions, which were not handled by the {@link au.com.example.boot.starter.web.DefaultExceptionHandler},
 * i.e. errors that occur before a request reaches a controller.
 * </p>
 * The default error attributes are:
 * <ul>
 * <li>errors - this attribute value is defined in the {@link au.com.example.boot.starter.web.http.Error}</li>
 * </ul>
 */
public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultErrorAttributes.class);

    private ResponseBuilder responseBuilder;

    public DefaultErrorAttributes(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    /**
     * Returns the error attributes, populated with the errors response for the current request status.
     *
     * @param webRequest
     * @param options
     * @return
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        HttpStatus status = HttpStatusHelper.getStatus(webRequest);
        return Collections.singletonMap("errors", responseBuilder.buildResponse(status.name(), status.getReasonPhrase()).getErrors());
    }

    /**
     * Returns the error for the request, which is not exposed by these attributes.
     *
     * @param webRequest
     * @return
     */
    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }

    /**
     * Logs the unexpected exception and lets the remaining resolvers handle it.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOG.error("handling unexpected exception", ex);
        return null;
    }
}
