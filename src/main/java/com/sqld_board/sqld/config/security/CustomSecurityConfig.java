package com.sqld_board.sqld.config.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Log4j2
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("********* security config");

        //CORS 설정
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        //CSRF 설정
        // GET 방식을 제외한 모든 요청에 CSRF 토큰이라는 것을 사용하는 방식, 하지만 API 작서할 때는 사용하지 않는 것이 일반적이므로 설정을 추가
        // API 서버는 무상태를 기본으로 사용하기 때문에 서버 내부에서 세션을 생성하지 않도록 추가한다.
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CSRF, formLogin, httpBasic 설정 비활성화
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        //요청에 대한 접근 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/demo-ui.html",
                        "/api-docs/json/**",
                        "/swagger-ui/**",
                        "/api/common/signIn",
                        "/api/common/**",
                        "/api/board/**"
                ).permitAll()
                .anyRequest().permitAll()
        );

        return http.build();
    }

    //Cors 설정 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("https://sqld-front.pages.dev", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
