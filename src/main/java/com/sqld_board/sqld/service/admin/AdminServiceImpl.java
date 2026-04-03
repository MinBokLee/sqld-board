package com.sqld_board.sqld.service.admin;

import com.sqld_board.sqld.dto.response.admin.MemberResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.exception.admin.InsufficientAdminPrivilegesException;
import com.sqld_board.sqld.exception.admin.SelfAuthorityChangeException;
import com.sqld_board.sqld.exception.admin.SuperAdminProtectedException;
import com.sqld_board.sqld.exception.common.MemberNotFoundException;
import com.sqld_board.sqld.exception.member.CustomException;
import com.sqld_board.sqld.exception.member.NotMatchUserException;
import com.sqld_board.sqld.mapper.AdminMapper;
import com.sqld_board.sqld.mapper.BoardMapper;
import com.sqld_board.sqld.model.member.MemberInfo;
import com.sqld_board.sqld.service.token.RefreshTokenService;
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

    /**
     * 게시글 복구
     * @param boardId
     */
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
    public MemberResponse changeRoleByAdmin(String currentAdminId, boolean isSuperAdmin, String memberId) {


        // 대상 사용자의 정보와 현재 권한 조회
        MemberInfo member = adminMapper.getMemberRole(memberId);
        if(member == null){
            throw new MemberNotFoundException();
        }

        String targetRole = member.getUserRole();

        // [보안 규칙 1] 자기 자신의 권한은 절대 바꿀 수 없음 (공통)
        if(currentAdminId.equals(memberId)){// DB의 userId 와 현재 접속자 ID 비교
            throw new SelfAuthorityChangeException(); //본인의 권한은 직접 변경할 수 없습니다.
        }

        // [보안 규치 2] 대상이 SUPER_ADMIN인 경우, 누구도 그 권한을 뺏을 수 없음
        if("SUPER_ADMIN".equals(targetRole)){
            throw new SuperAdminProtectedException(); //최상위 관리자의 권한은 변경할 수 없습니다.
        }

        // [보안규칙 3] 일반 관리자의 권한 제한 (목적: 승격만 가능)
        if(!isSuperAdmin){
            if("ADMIN".equals(targetRole)){
             // 이미 관리자인 사람을 강등시키려 할 때 차단
                throw new InsufficientAdminPrivilegesException(); //해당 관리자 권한을 변경할 수 있는 권한이 부족합니다.
            }
        }

        // 3. 권한 변경 (Toggle)
        if("ADMIN".equals(targetRole)){
            adminMapper.changeRoleUser(memberId); // ADMIN -> USER (SUPER_ADMIN만 가능)
        } else{
            adminMapper.changeRoleAdmin(memberId); // USER -> ADMIN
        }
        //  userRole 업데이트 후, 최신 정보 조회
          MemberInfo updateRole = adminMapper.getMemberDetail(memberId);

        // 4. 프론트엔드용 응답 객체(DTO)변환하여 반환
        return new MemberResponse(updateRole);
    } //end of changeRoleByAdmin()

    /**
     * 사용자 강퇴
     * @param memberIds
     * @return
     */
    @Override
    @Transactional
    public MessageType deleteMembersBySuperAdmin(List<String> memberIds) {

        // 0.사용자가 null 이거나 선택되지 않은 경우
        if(memberIds ==null || memberIds.isEmpty()){
            throw new CustomException();
        }

        //1. 권한 체크
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_SUPER_ADMIN"));
        if(!isSuperAdmin){
            throw new InsufficientAdminPrivilegesException();
        }

        // 1-2 삭제할 사용자가 작성한 글과, 댓글 익명화
        boardMapper.anonymizePostsByAdmin(memberIds);
        boardMapper.anonymizeCommentsByAdmin(memberIds);

        // 2. 삭제 대상들의 리프레시 토큰 일괄 삭제
        for(String userId : memberIds){
            refreshTokenService.deleteByMemberId(userId);
        }

        // 3. 회원 정보 일괄 삭제
        adminMapper.deleteMembersBySuperAdmin(memberIds);

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
