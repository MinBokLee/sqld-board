# GEMINI.md (Master Log)

> **주의:** 이 파일은 프로젝트의 핵심 규칙과 현재 상태를 담고 있는 마스터 가이드입니다. 모든 작업 시 이 파일의 규칙을 최우선으로 준수하십시오.

---

## 🛡️ 사용자 핵심 규칙 (Mandates)
1. **[승인]** 모든 작업은 사용자의 승인하에 진행되어야 함.
2. **[대기]** 질문에 답변 시 임의로 작업을 진행하지 않음.
3. **[보존]** 임의 파일 삭제 금지.
4. **[검수]** 누락 및 영향도 철저 검수.

---

## 📈 현재 진행 상태 (Current Status)
- **최근 업데이트:** 2026-04-29 (Session_21 분석 완료)
- **주요 성과:** 
    - **BoardMaster 매핑 이슈 원인 규명**: `SORT_ORDER` 값이 0으로 매핑되는 이유가 MyBatis 생성자 매핑의 순서 의존성 및 타입 처리 방식에 있음을 분석함.
    - **ClassCastException 진단**: `existsByBoardNameExceptMe` API 호출 시 발생하는 형변환 에러의 원인이 XML `resultType` 오설정임을 확인하고 해결 전략 수립.
    - **RESTful 설계 표준 준수**: 상세 조회 API의 `@PathVariable` 적용 정당성 확보 및 기술 표준 가이드 강화.
- **상태:** 실시간 통신 기능 안정화 이후, 게시판 관리 기능(BoardMaster)의 정교한 데이터 매핑 및 버그 수정 단계에 있음.

---

## 🗂️ 세션 로그 인덱스 (Session Logs)
...
- [Session_21: BoardMaster 매핑 이슈 분석 및 API 설계 표준 확립](./logs/gemini_history/session_20260429_01.md)

---

## 🚀 다음 할 일 (Todo)
- [ ] `BoardMaster.xml` 리턴 타입(`resultType`) 수정 및 매핑 고도화
- [ ] `COMMON_CODE_GROUP` 실제 데이터 값 확인 (SORT_ORDER)
- [ ] 프론트엔드 Axios Interceptor 기반 응답 언래핑(`data.data`) 적용 확인
- [ ] `prod` 환경 데이터베이스 스키마 동기화 (SQL 실행)
- [ ] 잔여 기능 예외 처리 및 응답 규격 통합 마무리
- [x] 실시간 접속자 명단(Presence) 기능 도입 완료
- [x] 통합 데이터 정리 스케줄러(DatabaseCleanupScheduler) 구축 완료
- [x] 공통 코드 조회 API 리스트 반환 및 필터링 개선 완료
