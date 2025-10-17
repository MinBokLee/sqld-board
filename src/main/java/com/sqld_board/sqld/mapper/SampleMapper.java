package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.request.sample.SampleDtoReq;
import com.sqld_board.sqld.dto.response.sample.SampleDtoRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SampleMapper {

    int saveSample(SampleDtoReq req);

    List<SampleDtoRes> searchMemberInfo();
}
