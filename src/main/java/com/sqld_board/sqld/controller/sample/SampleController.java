package com.sqld_board.sqld.controller.sample;

import com.sqld_board.sqld.dto.request.SampleDtoReq;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.sqld_board.sqld.dto.response.Response.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Response sampleSearch(){
            List<SampleDtoReq> result = sampleService.searchMemberInfo();
        return success(200, result);
    }
}
