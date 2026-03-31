package com.sqld_board.sqld.controller.admin;

import com.sqld_board.sqld.dto.request.sign.MemberBulkDeleteReq;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.admin.MemberResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.admin.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;
    private final ResponseHandler responseHandler;


    @Operation(summary = "게시글 복구")
    @PutMapping("/restore/{boardId}")
    public ResponseEntity<Response> restore(@PathVariable long boardId) {
        adminService.restore(boardId);

        return ResponseEntity.ok(responseHandler.getSuccessResponse(MessageType.BOARD_CONTENTS_RESTORE));
    }

    /**
     * 3. 사용자 ->운영자로 Role 변경
     * @param memberId
     * @return
     */
    @Operation(summary = "사용자 -> 운영자로 Role 변경")
    @PatchMapping("/changeRoleAdmin/{memberId}")
    public ResponseEntity<Response> changeRoleAdmin(@PathVariable String memberId){
        adminService.changeRoleAdmin(memberId);
        return ResponseEntity.ok(responseHandler.getSuccessResponse(MessageType.ADMIN_CHANGE_ROLE));
    }
    /**
     * 2. 회원 일괄 삭제
     * DELETE 메소드에 @RequestBody를 사용하는 방식은, 최신 브라우저와 라이브러리(Axios 등)에서 잘 작동하지만,
     * 아주 오래된 환경에서는 차단될 수 있다. post 방식을 대안으로 고려할 수 있다.
     * @param memberBulkDeleteReq
     * @return
     */
    @Operation(summary = "회원 일괄 삭제")
    @PostMapping("/deleteMembersByAdmin")
    public ResponseEntity<Response> deleteMembersByAdmin(@RequestBody MemberBulkDeleteReq memberBulkDeleteReq){
        List<String> userIds = memberBulkDeleteReq.getUserIds();
        MessageType result = adminService.deleteMembersByAdmin(userIds);
        return ResponseEntity.ok(Response.success(result));
    }

    /**
     * 1. 회원 정보를 확인한다.
     * @return
     */
    @Operation(summary = "회원 리스트 확인")
    @GetMapping("/getMemberList")
    public ResponseEntity<Response> getMemberList(){
        List<MemberResponse> memberList = adminService.getMemberList();
        return ResponseEntity.ok(Response.success(memberList));
    }


}
