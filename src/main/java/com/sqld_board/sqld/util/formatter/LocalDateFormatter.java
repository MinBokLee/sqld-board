package com.sqld_board.sqld.util.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * {@link java.time.LocalDate} 타입을 위한 커스텀 포맷터(Formatter) 클래스입니다.
 * Spring의 {@link Formatter} 인터페이스를 구현하여, 문자열('yyyy-MM-dd')과 LocalDate 객체 간의 변환을 처리합니다.
 * 이 포맷터는 {@link com.sqld_board.sqld.config.security.CustomServletConfig}에 등록되어 사용됩니다.
 */
public class LocalDateFormatter implements Formatter<LocalDate> {

    /**
     * "yyyy-MM-dd" 형식의 문자열을 LocalDate 객체로 변환(파싱)합니다.
     * @param text 파싱할 날짜 문자열
     * @param locale 현재 사용자의 로케일
     * @return 변환된 LocalDate 객체
     * @throws ParseException 파싱 실패 시 발생
     */
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * LocalDate 객체를 "yyyy-MM-dd" 형식의 문자열로 변환합니다.
     * @param object 포맷팅할 LocalDate 객체
     * @param locale 현재 사용자의 로케일
     * @return 변환된 날짜 문자열
     */
    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    }
}

/**
 * Formatter를 이용한 LocalDate처리
 * 날짜 / 시간은 브라우저에서 문자열로 전송되지만, 서버에서는 LocalDate 또는, LocalDateTime으로 처리된다.
 * 그렇기 때문에 이를 변환해 주는 Formatter을 추가해서 자동으로 처리할 수 있게 한다.
 *
 * 작성된 LocalDateFormatter는 스프링MVC 동작과정에서 사용돌 수 있도록 설정을 추가해 주어야 한다.
 * Config> CustomServletConfig 클래스를 추가.
 *
 */
