package com.sqld_board.sqld.dto.request.sample;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 샘플(예제) 데이터의 생성 및 수정을 위한 요청 데이터를 전달하는 DTO(Data Transfer Object) 클래스입니다.
 */
@Data
@NoArgsConstructor
public class SampleDtoReq {

    private int sampleId;
    private String sampleName;

}


