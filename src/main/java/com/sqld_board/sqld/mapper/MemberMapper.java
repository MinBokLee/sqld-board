package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.response.member.MemberSimpleInfoRes;
import com.sqld_board.sqld.model.member.MemberInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 회원(Member) 관련 데이터베이스 작업을 위한 MyBatis 매퍼 인터페이스입니다.
 * resources/mapper/Member.xml 파일과 매핑됩니다.
 */
@Mapper
@Repository
public interface MemberMapper {

    Optional<MemberSimpleInfoRes> readMemberSimpleInfo(@Param("memberId") String memberId);

    // 회원 삭제 (딘건)
    void deleteMember(@Param("memberId") String memberId);


    // 회원 이메일 인증 시, 이미 가입된 회원 여부 체크
    boolean existsByUserEmail(@Param("email") String email);

//    // 가입인사 글 작성 시, 상태값 변경
//    int updateUserStatus(@Param("memberId") String memberId,
//                         @Param("userStatus") String userStatus);
//
//    int checkGreetingBoardWrite(@Param("userId") String memberId);

    /**
     * 사용자의 최근 접속 기록을 저장 합니다.
     * @param memberId
     */
    void updateLastLogin(@Param("memberId") String memberId);

    /**
     * 사용자 ID가 이미 존재하는지 확인합니다.
     * @param userId 확인할 사용자 ID
     * @return 존재하면 true, 아니면 false
     */
    boolean existsByUserId(@Param("userId") String userId);

    /**
     * 사용자 이름이 이미 존재하는지 확인합니다.
     * @param userName 확인할 사용자 이름
     * @return 존재하면 true, 아니면 false
     */
    boolean existsByUserName(@Param("userName") String userName);

    /**
     * 새로운 회원 정보를 데이터베이스에 삽입합니다.
     * @param req 삽입할 회원 정보를 담은 {@link MemberInfo} 객체
     * @return 영향을 받은 행의 수
     */
    int signUpMember(MemberInfo req);

    Optional<MemberInfo> readMemberByMemberId(@Param("memberId") String memberId);

    /**
     * 사용자 ID를 기준으로 회원 정보를 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 조회된 회원 정보를 담은 {@link Optional<MemberInfo>} 객체
     */
    Optional<MemberInfo> readMemberByUsersId(@Param ("userId") String userId);

    /**
     * 사용자 ID와 이메일이 일치하는 회원이 존재하는지 확인합니다.
     * @param userId 사용자 ID
     * @param email 이메일 주소
     * @return 존재하면 true, 아니면 false
     */
    boolean existsByUserIdAndEmail(@Param("userId") String userId, @Param("email") String email);

    /**
     * 사용자의 비밀번호를 업데이트합니다.
     * @param memberId 사용자 ID
     * @param userPass 새 암호화된 비밀번호
     * @return 영향을 받은 행의 수
     */
    int updatePassword(@Param("memberId") String memberId, @Param("userPass") String userPass);

    /**
     * 사용자의 프로필 이미지를 업데이트합니다.
     * @param memberId 사용자 ID
     * @param profileImage 새 프로필 이미지 URL
     * @return 영향을 받은 행의 수
     */
    int updateProfileImage(@Param("memberId") String memberId, @Param("profileImage") String profileImage);

//    /**
//     * 사용자의 총 게시글 수를 조회합니다.
//     * @param memberId 사용자 ID
//     * @return 게시글 수
//     */
//    int getPostCount(@Param("memberId") String memberId);
//
    /**
     * 사용자의 총 댓글 수를 조회합니다.
     * @param memberId 사용자 ID
     * @return 댓글 수
     */
    int getCommentCount(@Param("memberId") String memberId);

    /**
     * 새로운 회원의 ID를 위한 시퀀스 번호를 생성합니다. (MySQL member 테이블 사용)
     * @param params(name) 식별을 위한 이름 (기본값으로 활용)
     * @return 생성된 ID
     */
    void insertMemberSequence(Map<String, Object> params);

    /**
     * 마지막으로 생성된 ID를 가져옵니다.
     * MyBatis의 selectKey 또는 useGeneratedKeys를 활용할 예정이므로 이 메서드는 보조적입니다.
     */
    Long getLastInsertId();

    /**
     * 새로운 회원의 ID를 위한 다음 시퀀스 값을 데이터베이스에서 가져옵니다. (기존 MariaDB 방식 - 삭제 예정)
     * @return 다음 시퀀스 번호
     */
    Long getNextMemberSequence();
}
