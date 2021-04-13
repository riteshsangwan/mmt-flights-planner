package com.mmt.flights.planner.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmt.flights.planner.ApiConstants;
import com.mmt.flights.planner.ErrorCodes;
import com.mmt.flights.planner.api.ApiError;
import com.mmt.flights.planner.properties.Planner;
import com.mmt.flights.planner.security.exception.AuthenticationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for all security filters
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractSecurityFilter extends OncePerRequestFilter {

    /**
     * Bearer token type
     */
    protected static final String BEARER = "Bearer";

    /**
     * A flag to indicate whether authorization header should be validated
     * For different api paths, we may or may not need to have authorization header mandatory
     * Client id is mandatory for all api's
     */
    private final boolean validateAuthorizationHeader;

    /**
     * Spring resolved bean for object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Application properties resolved by spring
     */
    @Autowired
    private Planner properties;

    /**
     * Get the logger instance
     *
     * @return the logger instance
     */
    protected abstract Logger getLogger();

    /**
     * Get the service token from the http servlet request
     *
     * @param request the http servlet request
     * @return the service token
     */
    protected String getServiceToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * Write response to {@link HttpServletResponse} object
     *
     * @param ex       the authentication exception that is thrown
     * @param response the http servlet response object to write response
     * @throws IOException if any io error occurs
     */
    protected void writeResponse(AuthenticationException ex, HttpServletResponse response)
            throws IOException {
        getLogger().warn("authentication exception in abstract security filter", ex);
        ApiError errorResponse = ApiError.builder()
                .serviceName(properties.getServiceName())
                .serviceVersion(properties.getServiceVersion())
                .message(ex.getMessage()).build();

        errorResponse.setCode(ex.getCode());

        response.setStatus(ex.getStatus().value());

        String json = objectMapper.writeValueAsString(errorResponse);
        // set response headers
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(json.getBytes(StandardCharsets.UTF_8).length);

        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> whitelistedClients = Arrays.asList(properties.getAuth().getWhitelistedClients().split(","));
        try {
            String token = getServiceToken(request);

            // validate client api key
            String clientApiKey = request.getHeader(ApiConstants.CLIENT_API_KEY_HEADER_NAME);

            if (StringUtils.isBlank(clientApiKey)) {
                throw new AuthenticationException(ErrorCodes.CLIENT_API_KEY_HEADER_MISSING, "Client api key header not specified");
            }

            if (!whitelistedClients.contains(clientApiKey)) {
                throw new AuthenticationException(ErrorCodes.CLIENT_NOT_WHITELISTED, HttpStatus.FORBIDDEN,
                        String.format("Client '%s' is not whitelisted", clientApiKey));
            }

            doFilterInternalInternal(clientApiKey, token);
        } catch (AuthenticationException ex) {
            writeResponse(ex, response);
            return;
        } catch (Exception ex) {
            writeResponse(new AuthenticationException(ErrorCodes.SERVER_ERROR, HttpStatus.UNAUTHORIZED,
                    "Unknown exception while validating security credentials", ex), response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Child classes must implement this filter to provide business security logic
     *
     * @param clientApiKey the client api key must never be null
     * @param accessToken  the optional access token
     * @throws Exception if any error occurs
     */
    protected abstract void doFilterInternalInternal(String clientApiKey, String accessToken) throws Exception;
}
