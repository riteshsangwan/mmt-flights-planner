package com.mmt.flights.planner.router.exception.handler;

import com.google.common.base.Splitter;
import com.mmt.flights.planner.ErrorCodes;
import com.mmt.flights.planner.api.ApiClientError;
import com.mmt.flights.planner.api.ApiError;
import com.mmt.flights.planner.properties.Planner;
import com.mmt.flights.planner.security.exception.AuthenticationException;
import com.mmt.flights.planner.service.exception.ClientErrorException;
import com.mmt.flights.planner.service.exception.ServiceException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Global controller level exception handler
 * This class is responsible to translate exceptions to corresponding http status codes
 *
 * @author ritesh
 * @version 1.0.0
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Private static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * application level properties resolved by spring
     */
    @Autowired
    private Planner planner;

    /**
     * Check if the http servlet request has debug parameter
     *
     * @param request the http servlet request
     * @return <CODE>true</CODE> if debug request otherwise <CODE>false</CODE>
     */
    private boolean isDebugRequest(HttpServletRequest request) {
        // get the debug query parameter
        if (StringUtils.isNotBlank(request.getQueryString())) {
            final Map<String, String> parameters = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(request.getQueryString());
            return MapUtils.isNotEmpty(parameters) && parameters.containsKey("debug") && "true".equals(parameters.get("debug"));
        }
        return false;
    }

    /**
     * Add additional properties to the error response
     *
     * @param response the error response
     * @param request  the http servlet request
     * @param th       the exception that was thrown
     */
    private void additionalProperties(ApiError response, HttpServletRequest request, Throwable th) {
        // if client specified debug parameter and exception cause is not null
        if (isDebugRequest(request)) {
            final Map<String, Object> info = new HashMap<String, Object>();
            info.put("errorClass", th.getClass().getCanonicalName());
            info.put("stackTrace", th.getStackTrace());
            response.setInfo(info);
        }
    }

    /**
     * Handle any other exceptional cases
     *
     * @param th the cause
     * @return the error response
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleThrowable(HttpServletRequest request, Exception th) {
        LOGGER.error("unknown server exception", th);

        ApiError response = ApiError.builder()
                .serviceName(planner.getServiceName())
                .serviceVersion(planner.getServiceVersion())
                .code(ErrorCodes.SERVER_ERROR)
                .message(th.getMessage()).build();
        additionalProperties(response, request, th);

        return new ResponseEntity<ApiError>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle request rejected exception
     *
     * @param request the http servlet request
     * @param ex      the exception
     * @return the error response
     */
    @ExceptionHandler(value = RequestRejectedException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleRRException(HttpServletRequest request, RequestRejectedException ex) {
        LOGGER.error("request rejected exception", ex);

        ApiError response = ApiError.builder()
                .serviceName(planner.getServiceName())
                .serviceVersion(planner.getServiceVersion())
                .code(ErrorCodes.GENERIC_400_ERROR)
                .message(ex.getMessage()).build();
        additionalProperties(response, request, ex);

        return new ResponseEntity<ApiError>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle {@link ClientErrorException}
     *
     * @param cee the cause
     * @return the error response
     */
    @ExceptionHandler(ClientErrorException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleClientErrorException(HttpServletRequest request, ClientErrorException cee) {
        LOGGER.error("invalid client exception", cee);

        ApiClientError response = ApiClientError.builder()
                .serviceName(planner.getServiceName())
                .serviceVersion(planner.getServiceVersion())
                .code(cee.getCode())
                .message(cee.getMessage())
                .errorDetails(cee.getDetails())
                .build();

        additionalProperties(response, request, cee);

        return new ResponseEntity<ApiError>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle {@link ServiceException}
     *
     * @param se the cause
     * @return the error response
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleServiceException(HttpServletRequest request, ServiceException se) {
        if (se.getCause() instanceof ClientErrorException) {
            return handleClientErrorException(request, (ClientErrorException) se.getCause());
        } else if (se.getCause() instanceof AuthenticationException) {
            return handleAuthenticationException(request, (AuthenticationException) se.getCause());
        }

        LOGGER.error("unknown server service exception", se);
        ApiError response = ApiError.builder()
                .serviceName(planner.getServiceName())
                .serviceVersion(planner.getServiceVersion())
                .code(se.getCode())
                .message(se.getMessage()).build();
        additionalProperties(response, request, se);

        return new ResponseEntity<ApiError>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle {@link AuthenticationException}
     *
     * @param ae the cause
     * @return the error response
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleAuthenticationException(HttpServletRequest request, AuthenticationException ae) {
        ApiError response = ApiError.builder()
                .serviceName(planner.getServiceName())
                .serviceVersion(planner.getServiceVersion())
                .code(ae.getCode())
                .message(ae.getMessage()).build();
        additionalProperties(response, request, ae);
        return new ResponseEntity<ApiError>(response, ae.getStatus() != null ? ae.getStatus() : HttpStatus.UNAUTHORIZED);
    }
}
