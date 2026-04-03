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
- **최근 업데이트:** 2026-04-02 (Session_11 완료)
- **주요 성과:** 관리자 휴지통 API, SUPER_ADMIN 기반 회원/권한 관리 시스템 구축, API 응답 표준화(데이터 포함 응답) 완료.
- **상태:** 관리자용 데이터 라이프사이클 관리와 강력한 보안 위계 체계가 완성되었으며, 실시간 서비스로 나아갈 준비가 된 단계.

---

## 🗂️ 세션 로그 인덱스 (Session Logs)
- [Session_01: 시스템 구축](./logs/gemini_history/session_20260325_01.md)
- [Session_02: JSON 에러 수정](./logs/gemini_history/session_20260325_02.md)
- [Session_03: 고급 댓글 삭제](./logs/gemini_history/session_20260325_03.md)
- [Session_04: 다운로드 보안](./logs/gemini_history/session_20260325_04.md)
- [Session_06: 시큐리티 최적화](./logs/gemini_history/session_20260325_06.md)
- [Session_07: 소프트 딜리트 전면 도입 및 일괄 삭제](./logs/gemini_history/session_20260325_07.md)
- [Session_08: API 정비 및 보안 고도화](./logs/gemini_history/session_20260330_01.md)
- [Session_09: 복구/삭제 시스템 및 로드맵 수립](./logs/gemini_history/session_20260331_01.md)
- [Session_10: 마이페이지(내가 쓴 글/스크랩) 페이징 및 검색 기능 고도화](./logs/gemini_history/session_20260401_01.md)
- [Session_11: 관리자 기능 및 보안 위계 고도화(SUPER_ADMIN)](./logs/gemini_history/session_20260402_01.md)

---

## 🚀 다음 할 일 (Todo)
- [x] 전사적 소프트 딜리트(Soft Delete) 시스템 구축
- [x] 게시글 일괄 삭제(Bulk Delete) API 구현
- [x] 상세 조회 시 실시간 스크랩 정보 연동
- [x] Spring Scheduling을 이용한 일괄 물리 삭제(Batch Cleanup) 구현
- [x] 게시글 복구(Restore) API 및 보안 고도화(Step 4)
- [x] 마이페이지(내가 쓴 글/스크랩) 페이징 및 검색 기능 최적화
- [x] [관리자] 휴지통(삭제된 게시글 목록) 조회 API 개발
- [x] [관리자] 권한 위계(SUPER_ADMIN) 및 회원 관리 보안 강화
- [ ] Redis 기반 조회수(View Count) 캐싱 및 지연 업데이트 로직 (환경 구축 포함)
- [ ] WebSocket 기반 실시간 채팅 및 쪽지/알림 시스템 설계
- [ ] 카테고리 동적 관리 시스템(DB 기반) 설계 및 구현
--- End of Context from: GEMINI.md ---
