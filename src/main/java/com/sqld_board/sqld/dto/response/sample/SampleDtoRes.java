package com.sqld_board.sqld.dto.response.sample;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 샘플(예제) 데이터 조회 시, 서버에서 클라이언트로 응답 데이터를 전달하는 DTO(Data Transfer Object) 클래스입니다.
 */
@Data
@NoArgsConstructor
public class SampleDtoRes {

    private int sampleId;
    private String sampleName;
}
