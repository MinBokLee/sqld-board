package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.code.CommonCodeGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommonCodeGroupMapper {

    /**
     * 정렬 순서 중복 체크
     * @return
     */
    Boolean validateSortOrderDuplicate(@Param("groupCode") String groupCode
                                      ,@Param("sortOrder") int sortOrder);

    /**
     * 전체 그룹 중 최대 정렬 순서 조회
     * @return
     */
    int getMaxSortOrder();

    /**
     * 그룹코드 수정
     * @param commonCodeGroup
     */
    void updateGroupCode(CommonCodeGroup commonCodeGroup);

    /**
     * 그룹 코드 상세 조회
     * @param groupCode
     * @return
     */
    Optional<CommonCodeGroup> readDetailGroupCode(@Param("groupCode") String groupCode);

    /**
     * 그룹 코드 리스트 조회
      * @return
     */
    List<CommonCodeGroup> getGroupCodeList();

    /**
     * 그룹 코드 중복 조회
     * @param groupCode
     * @return
     */
    Boolean existGroupCode(@Param("groupCode") String groupCode);

    /**
     * 그룹 코드 등록
     * @param commonCodeGroup
     */
    void addGroupCode(CommonCodeGroup commonCodeGroup);
}
