package com.sqld_board.sqld.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {

    EXCEPTION("exception.code", "exception.msg"),
    SIGN_IN_FAILURE_EXCEPTION("signInFailureException.code","signInFailureException.msg"),
    MEMBER_NOT_FOUND_EXCEPTION("memberNotFoundException.code","memberNotFoundException.msg"),
    NOT_WRITE_BOARD_EXCEPTION("notWriteBoardException.code", "notWriteBoardException.msg"),
    EXIST_MAIL_EXCEPTION("existMailException.code", "existMailException.msg"),
    EXIST_MEMBER_EXCEPTION("existMemberException.code", "existMemberException.msg"),
    NOT_MATCH_USER_EXCEPTION("notMatchUserException.code", "notMatchUserException.msg"),
    CUSTOM_EXCEPTION("customException.code", "customException.msg"),
    NOT_BOARD_CONTENT_EXCEPTION("notBoardContentException.code", "notBoardContentException.msg"),
    NOT_POST_OWNER_EXCEPTION("notPostOwnerException.code", "notPostOwnerException.msg"),
    FAILURE_CREATE_CONTENT_EXCEPTION("failureCreateContentException.code", "failureCreateContentException.msg"),
    FAILURE_UPDATE_CONTENT_EXCEPTION("failureUpdateContentException.code", "failureUpdateContentException.msg"),
    FAILURE_UPDATE_COMMENT_EXCEPTION("failureUpdateCommentException.code", "failureUpdateCommentException.msg"),
    LOGIN_REQUIRED_EXCEPTION("loginRequiredException.code", "loginRequiredException.msg"),
    NOT_BOARD_COMMENT_EXCEPTION("notBoardCommentException.code", "notBoardCommentException.msg"),
    FAILURE_CREATE_COMMENT_EXCEPTION("failureCreateCommentException.code", "failureCreateCommentException.msg"),
    FAILURE_FILE_UPLOAD_ERROR_EXCEPTION("failureFileUploadErrorException.code", "failureFileUploadErrorException.msg"),
    NOT_FOUND_CONTENT_BOARD_EXCEPTION("notFoundContentBoardException.code", "notFoundContentBoardException.msg"),
    FILE_NOT_UPLOAD_EXCEPTION("fileNotUploadException.code", "fileNotUploadException.msg"),
    INVALID_SEARCH_KEYWORD_EXCEPTION("invalidSearchKeywordException.code", "invalidSearchKeywordException.msg"),
    NOT_FOUND_FILE_ID_EXCEPTION("notFoundFileIdException.code", "notFoundFileIdException.msg"),
    FILE_DOWNLOAD_EXCEPTION("fileDownloadException.code", "fileDownloadException.msg"),
    SCRAP_FAILED_EXCEPTION("scrapFailedException.code", "scrapFailedException.msg"),
    MISSING_DELETE_TARGET_EXCEPTION("missingDeleteTargetException.code", "missingDeleteTargetException.msg"),
    ALREADY_SCRAPPED_EXCEPTION("alreadyScrappedException.code", "alreadyScrappedException.msg"),
    NO_RESOURCE_FOUND_EXCEPTION("noResourceFoundException.code", "noResourceFoundException.msg"),
    SELF_AUTHORITY_CHANGE_EXCEPTION("selfAuthorityChangeException.code", "selfAuthorityChangeException.msg"),
    SUPER_ADMIN_PROTECTED_EXCEPTION("superAdminProtectedException.code", "superAdminProtectedException.msg"),
   INSUFFICIENT_ADMIN_PRIVILEGES_EXCEPTION("insufficientAdminPrivilegesException.code", "insufficientAdminPrivilegesException.msg");






    private final String code;
    private final String message;

    ExceptionType(String code, String message)  {
        this.code = code;
        this.message = message;
    }
}
