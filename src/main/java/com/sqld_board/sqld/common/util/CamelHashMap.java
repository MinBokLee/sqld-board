package com.sqld_board.sqld.common.util;


import org.springframework.jdbc.support.JdbcUtils;

import java.util.LinkedHashMap;

/**
 * 데이터베이스 조회 결과를 담을 때, 스네이크 케이스(snake_case)의 키를 카멜 케이스(camelCase)로
 * 자동 변환하여 저장하는 LinkedHashMap의 확장 클래스입니다.
 * Mybatis 등에서 조회 결과를 Map으로 받을 경우 유용하게 사용될 수 있습니다.
 */
@SuppressWarnings("serial")
public class CamelHashMap extends LinkedHashMap<String, Object> {

    /**
     * 키(Key)를 카멜 케이스(camelCase)로 변환하여 값을 저장합니다.
     * @param key   스네이크 케이스(snake_case) 형태의 원본 키
     * @param value 저장할 값
     * @return      이전과 연관된 값 또는 없을 경우 null
     */
    @Override
    public Object put(String key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
    }
}
