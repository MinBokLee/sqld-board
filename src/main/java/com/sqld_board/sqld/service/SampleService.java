package com.sqld_board.sqld.service;

import com.sqld_board.sqld.dto.request.SampleDtoReq;
import com.sqld_board.sqld.dto.response.sample.SampleDtoRes;
import com.sqld_board.sqld.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleService {

    private final SampleMapper sampleMapper;

    public List<SampleDtoRes> searchMemberInfo(){
       return sampleMapper.searchMemberInfo();
    }

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
