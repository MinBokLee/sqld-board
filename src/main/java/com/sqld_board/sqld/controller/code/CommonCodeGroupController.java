package com.sqld_board.sqld.controller.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.code.CommonCodeGroupResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.code.CommonCodeGroupService;
import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "그룹코드 수정")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateGroupCode/{groupCode}")
    public Response updateGroupCode(@PathVariable String groupCode,
                                    @RequestBody GroupCodeRequest groupCodeRequest){
        commonCodeGroupService.updateGroupCode(groupCode, groupCodeRequest);

        return responseHandler.getSuccessResponse(MessageType.UPDATE_GROUP_CODE_SUCCESS);
    }

    @Operation(summary = "그룹코드 상세조회")
    @GetMapping("/readDetailGroupCode/{groupCode}")
    public Response readDetailGroupCode(@PathVariable String groupCode){

        CommonCodeGroupResponse groupCodeDetailData = commonCodeGroupService.readDetailGroupCode(groupCode);

        return Response.success(groupCodeDetailData);
    }

    @Operation(summary = "그룹코드 목록 조회")
    @GetMapping("/getGroupCodeList")
    public Response getGroupCodeList(){
      List<CommonCodeGroupResponse> groupCodeList = commonCodeGroupService.getGroupCodeList();

      return Response.success(groupCodeList);
    }


    @Operation(summary = "그룹코드를 등록한다.")
    @PostMapping("/addGroupCode")
    @ResponseStatus(HttpStatus.CREATED)
    public Response addGroupCode(@RequestBody GroupCodeRequest groupCodeRequest){
        commonCodeGroupService.addGroupCode(groupCodeRequest);

        return responseHandler.getSuccessResponse(MessageType.ADD_GROUP_CODE_SUCCESS);
    }
}
