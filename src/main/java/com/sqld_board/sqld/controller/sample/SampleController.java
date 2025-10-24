package com.sqld_board.sqld.controller.sample;

import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.dto.request.sample.SampleDtoReq;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.sample.SampleDtoRes;
import com.sqld_board.sqld.service.sample.SampleService;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService;

    @Operation(summary = "sample 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Response sampleSearch(){

        List<SampleDtoRes> result = sampleService.searchMemberInfo();
        if(result == null || result.isEmpty()){
            return Response.success(MessageConstants.CONTENT_NO, result);
        }
            return Response.success(MessageConstants.OK, result);
    }

    @Operation(summary = "sample 저장")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public Response saveSample(@RequestBody SampleDtoReq req){

        boolean isSaved = sampleService.saveSample(req);

        try{
            if(isSaved) {
                return Response.success(MessageConstants.CREATE_OK);
            } else {
                return Response.failure(MessageConstants.CREATE_FAIL);
            }
        } catch(Exception e){
          // 로깅 또는 예외 핸들링
          return Response.failure(MessageConstants.INTERNAL_SERVER_ERROR); // 500
        }
    }

// try {
//        boolean isSaved = sampleService.saveSample(req);
//
//        if (isSaved) {
//            return Response.success(MessageConstants.CREATE_OK, null);  // 201 + 메시지
//        } else {
//            return Response.failure(MessageConstants.CREATE_FAIL);  // 301 + 실패 메시지
//        }
//
//    } catch (Exception e) {
//        // 로깅 또는 예외 핸들링
//        return Response.failure(MessageConstants.INTERNAL_SERVER_ERROR); // 500
//    }
//}
}
