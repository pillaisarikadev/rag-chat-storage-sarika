package rag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value("${app.apiKey:}")
    private String apiKey;

    @Value("${app.rateLimitPerMinute:120}")
    private int rateLimit;

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ApiKeyFilter(apiKey));
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RateLimitFilter(rateLimit));
        bean.setOrder(2);
        return bean;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${app.cors.allowedOrigins:*}") String allowedOrigins) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("GET","POST","PATCH","DELETE","PUT","OPTIONS")
                        .allowCredentials(false);
            }
        };
    }
}
