package com.sqld_board.sqld.util.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

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
