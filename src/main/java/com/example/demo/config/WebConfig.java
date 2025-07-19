package com.example.demo.config;

import com.example.demo.aop.authResolver.userA.LoginUserAArgumentResolver;
import com.example.demo.aop.authResolver.userB.LoginUserBArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final LoginUserBArgumentResolver resolver;
  private final LoginUserAArgumentResolver resolverA;

  public WebConfig(LoginUserBArgumentResolver resolver, LoginUserAArgumentResolver resolverA) {
    this.resolver = resolver;
    this.resolverA = resolverA;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(resolver);
    resolvers.add(resolverA);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
          @Override
          public void addCorsMappings(CorsRegistry registry) {
              registry.addMapping("/**")
                  .allowedOrigins("http://localhost:8080", "http://localhost:3000", "file://")
                  .allowedMethods("*")
                  .allowCredentials(true);
          }
      };
  }
}
