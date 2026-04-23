package com.sqld_board.sqld.exception;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.exception.admin.InsufficientAdminPrivilegesException;
import com.sqld_board.sqld.exception.admin.SelfAuthorityChangeException;
import com.sqld_board.sqld.exception.admin.SuperAdminProtectedException;
import com.sqld_board.sqld.exception.board.*;
import com.sqld_board.sqld.exception.code.*;
import com.sqld_board.sqld.exception.common.*;
import com.sqld_board.sqld.exception.emailVerification.ExistMailException;
import com.sqld_board.sqld.exception.emailVerification.MailSendFailureException;
import com.sqld_board.sqld.exception.member.CustomException;
import com.sqld_board.sqld.exception.member.ExistMemberException;
import com.sqld_board.sqld.exception.member.NotMatchUserException;
import com.sqld_board.sqld.exception.websocket.TokenSignatureException;
import com.sqld_board.sqld.handler.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.sqld_board.sqld.exception.ExceptionType.*;


/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 클래스입니다.
 * {@code @RestControllerAdvice} 어노테이션을 사용하여 모든 컨트롤러에서 발생하는 예외를 가로채
 * 표준화된 형식의 에러 응답을 생성합니다.
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseHandler responseHandler;

    @ExceptionHandler(SelfDeleteResignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response SelfDeleteResignException(SelfDeleteResignException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(SELF_DELETE_RESIGN_EXCEPTION);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response TokenExpiredException(TokenExpiredException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(TOKEN_EXPIRED_EXCEPTION);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response InvalidRefreshTokenException(InvalidRefreshTokenException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(INVALID_REFRESH_TOKEN_EXCEPTION);
    }

    @ExceptionHandler(RefreshTokenMissingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response RefreshTokenMissingException(RefreshTokenMissingException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(REFRESH_TOKEN_MISSING_EXCEPTION);
    }

    @ExceptionHandler(ImageUpdateFailedException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Response ImageUpdateFailedException(ImageUpdateFailedException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(IMAGE_UPDATE_FAILED_EXCEPTION);
    }

    @ExceptionHandler(PasswordChangeFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response PasswordChangeFailedException(PasswordChangeFailedException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(PASSWORD_CHANGE_FAILED_EXCEPTION);
    }

    @ExceptionHandler(MailSendFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MailSendFailureException(MailSendFailureException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(MAIL_SEND_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response InvalidVerificationCodeException(InvalidVerificationCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(INVALID_VERIFICATION_CODE_EXCEPTION);
    }

    @ExceptionHandler(ExpiredVerificationCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response ExpiredVerificationCodeException(ExpiredVerificationCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXPIRED_VERIFICATION_CODE_EXCEPTION);
    }

    @ExceptionHandler(SignUpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response SignUpException(SignUpException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(SIGN_UP_EXCEPTION);
    }

    @ExceptionHandler(ExistUserNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response ExistUserNameException(ExistUserNameException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXIST_USER_NAME_EXCEPTION);
    }

    @ExceptionHandler(ExistUserIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response ExistUserIdException(ExistUserIdException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXIST_USER_ID_EXCEPTION);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response EmailNotVerifiedException(EmailNotVerifiedException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EMAIL_NOT_VERIFIED_EXCEPTION);
    }

    @ExceptionHandler(NotFoundEmailVerificationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundEmailVerificationException(NotFoundEmailVerificationException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(NOT_FOUND_EMAIL_VERIFICATION_EXCEPTION);
    }

    @ExceptionHandler(VerificationMailFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response VerificationMailFailed(VerificationMailFailedException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(VERIFICATION_MAIL_FAILED_EXCEPTION);
    }

    @ExceptionHandler(ExistsSortOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response ExistsSortOrderException(ExistsSortOrderException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXISTS_SORT_ORDER_EXCEPTION);
    }


    @ExceptionHandler(InvalidSortOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response InvalidSortOrderException(InvalidSortOrderException e){
        log.error(e.getMessage(),e);
        return responseHandler.getFailureResponse(INVALID_SORT_ORDER_EXCEPTION, e.getMaxAvailableOrder());
    }

    @ExceptionHandler(InvalidCategoryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response InvalidCategoryException(InvalidCategoryException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(INVALID_CATEGORY_EXCEPTION);
    }

    @ExceptionHandler(ExistsBoardNameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response ExistsBoardNameException(ExistsBoardNameException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXISTS_BOARD_NAME_EXCEPTION);
    }

    @ExceptionHandler(NotFoundBoardCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundBoardCodeException(NotFoundBoardCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(NOT_FOUND_BOARD_CODE_EXCEPTION);
    }

    @ExceptionHandler(ExistsBoardCodeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response ExistsBoardCodeException(ExistsBoardCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXISTS_BOARD_CODE_EXCEPTION);
    }

    @ExceptionHandler(ExistsCommonGroupDetailCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
        public Response ExistsCommonGroupDetailCodeException(ExistsCommonGroupDetailCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(EXISTS_COMMON_GROUP_DETAIL_CODE_EXCEPTION);
    }

    @ExceptionHandler(NotFoundGroupDetailCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundGroupDetailCodeException(NotFoundGroupDetailCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(NOT_FOUND_GROUP_DETAIL_CODE_EXCEPTION);
    }

    @ExceptionHandler(NotFoundGroupCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundGroupCodeException(NotFoundGroupCodeException e){
        log.error(e.getMessage(), e);
        return responseHandler.getFailureResponse(NOT_FOUND_GROUP_CODE_EXCEPTION);
    }


    @ExceptionHandler(ExistsCommonGroupCodeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response ExistsCommonGroupCodeException(ExistsCommonGroupCodeException e){
        log.error("ExistsCommonGroupCodeException", e);
        return responseHandler.getFailureResponse(EXISTS_COMMON_GROUP_CODE_EXCEPTION);
    }

    @ExceptionHandler(TokenSignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response TokenSignatureException(TokenSignatureException e){
        log.error("TokenSignatureException", e);
        return responseHandler.getFailureResponse(TOKEN_SIGNATURE_EXCEPTION);
    }

    @ExceptionHandler(InsufficientAdminPrivilegesException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response InsufficientAdminPrivilegesException(InsufficientAdminPrivilegesException e){
        log.error("InsufficientAdminPrivilegesException", e);
        return responseHandler.getFailureResponse(INSUFFICIENT_ADMIN_PRIVILEGES_EXCEPTION);
    }


    @ExceptionHandler(SuperAdminProtectedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response SuperAdminProtectedException(SuperAdminProtectedException e){
        log.error("SuperAdminProtectedException", e);
        return responseHandler.getFailureResponse(SUPER_ADMIN_PROTECTED_EXCEPTION);
    }

    @ExceptionHandler(SelfAuthorityChangeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response SelfAuthorityChangeException(SelfAuthorityChangeException e){
        log.error("SelfAuthorityChangeException", e);
        return responseHandler.getFailureResponse(SELF_AUTHORITY_CHANGE_EXCEPTION);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NoResourceFoundException(NoResourceFoundException e){
        log.error("정적 리소스를 찾을 수 없습니다: {}", e.getResourcePath());
        return responseHandler.getFailureResponse(NO_RESOURCE_FOUND_EXCEPTION);
    }

    @ExceptionHandler(AlreadyScrappedException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 error
    public Response AlreadyScrappedException(AlreadyScrappedException e){
        log.error("AlreadyScrappedException", e);
        return responseHandler.getFailureResponse(ALREADY_SCRAPPED_EXCEPTION);
    }

    @ExceptionHandler(MissingDeleteTargetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MissingDeleteTargetException(MissingDeleteTargetException e){
        log.error("MissingDeleteTargetException", e);
        return responseHandler.getFailureResponse(MISSING_DELETE_TARGET_EXCEPTION);
    }

    @ExceptionHandler(ScrapFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response ScrapFailedException(ScrapFailedException e){
        log.error("ScrapFailedException", e);
        return responseHandler.getFailureResponse(SCRAP_FAILED_EXCEPTION);
    }

    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response FileDownloadException(FileDownloadException e){
        log.error("파일 다우누로드중 오류가 발생했습니다.", e);
        return responseHandler.getFailureResponse(FILE_DOWNLOAD_EXCEPTION);
    }

    @ExceptionHandler(NotFoundFileIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundFileIdException(NotFoundFileIdException e){
        log.error("파일 정보를 찿을 수 없습니다.",e);
        return responseHandler.getFailureResponse(NOT_FOUND_FILE_ID_EXCEPTION);
    }

    @ExceptionHandler(InvalidSearchKeywordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response InvalidSearchKeywordException(InvalidSearchKeywordException e){
        log.error("입력된 검색어가 없습니다.", e);
        return responseHandler.getFailureResponse(INVALID_SEARCH_KEYWORD_EXCEPTION);
    }

    @ExceptionHandler(FileNotUploadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response FileNotUploadException(FileNotUploadException e){
        log.error("업로드할 파일을 찾을 수 없습니다.", e);
        return responseHandler.getFailureResponse(FILE_NOT_UPLOAD_EXCEPTION);
    }

    @ExceptionHandler(NotFoundContentBoardException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotFoundContentBoardException(NotFoundContentBoardException e){
        log.error("해당 게시글을 찾을 수 업습니다.", e);
        return responseHandler.getFailureResponse(NOT_FOUND_CONTENT_BOARD_EXCEPTION);
    }

    @ExceptionHandler(FailureFileUploadErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response FailureFileUploadErrorException(FailureFileUploadErrorException e){
        log.error("파일 업로드 중 오류가 발생");
        return responseHandler.getFailureResponse(FAILURE_FILE_UPLOAD_ERROR_EXCEPTION);

    }

    @ExceptionHandler(FailureCreateContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response FailureCreateContentException(FailureCreateContentException e) {
        log.error("게시판 글 작성 시 오류 발생", e);
        return responseHandler.getFailureResponse(FAILURE_CREATE_CONTENT_EXCEPTION);
    }

    @ExceptionHandler(FailureUpdateCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response FailureUpdateCommentException(FailureUpdateCommentException e){
        log.error("댓글 수정 중, 오류 발생", e);
        return responseHandler.getFailureResponse(FAILURE_UPDATE_COMMENT_EXCEPTION);
    }

    @ExceptionHandler(FailureCreateCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response FailureCreateCommentException(FailureCreateCommentException e){
        log.error("댓글 저장 중, 오류 발생", e);
        return responseHandler.getFailureResponse(FAILURE_CREATE_COMMENT_EXCEPTION);
    }

    @ExceptionHandler(NotBoardCommentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response NotBoardCommentException(NotBoardCommentException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(NOT_BOARD_COMMENT_EXCEPTION);
    }

    @ExceptionHandler(LoginRequiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response LoginRequiredException(LoginRequiredException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(LOGIN_REQUIRED_EXCEPTION);
    }

    @ExceptionHandler(FailureUpdateContentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response FailureUpdateContentException(FailureUpdateContentException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(FAILURE_UPDATE_CONTENT_EXCEPTION);
    }


    @ExceptionHandler(NotPostOwnerException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response NotPostOwnerException(NotPostOwnerException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(NOT_POST_OWNER_EXCEPTION);
    }

    @ExceptionHandler(NotBoardContentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response NotBoardContentException(NotBoardContentException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(NOT_BOARD_CONTENT_EXCEPTION);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response CustomException(CustomException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(CUSTOM_EXCEPTION);
    }

    @ExceptionHandler(NotMatchUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response NotMatchUserException(NotMatchUserException e){
        log.info("error", e);
        return responseHandler.getFailureResponse(NOT_MATCH_USER_EXCEPTION);
    }


    @ExceptionHandler(NotWriteBoardException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response notWriteBoard(NotWriteBoardException e) {
        log.info("error", e);
        return responseHandler.getFailureResponse(NOT_WRITE_BOARD_EXCEPTION);
    }


    /**
     * 처리되지 않은 모든 예외를 처리하여 500 Internal Server Error 응답을 반환합니다.
     * @param e 발생한 예외 객체
     * @return 서버 에러 응답
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e){
        log.info("error", e);
        return responseHandler.getFailureResponse(EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(ExistMemberException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    public Response existMember(ExistMemberException e) {
        log.info("ExistMemberException", e);
        return responseHandler.getFailureResponse(EXIST_MEMBER_EXCEPTION);
    }

    @ExceptionHandler(ExistMailException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    public Response ExistMail(ExistMailException e) {
        log.info("ExistMailException", e);
        return responseHandler.getFailureResponse(EXIST_MAIL_EXCEPTION);
    }

    /**
     * 로그인 실패 예외({@link InvalidCredentialsException})를 처리하여 401 Unauthorized 응답을 반환합니다.
     * @param e 발생한 예외 객체
     * @return 로그인 실패 응답
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response SignInFailureException(Exception e) {
        log.info("SignInFailureException", e);
        return responseHandler.getFailureResponse(INVALID_CREDENTIALS_EXCEPTION);
    }

    /**
     * 회원 정보를 찾지 못했을 때의 예외({@link MemberNotFoundException})를 처리하여 404 Not Found 응답을 반환합니다.
     * @return 회원 정보 없음 응답
     */
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response MemberNotFoundException() {
        return responseHandler.getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION);
    }

}


