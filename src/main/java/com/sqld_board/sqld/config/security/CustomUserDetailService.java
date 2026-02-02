package com.sqld_board.sqld.config.security;

// 스프링 시큐리티는 사용자의 인증 처리를 위해서 UserDetailService 라는 인터페이스의 구현체를 활용한다.

import com.sqld_board.sqld.mapper.MemberMapper;
import com.sqld_board.sqld.mapper.SampleMapper;
import com.sqld_board.sqld.model.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        log.info("******** loadUserByUsername *******");

        return null;
    }
}
