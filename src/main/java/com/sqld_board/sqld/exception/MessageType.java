package com.sqld_board.sqld.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    MAIL_SEND_SUCCESS("success.mail.code", "success.mail.msg"),
    DELETE_MEMBER_SUCCESS("success.deleteMember.code", "success.deleteMember.msg"),
    ADMIN_KICK_SUCCESS("success.admin.kick.code", "success.admin.kick.msg"),
    ADMIN_CHANGE_ROLE("success.adminChangeRole.code", "success.adminChangeRole.msg"),
    DELETE_CONTENT_Y("success.deleteContentY.code", "success.deleteContentY.msg"),
    CREATE_COMMENT("success.createComment.code","success.createComment.msg"),
    IsLiked_Content("success.isLiked.code", "success.isLiked.msg"),
    IsNotLiked_Content("success.isNotLiked.code", "success.isNotLiked.msg"),
    Create_Board_Content("success.createBoardContent.code", "success.createBoardContent.msg"),
    View_Increment_Success("board.view.increment.success.code", "board.view.increment.success.msg"),
    BOARD_SCRAP_INSERT_SUCCESS("board.scrap.insert.success.code", "board.scrap.insert.success.msg"),
    BOARD_SCRAP_DELETE_SUCCESS("board.scrap.delete.success.code", "board.scrap.delete.success.msg"),
    BOARD_CONTENTS_RESTORE("board.contents.restore.success.code", "board.contents.restore.success.msg");




    private final String code;
    private final String msg;

    MessageType(String code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
