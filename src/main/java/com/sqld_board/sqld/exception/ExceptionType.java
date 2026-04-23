package com.sqld_board.sqld.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {

    EXCEPTION("exception.code", "exception.msg"),
    INVALID_CREDENTIALS_EXCEPTION("invalidCredentialsException.code", "invalidCredentialsException.msg"),
    MEMBER_NOT_FOUND_EXCEPTION("memberNotFoundException.code","memberNotFoundException.msg"),
    IMAGE_UPDATE_FAILED_EXCEPTION("imageUpdateFailedException.code", "imageUpdateFailedException.msg"),
    REFRESH_TOKEN_MISSING_EXCEPTION("refreshTokenMissingException.code", "refreshTokenMissingException.msg"),
    INVALID_REFRESH_TOKEN_EXCEPTION("invalidIdRefreshTokenException.code", "invalidIdRefreshTokenException.msg"),
    SELF_DELETE_RESIGN_EXCEPTION("selfDeleteResignException.code", "selfDeleteResignException.msg"),
    TOKEN_EXPIRED_EXCEPTION("tokenExpiredException.code", "tokenExpiredException.msg"),
    MAIL_SEND_FAILURE_EXCEPTION("mailSendFailureException.code", "mailSendFailureException.msg"),
    PASSWORD_CHANGE_FAILED_EXCEPTION("passwordChangeFailedException.code", "passwordChangeFailedException.msg"),
    NOT_FOUND_EMAIL_VERIFICATION_EXCEPTION("notFoundEmailVerificationException.code","notFoundEmailVerificationException.msg"),
    EMAIL_NOT_VERIFIED_EXCEPTION("emailNotVerifiedException.code","emailNotVerifiedException.msg"),
    EXIST_USER_ID_EXCEPTION("existUserIdException.code","existUserIdException.msg"),
    EXIST_USER_NAME_EXCEPTION("existUserNameException.code","existUserNameException.msg"),
    SIGN_UP_EXCEPTION("signUpException.code", "signUpException.msg"),
    EXPIRED_VERIFICATION_CODE_EXCEPTION("expiredVerificationCode.code","expiredVerificationCode.msg"),
    INVALID_VERIFICATION_CODE_EXCEPTION("invalidVerificationCode.code","invalidVerificationCode.msg"),
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
    INSUFFICIENT_ADMIN_PRIVILEGES_EXCEPTION("insufficientAdminPrivilegesException.code", "insufficientAdminPrivilegesException.msg"),
    TOKEN_SIGNATURE_EXCEPTION("tokenSignatureException.code", "tokenSignatureException.msg"),
    EXISTS_COMMON_GROUP_CODE_EXCEPTION("existsCommonGroupCodeException.code", "existsCommonGroupCodeException.msg"),
    NOT_FOUND_GROUP_CODE_EXCEPTION("notFoundGroupCodeException.code", "notFoundGroupCodeException.msg"),
    NOT_FOUND_GROUP_DETAIL_CODE_EXCEPTION("notFoundGroupDetailCodeException.code", "notFoundGroupDetailCodeException.msg"),
    EXISTS_COMMON_GROUP_DETAIL_CODE_EXCEPTION("existsCommonGroupDetailCodeException.code", "existsCommonGroupDetailCodeException.msg"),
    EXISTS_BOARD_CODE_EXCEPTION("existsBoardCodeException.code", "existsBoardCodeException.msg"),
    NOT_FOUND_BOARD_CODE_EXCEPTION("notFoundBoardCodeException.code", "notFoundBoardCodeException.msg"),
    EXISTS_BOARD_NAME_EXCEPTION("existsBoardNameException.code", "existsBoardNameException.msg"),
    INVALID_CATEGORY_EXCEPTION("invalidCategoryException.code", "invalidCategoryException.msg"),
    INVALID_SORT_ORDER_EXCEPTION("invalidSortOrderException.code", "invalidSortOrderException.msg"),
    EXISTS_SORT_ORDER_EXCEPTION("existsSortOrderException.code", "existsSortOrderException.msg"),
    VERIFICATION_MAIL_FAILED_EXCEPTION("verificationMailFailedException.code", "verificationMailFailedException.msg"),;

    private final String code;
    private final String message;

    ExceptionType(String code, String message)  {
        this.code = code;
        this.message = message;
    }
}
