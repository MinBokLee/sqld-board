package com.sqld_board.sqld.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 애플리케이션의 보안 설정을 담당하는 구성 클래스입니다.
 * RESTful API 설계에 맞춰 메서드 기반 보안 정책을 적용하였습니다.
 */
@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * 특정 경로들을 스프링 시큐리티 필터 체인에서 완전히 제외합니다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/demo-ui.html"
                                ,"/swagger-ui/**"
                                ,"/api-docs/**"
                                ,"/swagger-resources/**"
                                ,"/webjars/**"
                                ,"ws-stomp");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("********* Security Config Loading: WebSocket Support Added");


        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/ws-stomp/**").permitAll() //WebSocket 엔드포인트 허용
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // --- [1] PUBLIC API (누구나 접근 가능) ---

                // 게시판 관련: 모든 조회(GET) 요청 허용
                .requestMatchers(HttpMethod.GET, "/api/board/**").permitAll()

                // 인증 관련: 로그인, 회원가입, 토큰갱신, 인증번호(Logout 제외)
                .requestMatchers("/api/auth/signIn"
                                ,"/api/auth/signUp"
                                ,"/api/auth/token-refresh"
                                ,"/api/auth/verify/**"
                                ,"/api/common/**"
                                ,"/api/notification/**"
                                ).permitAll()

                // 회원 관련: 중복체크 및 비번 찾기
                .requestMatchers(HttpMethod.GET, "/api/members/check-**").permitAll()
                .requestMatchers("/api/members/password/**").permitAll()

                // 정적 리소스: 업로드 이미지
                .requestMatchers("/uploads/**").permitAll()

                // --- [2] PRIVATE API (인증 필요) ---

                // 게시판 관련: 작성(POST), 수정(PUT), 삭제(DELETE), 상태변경(PATCH)
                .requestMatchers(HttpMethod.POST, "/api/board/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/board/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/board/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/board/**").authenticated()

                // 인증/회원 관련: 로그아웃, 내정보, 프로필수정, 탈퇴
                .requestMatchers("/api/auth/logout").authenticated()
                .requestMatchers("/api/members/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/members/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/members/**").authenticated()

                // --- [3] ADMIN API (관리자 전용) ---
                .requestMatchers(HttpMethod.GET,"/api/boardMaster/**").permitAll() //조회는 pass
                .requestMatchers( "/api/admin/**"
                                 ,"/api/common-code-group/**"
                                 ,"/api/common-code-group-detail/**"
                                 ,"/api/boardMaster/**"
                                ).hasAnyRole("ADMIN", "SUPER_ADMIN")
                .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:3000",
            "http://127.0.0.1:5173",
            "http://localhost:63342/",
            "http://175.197.69.42:8881",
            "http://175.197.69.42",
            "https://sqld-front.pages.dev"


        ));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
