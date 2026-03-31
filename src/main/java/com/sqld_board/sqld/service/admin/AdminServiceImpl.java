package com.sqld_board.sqld.service.admin;

import com.sqld_board.sqld.dto.response.admin.MemberResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.exception.member.CustomException;
import com.sqld_board.sqld.exception.member.NotMatchUserException;
import com.sqld_board.sqld.mapper.AdminMapper;
import com.sqld_board.sqld.mapper.BoardMapper;
import com.sqld_board.sqld.model.member.MemberInfo;
import com.sqld_board.sqld.service.token.RefreshTokenService;
import com.sqld_board.sqld.service.token.RefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final BoardMapper boardMapper;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void restore(Long boardId) {
        adminMapper.restoreBoardContent(boardId);
    }

    /**
     * 사용자 -> 운영자로 Role 변경
     * @param memberId
     */
    @Override
    @Transactional
    public void changeRoleAdmin(String memberId) {
        adminMapper.changeRoleAdmin(memberId);
    }

    /**
     * 사용자 강퇴
     * @param userIds
     * @return
     */
    @Override
    @Transactional
    public MessageType deleteMembersByAdmin(List<String> userIds) {

        // 0.사용자가 null 이거나 선택되지 않은 경우
        if(userIds ==null || userIds.isEmpty()){
            throw new CustomException();
        }

        //1. 권한 체크
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin){
            throw new NotMatchUserException();
        }

        // 1-2 삭제할 사용자가 작성한 글과, 댓글 익명화
        boardMapper.anonymizePostsByAdmin(userIds);
        boardMapper.anonymizeCommentsByAdmin(userIds);

        // 2. 삭제 대상들의 리프레시 토큰 일괄 삭제
        for(String userId : userIds){
            refreshTokenService.deleteByMemberId(userId);
        }

        // 3. 회원 정보 일괄 삭제
        adminMapper.deleteMembersByAdmin(userIds);

        return MessageType.ADMIN_KICK_SUCCESS;
    }

    /**
     * 회원 리스트를 불러온다.
     * @return
     */
    @Override
    @Transactional
    public List<MemberResponse> getMemberList() {
        List<MemberInfo> memberList = adminMapper.getMemberList();

        return memberList.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }
}
