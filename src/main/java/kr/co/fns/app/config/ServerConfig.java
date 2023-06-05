package kr.co.fns.app.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import kr.co.fns.app.config.resolver.CustomArgumentResolver;
import kr.co.fns.app.core.token.ExceptionHandlerFilter;
import kr.co.fns.app.core.token.RequestTokenFilter;
import kr.co.fns.app.core.token.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ServerConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;
    private final ApplicationProperties applicationProperties;
    private final ExternalUrlProperties properties;

    @Value("${spring.profiles.active}")
    private String profileActive;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecretKey.getBytes(), MacAlgorithm.HS256.getName());

        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 주의!, RequestTokenFilter.java 의 patterns 배열에도 동일하게 넣어줘야 한다. 동일하지 않으면 장애발생!
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(ExcludePathPatterns.excludePathPatternsMap.keySet().stream().collect(Collectors.toList())); //     RequestTokenFilter.excludePaths)));
    }

    /**
     * RequestFilter 필터는 interceptor내 HttpServletRequest 이슈용도로 추가.
     */
    /*@Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new RequestFilter());
        registrationBean.setOrder(3);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }*/


    /**
     * RequestTokenFilter, token 인증처리 필터
     */
    @Bean
    public FilterRegistrationBean getServeletFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new RequestTokenFilter(properties));
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("sso-token-validation");
        return registrationBean;
    }


    /**
     * ExceptionHandlerFilter, exception처리 필터
     */
    @Bean
    public FilterRegistrationBean getServeletErrorRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new ExceptionHandlerFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("error-handler");
        return registrationBean;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        configurer.addPathPrefix(applicationProperties.getPrefix() +applicationProperties.getVersion(),
                path -> Arrays.stream(applicationProperties.getExcludePrefixPath())
                                .anyMatch(p -> path.getName().indexOf(p) <= 0)
        );
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        SimpleModule simpleModule = new SimpleModule();
        StringDeserializer stringDeserializer = new StringDeserializer();
        simpleModule.addDeserializer(String.class, stringDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.registerModule(simpleModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); // snake case 로 변환
        return objectMapper;
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver());
    }

    @Bean
    public HandlerMethodArgumentResolver customArgumentResolver() {
        return new CustomArgumentResolver(jwtDecoder());
    }

}

   // to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS