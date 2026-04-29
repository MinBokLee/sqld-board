package com.sqld_board.sqld.controller.sample;

import com.sqld_board.sqld.dto.request.sample.SampleDtoReq;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.sample.SampleDtoRes;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.sample.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 샘플(예제) 관련 API 엔드포인트를 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService;
    private final ResponseHandler responseHandler;

    /**
     * 샘플 데이터를 조회합니다.
     * @return 조회된 샘플 데이터 리스트를 포함하는 응답 객체
     */
    @Operation(summary = "sample 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Response sampleSearch(){

        List<SampleDtoRes> sampleData = sampleService.searchMemberInfo();

            return Response.success(sampleData);
    }

    /**
     * 새로운 샘플 데이터를 저장합니다.
     * @param req 저장할 샘플 데이터를 담은 요청 DTO
     * @return 처리 결과를 담은 응답 객체
     */
    @Operation(summary = "sample 저장")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public Response saveSample(@RequestBody SampleDtoReq req){

        sampleService.saveSample(req);

        return responseHandler.getSuccessResponse(MessageType.ADMIN_DEMOTED_SUCCESS);
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
