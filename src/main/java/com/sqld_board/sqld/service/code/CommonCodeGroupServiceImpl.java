package com.sqld_board.sqld.service.code;

import com.sqld_board.sqld.dto.request.code.GroupCodeRequest;
import com.sqld_board.sqld.dto.response.code.CommonCodeGroupResponse;
import com.sqld_board.sqld.exception.code.ExistsCommonGroupCodeException;
import com.sqld_board.sqld.exception.code.ExistsSortOrderException;
import com.sqld_board.sqld.exception.code.InvalidSortOrderException;
import com.sqld_board.sqld.exception.code.NotFoundGroupCodeException;
import com.sqld_board.sqld.mapper.CommonCodeGroupMapper;
import com.sqld_board.sqld.model.code.CommonCodeGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommonCodeGroupServiceImpl implements CommonCodeGroupService {

    private final CommonCodeGroupMapper commonCodeGroupMapper;

    @Override
    public void updateGroupCode(String groupCode, GroupCodeRequest request) {
        // 1. 그룹코드 존재유무 확인
        validateGroupCodeExists(groupCode);

        // 2. validateSortOrder
        validateSortOrder(groupCode, request.getSortOrder());

        // 2. dto -> model로 변환
        CommonCodeGroup groupCodeInfo = CommonCodeGroup.builder()
                                                       .groupCode(groupCode)
                                                       .groupName(request.getGroupName())
                                                       .useYn(request.getUseYn())
                                                       .sortOrder(request.getSortOrder())
                                                       .build();
        // 3. DB 업데이트
        commonCodeGroupMapper.updateGroupCode(groupCodeInfo);

    }

    @Override
    public CommonCodeGroupResponse readDetailGroupCode(String groupCode) {
        // 1. 조회 및 예외 처리.
        CommonCodeGroup groupData = commonCodeGroupMapper.readDetailGroupCode(groupCode)
                                                         .orElseThrow(NotFoundGroupCodeException::new);

        // 2. Model Response DTO 로 변환
        return CommonCodeGroupResponse.modelToDto(groupData);

    }

    /**
     * 그룹 코드 목록 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<CommonCodeGroupResponse> getGroupCodeList() {
        List<CommonCodeGroup> groupCodeList = commonCodeGroupMapper.getGroupCodeList();

        return groupCodeList.stream()
                .map(CommonCodeGroupResponse::modelToDto)
                .collect(Collectors.toList());
    }

    /**
     * 공통 그룹코드를 추가한다.
     *
     * @param request
     */
    @Override
    public void addGroupCode(GroupCodeRequest request) {

        // 1. 그룹코드 중복 검증
        validateDuplicateGroupCode(request.getGroupCode());

        // 2. 정렬 순서 검증 추가
        validateSortOrder(null, request.getSortOrder());

        // 3. DTO 정보를 Model 객체로 변환
        CommonCodeGroup commonCodeGroup = CommonCodeGroup.builder()
                                                         .groupCode(request.getGroupCode())
                                                         .groupName(request.getGroupName())
                                                         .useYn(request.getUseYn() != null ? request.getUseYn() : "Y") // 기본값 처리
                                                         .sortOrder(request.getSortOrder())
                                                         .build();

        commonCodeGroupMapper.addGroupCode(commonCodeGroup);
    }


    /* ========================================================================= */
    /*                          private method */
    /* ========================================================================= */



    /**
     * 그룹 코드 정렬순서 중복 및 음수 방지 체크
     * @param groupCode
     * @param requestSortOrder
     */
    private void validateSortOrder(String groupCode, Integer requestSortOrder) {
        if(requestSortOrder == null){
            return;
        }

        // 1. 상세코드 sortOrder  범위 확인
        int currentMax = commonCodeGroupMapper.getMaxSortOrder();

        if(requestSortOrder > (currentMax +1)){
            throw new InvalidSortOrderException(currentMax+1);
        }

        // 2. 상세코드 중복 체크
        if(commonCodeGroupMapper.validateSortOrderDuplicate(groupCode, requestSortOrder)){
            throw new ExistsSortOrderException(); //이미 사용 중인 순서입니다.
        }
    }

    /**
     * 그룹 코드 중복 여부를 검증한다. 중복 시 예외 발생
     * @param groupCode
     */
    private void validateDuplicateGroupCode(String groupCode) {
        if (commonCodeGroupMapper.existGroupCode(groupCode)) {
            throw new ExistsCommonGroupCodeException();
        }
    }

    /**
     *그룹 코드 존재 유무 확인
     * @param groupCode
     */
    private void validateGroupCodeExists(String groupCode) {
        if(!commonCodeGroupMapper.existGroupCode(groupCode)){
            throw new NotFoundGroupCodeException();
        }
    }
}
