package com.sqld_board.sqld.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    MAIL_SEND_SUCCESS("success.mail.code", "success.mail.msg"),
    SIGNUP_SUCCESS("success.signup.code", "success.signup.msg"),
    CHANGE_PASSWORD_SUCCESS("success.change.password.code", "success.change.password.msg"),
    IMAGE_UPDATE_SUCCESS("success.image.update.code", "success.image.update.msg"),
    VERIFICATION_MAIL_SUCCESS("success.Verification.email.code", "success.Verification.email.msg"),
    DELETE_MEMBER_SUCCESS("success.deleteMember.code", "success.deleteMember.msg"),
    AVAILABLE_USER_ID("success.available.user.id.code", "success.available.user.id.msg"),
    AVAILABLE_USER_NAME("success.available.user.name.code", "success.available.user.name.msg"),
    ADMIN_KICK_SUCCESS("success.admin.kick.code", "success.admin.kick.msg"),
    ADMIN_PROMOTED_SUCCESS("success.admin.promoted.code", "success.admin.promoted.msg"),
    ADMIN_DEMOTED_SUCCESS("success.admin.demoted.code", "success.admin.demoted.msg"),
    DELETE_CONTENT_Y("success.deleteContentY.code", "success.deleteContentY.msg"),
    CREATE_COMMENT("success.createComment.code","success.createComment.msg"),
    IsLiked_Content("success.isLiked.code", "success.isLiked.msg"),
    IsNotLiked_Content("success.isNotLiked.code", "success.isNotLiked.msg"),
    Create_Board_Content("success.createBoardContent.code", "success.createBoardContent.msg"),
    View_Increment_Success("board.view.increment.success.code", "board.view.increment.success.msg"),
    BOARD_SCRAP_INSERT_SUCCESS("board.scrap.insert.success.code", "board.scrap.insert.success.msg"),
    BOARD_SCRAP_DELETE_SUCCESS("board.scrap.delete.success.code", "board.scrap.delete.success.msg"),
    BOARD_CONTENTS_RESTORE("success.board.contents.restore.code", "success.board.contents.restore.msg"),
    NOTIC_CONTENTS_CHECK_OK("success.notic.contents.check.code","success.notic.contents.check.msg"),
    ADD_GROUP_CODE_SUCCESS("success.add.groupCode.code","success.add.groupCode.msg"),
    UPDATE_GROUP_CODE_SUCCESS("success.update.groupCode.code","success.update.groupCode.msg"),
    ADD_GROUP_CODE_DETAIL_SUCCESS("success.add.groupCode.detail.code","success.add.groupCode.detail.msg"),
    UPDATE_GROUP_CODE_DETAIL_SUCCESS("success.update.groupCode.detail.code","success.update.groupCode.detail.msg"),
    ADD_BOARD_MASTER_SUCCESS("success.add.boardMaster.code","success.add.boardMaster.msg"),
    UPDATE_BOARD_MASTER_SUCCESS("success.update.boardMaster.code","success.update.boardMaster.msg"),
    UPDATE_BOARD_CONTENT_SUCCESS("success.updateBoardContent.code","success.updateBoardContent.msg");




    private final String code;
    private final String msg;

    MessageType(String code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
