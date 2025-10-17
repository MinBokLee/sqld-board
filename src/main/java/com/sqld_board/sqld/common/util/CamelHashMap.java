package com.sqld_board.sqld.common.util;


import org.springframework.jdbc.support.JdbcUtils;

import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class CamelHashMap extends LinkedHashMap<String, Object> {
    /**
     */
    @Override
    public Object put(String key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
    }
}
