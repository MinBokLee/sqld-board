    package com.sqld_board.sqld.service.sign;

    import com.sqld_board.sqld.common.util.AES256UTIL;
    import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
    import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
    import com.sqld_board.sqld.exception.common.MemberNotFoundException;
    import com.sqld_board.sqld.exception.common.SignInFailureException;
    import com.sqld_board.sqld.mapper.MemberMapper;
    import com.sqld_board.sqld.model.memberInfo.MemberInfo;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class SignService {

        private final MemberMapper memberMapper;
        private final PasswordEncoder passwordEncoder;

        @Value("${aes.secret}")
        private String aesKey64;

        /**
         * 로그인
         * @param req
         * @return
         */
        public boolean signIn(SignInMemberReq req)  {
            MemberInfo foundMember = readMemberByUsersId(req);
            if(foundMember != null){
                log.info("same");
                return true;
            } else{
                return false;
            }



        }

        /**
         * 회원 가입
         * @param req
         * @return
         */
        public boolean signUpMember(SignUpMemberReq req) {
               try{
                   // 비밀번호 암호화
                   encryptPassword(req);
                   // 비밀번호 인증 확인

                   int result = memberMapper.signUpMember(req);
                   return result ==1;
               }  catch (Exception e){
                   log.error("회원가입 실패", e);
               }
               return false;
            }

        /**********************/
        /*** Private Method ***/
        /**********************/

        /**
         * 로그인 시 ID 확인
         * @param req
         * @return
         */
        private MemberInfo readMemberByUsersId(SignInMemberReq req) {
            return memberMapper.readMemberByUsersId(req.getUserId()).orElseThrow(MemberNotFoundException::new);
        }


        /**
         * 비밀번호 암호화
         * @param req
         * @throws Exception
         */
        private void encryptPassword(SignUpMemberReq req) throws Exception{
            // base64 디코딩된 AES 키 사용
            byte[] aesKeyBytes = java.util.Base64.getDecoder().decode(aesKey64);
            String aesKey = new String(aesKeyBytes, java.nio.charset.StandardCharsets.UTF_8);
            AES256UTIL crypto = new AES256UTIL(aesKey);

            // paas AES256 암호화 저장
            String encryptedPwd = crypto.encrypt(req.getUserPass());
            req.setUserPass(encryptedPwd);
        }

        /**
         * 비밀번호 확인
         * @param req
         * @param member
         */
        private void validatePAssWord(SignUpMemberReq req, MemberInfo member) {

            if(!passwordEncoder.matches(req.getUserPass(), member.getUserPass()));
                throw new SignInFailureException();
        }


    }


