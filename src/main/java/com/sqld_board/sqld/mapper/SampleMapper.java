package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.request.SampleDtoReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SampleMapper {

    List<SampleDtoReq> searchMemberInfo();
}
