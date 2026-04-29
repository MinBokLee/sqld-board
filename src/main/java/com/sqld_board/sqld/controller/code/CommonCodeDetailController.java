package com.sqld_board.sqld.controller.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeDetailRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.code.CommonCodeDetailResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.code.CommonCodeDetailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
@RequiredArgsConstructor
@RequestMapping("api/common-code-group-detail")
public class CommonCodeDetailController {

    private final CommonCodeDetailService commonCodeDetailService;
    private final ResponseHandler responseHandler;

    @Operation(summary = "그룹 코드 상세 수정")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateCommonCodeDetail/{groupCode}/{codeId}")
    public Response updateCommonCodeDetail( @PathVariable String groupCode
                                                           ,@PathVariable String codeId
                                                           ,@RequestBody GroupCodeDetailRequest request){
        commonCodeDetailService.updateCommonCodeDetail(groupCode, codeId, request);

        return responseHandler.getSuccessResponse(MessageType.UPDATE_GROUP_CODE_DETAIL_SUCCESS);
    }

    @Operation(summary = "그룹 코드 상세 전체 리스트 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllCommonDetailCode")
    public Response getCommonDetailCode(){
        List<CommonCodeDetailResponse> detailCodeList  = commonCodeDetailService.getCommonDetailCode();

        return Response.success(detailCodeList);
    }


    @Operation(summary = "그룹 상세 코드 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/readDetailCommonDetailCode/")
    public Response readDetailCommonDetailCode(@RequestParam String groupCode){
        List<CommonCodeDetailResponse>  codeDetailData = commonCodeDetailService.readDetailCommonDetailCode(groupCode);

        return Response.success(codeDetailData);
    }

    @Operation(summary = "그룹 상세 코드 등록")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("addCommonDetailCode")
    public Response addCommonDetailCode(@RequestBody GroupCodeDetailRequest request){
        commonCodeDetailService.addCommonDetailCode(request);

        return responseHandler.getSuccessResponse(MessageType.ADD_GROUP_CODE_DETAIL_SUCCESS);
    }
}

