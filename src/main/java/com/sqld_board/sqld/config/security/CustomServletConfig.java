package com.sqld_board.sqld.config.security;

import com.sqld_board.sqld.util.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/***")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .maxAge(300)
//                .allowedHeaders("Authorization", "Content-Type");
//    }
}

/**
 *  CORS 설정은 @Controller가 있는 클래스에 @CrossOrign을 적용하거나 SpringSecurity를 이용하는 설정이 있다.
 @CrossOrigin 설정은 모든 컨트롤러에 개별적으로 적용해야 하므로 예제에서는 WebMvcConfigurer의 설정으로 사용한다.

 이는 보통 WebMvcConfigurer 인터페이스를 구현하거나 상속하여 사용하며, Spring MVC의 동작 방식을 설정합니다.

 CORS (Cross-Origin Resource Sharing): 웹 브라우저 보안 정책 때문에 발생하는 다른 도메인 간의 자원 공유 허용 여부를 설정합니다
 인터셉터 (Interceptor): Controller 실행 전후에 공통 로직을 처리합니다.

 뷰 리졸버 (View Resolver): Controller가 반환하는 논리적인 뷰 이름(예: "home")을 실제 템플릿 파일(예: /WEB-INF/views/home.jsp)로 변환합니다.
 정적 자원 핸들러: 이미지, CSS, JavaScript 같은 정적 파일을 어디서 찾을지 설정합니다.
 */
