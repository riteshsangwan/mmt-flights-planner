package com.mmt.flights.planner.config;

import com.mmt.flights.planner.security.filter.ClientApiKeyFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.mmt.flights.planner.ApiConstants.APIS_REQUEST_MATCHER_PATH;

/**
 * Application level security configuration
 *
 * @author sangwan
 * @version 1.0.0
 */
@Configuration
@Order(2)
public class ClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Client api filter
        final ClientApiKeyFilter clientApiKeyFilter = new ClientApiKeyFilter();
        this.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(clientApiKeyFilter);

        http.requestMatchers().antMatchers(APIS_REQUEST_MATCHER_PATH)
                .and().formLogin().disable().httpBasic().disable()
                .rememberMe().disable().logout().disable().x509().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable().cors().configurationSource(getCorsConfigurationSource())
                .and().addFilterBefore(clientApiKeyFilter, AnonymousAuthenticationFilter.class);
    }

    /**
     * Get the cors configuration source
     *
     * @return {@link CorsConfigurationSource} implementation
     */
    private CorsConfigurationSource getCorsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(APIS_REQUEST_MATCHER_PATH, corsConfiguration);

        return source;
    }
}
