package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.response.code.CategoryResponse;
import com.sqld_board.sqld.model.code.CommonCodeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommonCodeDetailMapper {

    Boolean validateSortOrderDuplicate(@Param("groupCode") String groupCode
                                      , @Param("codeId") String codeId
                                      , @Param("sortOrder") Integer sortOrder);

    int getMaxSortOrder(@Param("groupCode") String groupCode);

    List<CategoryResponse> getCategoryListByBoardCode(@Param("boardCode") String boardCode);

    /**
     * 그룹 상세 코드 수정
     */
    void updateCommonCodeDetail(CommonCodeDetail commonCodeDetail);

    /**
     * 그룹 코드 상세 전체 리스트 조회
     * @return
     */
    List<CommonCodeDetail> getCommonDetailCode();


    /**
     * 그룹 상세 코드 중복 확인  & 그룹 코드 상세 'groupCode, codeId' 유무 확인
     * @param groupCode
     * @param codeId
     * @return
     */
    Boolean existsCommonDetailCode( @Param("groupCode") String groupCode
                                  , @Param("codeId") String codeId);

    /**
     * 그룹 상세 코드 상세 조회
     * @param groupCode
     * @return
     */
    List<CommonCodeDetail> readDetailCommonDetailCode(@Param("groupCode") String groupCode);

    /**
     * 그룹 상세 코드 등록
     * @param commonCodeDetail
     */
    void addCommonDetailCode(CommonCodeDetail commonCodeDetail);



}
