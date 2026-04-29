package com.sqld_board.sqld.service.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeDetailRequest;
import com.sqld_board.sqld.dto.response.code.CommonCodeDetailResponse;

import java.util.List;

public interface CommonCodeDetailService {

    /**
     * 그룹 코드 상세 수정
     * @param groupCode
     * @param request
     */
    void updateCommonCodeDetail(String groupCode, String codeId, GroupCodeDetailRequest request);

    /**
     * 그룹 코드 상세 전체 리스트 조회
     * @return
     */
    List<CommonCodeDetailResponse> getCommonDetailCode();

    /**
     * 그룹 상세 조회
     * @param groupCode
     * @return
     */
    List<CommonCodeDetailResponse> readDetailCommonDetailCode(String groupCode);

    /**
     * 그룹 상세코드 추가
     * @param request
     */
    void addCommonDetailCode(GroupCodeDetailRequest request);


}
