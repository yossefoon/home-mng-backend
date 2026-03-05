package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${FAMILY_KEY:}")
    private String familyKey;

    @Bean
    public FilterRegistrationBean<FamilyKeyFilter> familyKeyFilter() {
        FilterRegistrationBean<FamilyKeyFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new FamilyKeyFilter(familyKey));
        reg.addUrlPatterns("/*");
        reg.setOrder(1);
        return reg;
    }
}