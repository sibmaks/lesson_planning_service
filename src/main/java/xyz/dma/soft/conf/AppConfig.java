package xyz.dma.soft.conf;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.dma.soft.conf.handler.RestRequestLoggerFilter;
import xyz.dma.soft.conf.handler.SessionRequiredInterceptor;

@Component
@AllArgsConstructor
public class AppConfig implements WebMvcConfigurer {
    private final SessionRequiredInterceptor productServiceInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    @ConditionalOnMissingBean({RestRequestLoggerFilter.class})
    public FilterRegistrationBean<?> restLoggingFilterRegistrationBean() {
        FilterRegistrationBean<RestRequestLoggerFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RestRequestLoggerFilter());
        registration.addUrlPatterns("/v3/*");
        return registration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(productServiceInterceptor);
    }
}
