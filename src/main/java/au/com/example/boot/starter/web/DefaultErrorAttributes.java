package au.com.example.boot.starter.web;

import au.com.example.boot.starter.web.http.HttpStatusHelper;
import au.com.example.boot.starter.web.http.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     *
     * @param requestAttributes
     * @param includeStackTrace
     * @return
     */
    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        HttpStatus status = HttpStatusHelper.getStatus(requestAttributes);
        return Collections.singletonMap("errors", responseBuilder.buildResponse(status.name(), status.getReasonPhrase()).getErrors());
    }

    /**
     *
     * @param requestAttributes
     * @return
     */
    @Override
    public Throwable getError(RequestAttributes requestAttributes) {
        return null;
    }

    /**
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
