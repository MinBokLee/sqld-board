package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.request.sample.SampleDtoReq;
import com.sqld_board.sqld.dto.response.sample.SampleDtoRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 샘플(예제) 관련 데이터베이스 작업을 위한 MyBatis 매퍼 인터페이스입니다.
 * resources/mapper/Sample.xml 파일과 매핑됩니다.
 */
@Mapper
@Repository
public interface SampleMapper {

    /**
     * 새로운 샘플 데이터를 데이터베이스에 삽입합니다.
     * @param req 삽입할 샘플 데이터를 담은 {@link SampleDtoReq} 객체
     * @return 영향을 받은 행의 수
     */
    int saveSample(SampleDtoReq req);

    /**
     * 샘플 데이터 목록을 조회합니다.
     * @return 조회된 샘플 데이터 리스트
     */
    List<SampleDtoRes> searchMemberInfo();
}
