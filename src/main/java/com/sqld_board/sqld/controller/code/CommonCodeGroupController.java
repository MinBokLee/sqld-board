package com.sqld_board.sqld.controller.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.code.CommonCodeGroupResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.code.CommonCodeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
@RestController
@RequestMapping("/api/common-code-group")
public class CommonCodeGroupController {

    private final ResponseHandler responseHandler;
    private final CommonCodeGroupService commonCodeGroupService;


    /**
     * 그룹코드 수정
     * @param groupCode
     * @param groupCodeRequest
     * @return
     */
    @PatchMapping("/updateGroupCode/{groupCode}")
    public ResponseEntity<Response> updateGroupCode(@PathVariable String groupCode, @RequestBody GroupCodeRequest groupCodeRequest){
        commonCodeGroupService.updateGroupCode(groupCode, groupCodeRequest);
        return ResponseEntity.ok(Response.success(MessageType.UPDATE_GROUP_CODE_SUCCESS));
    }

    /**
     * 그룹코드 상세조회
     * @param groupCode
     * @return
     */
    @GetMapping("/readDetailGroupCode/{groupCode}")
    public ResponseEntity<Response> readDetailGroupCode(@PathVariable String groupCode){

        CommonCodeGroupResponse rData = commonCodeGroupService.readDetailGroupCode(groupCode);

        return ResponseEntity.ok(Response.success(rData));
    }

    /**
     * 그룹코드 목록 조회
     * @return
     */
    @GetMapping("/getGroupCodeList")
    public ResponseEntity<Response> getGroupCodeList(){
      List<CommonCodeGroupResponse> groupCodeList = commonCodeGroupService.getGroupCodeList();
      return ResponseEntity.ok(Response.success(groupCodeList));
    }

    /**
     * 그룹코드를 등록한다.
     * @param groupCodeRequest
     * @return
     */
    @PostMapping("/addGroupCode")
    @ResponseStatus(HttpStatus.CREATED)
    public Response addGroupCode(@RequestBody GroupCodeRequest groupCodeRequest){
        commonCodeGroupService.addGroupCode(groupCodeRequest);

        return responseHandler.getSuccessResponse(MessageType.ADD_GROUP_CODE_SUCCESS);
    }
}
