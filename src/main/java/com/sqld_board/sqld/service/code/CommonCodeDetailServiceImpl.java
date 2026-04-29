package com.sqld_board.sqld.service.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeDetailRequest;
import com.sqld_board.sqld.dto.response.code.CommonCodeDetailResponse;
import com.sqld_board.sqld.exception.code.*;
import com.sqld_board.sqld.mapper.CommonCodeDetailMapper;
import com.sqld_board.sqld.mapper.CommonCodeGroupMapper;
import com.sqld_board.sqld.model.code.CommonCodeDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommonCodeDetailServiceImpl implements CommonCodeDetailService{

    private final CommonCodeGroupMapper commonCodeGroupMapper;
    private final CommonCodeDetailMapper commonCodeDetailMapper;

    /**
     * 그룹 상세 코드 수정
     * @param groupCode
     * @param codeId
     * @param request
     */
    @Override
    public void updateCommonCodeDetail(String groupCode, String codeId, GroupCodeDetailRequest request) {

        validateSortOrder(groupCode, request.getCodeId(), request.getSortOrder());

        // 1. groupCode. codeId 유무 확인
        validateCodeExists(groupCode, codeId);

        CommonCodeDetail updateData = CommonCodeDetail.builder()
                .groupCode(groupCode)
                .codeId(codeId)
                .codeName(request.getCodeName())
                .useYn(request.getUseYn())
                .sortOrder(request.getSortOrder())
                .build();

        commonCodeDetailMapper.updateCommonCodeDetail(updateData);
    }

    /**
     * 그룹 코드 상세 전체 리스트 조회
     * @return
     */
    @Transactional(readOnly = true)
    public List<CommonCodeDetailResponse> getCommonDetailCode(){
        List<CommonCodeDetail> codeDetailList = commonCodeDetailMapper.getCommonDetailCode();

        return codeDetailList.stream()
                .map(CommonCodeDetailResponse::modelToDto)
                .collect(Collectors.toList());
    }

    /**
     * 그룹 상세 코드 상세 조회
     * @param groupCode
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<CommonCodeDetailResponse> readDetailCommonDetailCode(String groupCode) {

        // 1. 그룹코드 상세 조회 & 유효성 검사
        List<CommonCodeDetail> codeDetailData = commonCodeDetailMapper.readDetailCommonDetailCode(groupCode);

        if(codeDetailData.isEmpty()){
            throw new NotFoundGroupDetailCodeException();
        }

        // 2. Model Response DTO List 로 변환
        return codeDetailData.stream()
                            .map(CommonCodeDetailResponse::modelToDto)
                            .collect(Collectors.toList());
    }

    /**
     * 그룹 상세코드 등록
     * @param request
     */
    @Override
    public void addCommonDetailCode(GroupCodeDetailRequest request) {

        validateSortOrder(request.getGroupCode(),null, request.getSortOrder());

        // 1. 그룹 코드 확인
        validateDuplicateGroupCode(request.getGroupCode());

        // 2. 상세 코드 중복 확인
        validateDuplicateDetailCode(request.getGroupCode(),request.getCodeId());

        // 4. Builder
        CommonCodeDetail codeDetailInfo = CommonCodeDetail.builder()
                                                  .groupCode(request.getGroupCode())
                                                  .codeId(request.getCodeId())
                                                  .codeName(request.getCodeName())
                                                  .sortOrder(request.getSortOrder())
                                                  .useYn(request.getUseYn() != null ? request.getUseYn() : "Y")
                                                  .build();

        // 4. DB 저장
        commonCodeDetailMapper.addCommonDetailCode(codeDetailInfo);
    }

    /* ========================================================================= */
    /*                          private method */
    /* ========================================================================= */


    /**
     * 그룹 코드 정렬순서 중복 및 음수 방지 체크
     * @param groupCode
     * @param codeId
     * @param requestSortOrder
     */
    private void validateSortOrder(String groupCode, String codeId,Integer requestSortOrder) {
        if(requestSortOrder == null){
            return;
        }

        // 1. 상세코드 sortOrder  범위 확인
        int currentMax = commonCodeDetailMapper.getMaxSortOrder(groupCode);

        if(requestSortOrder > (currentMax +1)){
            throw new InvalidSortOrderException(currentMax+1);
        }

        // 2. 상세코드 중복 체크
        if(commonCodeDetailMapper.validateSortOrderDuplicate(groupCode, codeId, requestSortOrder)){
            throw new ExistsSortOrderException(); //이미 사용 중인 순서입니다.
        }
    }

    /**
     * 그룹 코드 상세 'groupCode, codeId' 유무 확인
     * @param groupCode
     * @param codeId
     */
    private void  validateCodeExists(String groupCode, String codeId) {

        if(!commonCodeDetailMapper.existsCommonDetailCode(groupCode, codeId)){
            throw new NotFoundGroupDetailCodeException();
        }
    }

    /**
     * 그룹 코드 존재 유무 확인
     * 데이터가 있으면 -> true
     * 데이터가 없으면 -> false
     * @param groupCode
     */
    private void validateDuplicateGroupCode(String groupCode){
        if(!commonCodeGroupMapper.existGroupCode(groupCode)){
            throw new NotFoundGroupCodeException();
        }
    }

    /**
     * 그룹 코드 상세 ID 중복 조회
     * @param groupCode
     * @param codeId
     */
    private void validateDuplicateDetailCode(String groupCode, String codeId){
        if(commonCodeDetailMapper.existsCommonDetailCode(groupCode,codeId)){
            throw new ExistsCommonGroupDetailCodeException();
        }


    }
}
