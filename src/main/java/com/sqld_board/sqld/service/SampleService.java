package com.sqld_board.sqld.service;

import com.sqld_board.sqld.dto.request.SampleDtoReq;
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

    public List<SampleDtoReq> searchMemberInfo(){
       return sampleMapper.searchMemberInfo();
    }
}
