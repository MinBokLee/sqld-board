package com.sqld_board.sqld.dto.request.sample;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SampleDtoReq {

    private int sampleId;
    private String sampleName;
    // Lombok을 사용하지 않고 직접 추가
    public int getSampleId() {
        return sampleId;
    }

}


