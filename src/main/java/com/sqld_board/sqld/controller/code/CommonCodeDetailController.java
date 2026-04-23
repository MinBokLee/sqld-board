package com.sqld_board.sqld.controller.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeDetailRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.code.CommonCodeDetailResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.service.code.CommonCodeDetailService;
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

    /**
     * 그룹 코드 상세 수정
     * @param groupCode
     * @param codeId
     * @param request
     * @return
     */
    @PatchMapping("/updateCommonCodeDetail/{groupCode}/{codeId}")
    public ResponseEntity<Response> updateCommonCodeDetail( @PathVariable String groupCode
                                                                          , @PathVariable String codeId
                                                                          , @RequestBody GroupCodeDetailRequest request){

        commonCodeDetailService.updateCommonCodeDetail(groupCode, codeId, request);

        return ResponseEntity.ok(Response.success(MessageType.UPDATE_GROUP_CODE_DETAIL_SUCCESS));
    }

    /**
     * 그룹 코드 상세 전체 리스트 조회
     * @return
     */
    @GetMapping("/getCommonDetailCode")
    public ResponseEntity<Response> getCommonDetailCode(){
        List<CommonCodeDetailResponse> detailCodeList  = commonCodeDetailService.getCommonDetailCode();

        return ResponseEntity.ok(Response.success(detailCodeList));
    }

    /**
     * 그룹 상세 조회
     * @param groupCode
     * @return
     */
    @GetMapping("/readDetailCommonDetailCode/{groupCode}")
    public ResponseEntity<Response> readDetailCommonDetailCode(@PathVariable String groupCode){

        CommonCodeDetailResponse  codeDetailData = commonCodeDetailService.readDetailCommonDetailCode(groupCode);

        return ResponseEntity.ok(Response.success(codeDetailData));
    }


    /**
     * 그룹 상세 코드 등록
     */
    @PostMapping("addCommonDetailCode")
    public ResponseEntity<Response> addCommonDetailCode(@RequestBody GroupCodeDetailRequest request){
        commonCodeDetailService.addCommonDetailCode(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Response.success(MessageType.ADD_GROUP_CODE_DETAIL_SUCCESS));
    }
}

