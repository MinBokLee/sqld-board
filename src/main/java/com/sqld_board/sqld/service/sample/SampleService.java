package com.sqld_board.sqld.service.sample;

import com.sqld_board.sqld.dto.request.sample.SampleDtoReq;
import com.sqld_board.sqld.dto.response.sample.SampleDtoRes;
import com.sqld_board.sqld.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 샘플(예제) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 컨트롤러와 매퍼(Mapper) 사이의 중간 계층 역할을 합니다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SampleService {

    private final SampleMapper sampleMapper;

    /**
     * 샘플 데이터 목록을 조회합니다.
     * @return 조회된 샘플 데이터 DTO 리스트
     */
    public List<SampleDtoRes> searchMemberInfo(){
        return sampleMapper.searchMemberInfo();
    }

    /**
     * 새로운 샘플 데이터를 저장합니다.
     * @param req 저장할 샘플 정보를 담은 DTO
     * @return 저장 성공 시 true, 실패 시 false
     */
    public boolean saveSample(SampleDtoReq req) {

        try {
            int result = sampleMapper.saveSample(req);
            return result == 1;
        } catch (Exception e){
            log.error("Sample 저장 실패", e);
        }
        return false;

    }





}
