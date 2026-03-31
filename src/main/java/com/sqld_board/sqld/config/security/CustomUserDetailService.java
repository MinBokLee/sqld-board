package com.sqld_board.sqld.config.security;

import com.sqld_board.sqld.mapper.MemberMapper;
import com.sqld_board.sqld.model.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security를 위한 커스텀 UserDetailsService 구현체.
 * 사용자 인증 시 데이터베이스에서 사용자 정보를 로드하는 역할을 담당합니다.
 * {@link UserDetailsService} 인터페이스를 구현하여 사용자 이름(userId)을 기반으로
 * {@link UserDetails} 객체를 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    /**
     * 회원 관련 데이터베이스 작업을 위한 매퍼.
     * {@link MemberMapper}를 통해 회원 정보를 조회합니다.
     */
    private final MemberMapper memberMapper;

    /**
     * 주어진 사용자 이름(userId)을 기반으로 사용자 상세 정보를 로드합니다.
     * Spring Security의 인증 프로세스에서 호출됩니다.
     *
     * @param username 인증을 시도하는 사용자의 ID (여기서는 userId에 해당).
     * @return 로드된 사용자 정보가 담긴 {@link UserDetails} 객체.
     * @throws UsernameNotFoundException 해당 사용자 이름으로 사용자를 찾을 수 없는 경우.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // MemberMapper를 사용하여 사용자 ID로 회원 정보를 조회합니다.
        // 회원 정보가 존재하면 createUserDetails 메서드를 호출하여 UserDetails 객체를 생성하고,
        // 그렇지 않으면 UsernameNotFoundException을 발생시킵니다.
        return memberMapper.readMemberByMemberId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + username));
    }

    /**
     * {@link MemberInfo} 객체를 기반으로 {@link UserDetails} 객체를 생성합니다.
     * 이 메서드는 데이터베이스에서 조회된 회원 정보를 Spring Security가 이해할 수 있는
     * UserDetails 형태로 변환하는 역할을 합니다.
     *
     * @param memberInfo 데이터베이스에서 조회된 회원 정보.
     * @return Spring Security의 {@link UserDetails} 구현체.
     */
    private UserDetails createUserDetails(MemberInfo memberInfo) {
        // Spring Security의 User 객체를 생성하여 반환합니다.
        // 첫 번째 인자는 사용자 ID, 두 번째는 암호화된 비밀번호, 세 번째는 사용자 권한(역할) 목록입니다.
        return new User(
                memberInfo.getMemberId(), // 사용자 ID를 Spring Security의 사용자 이름으로 사용
                memberInfo.getUserPass(), // 사용자의 암호화된 비밀번호
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + memberInfo.getUserRole())) // 사용자의 역할(예: ROLE_ADMIN, ROLE_USER)을 권한으로 설정
        );
    }
}
