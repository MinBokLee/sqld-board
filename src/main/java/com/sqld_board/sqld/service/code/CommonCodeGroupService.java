package com.sqld_board.sqld.service.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeRequest;
import com.sqld_board.sqld.dto.response.code.CommonCodeGroupResponse;

import java.util.List;

public interface CommonCodeGroupService {

    void updateGroupCode(String groupCode, GroupCodeRequest groupCodeRequest);

    /**
     *  그룹코드 상세 조회
     * @param groupCode
     * @return
     */
    CommonCodeGroupResponse readDetailGroupCode(String groupCode);

    /**
     *  그룹코드 목록 조회
     * @return
     */
     List<CommonCodeGroupResponse> getGroupCodeList();

    /**
     * 그룹 코드 등록
     * @param groupCodeRequest
     */
    void addGroupCode(GroupCodeRequest groupCodeRequest);


}
