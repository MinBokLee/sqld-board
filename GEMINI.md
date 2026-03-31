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
- **최근 업데이트:** 2026-03-31 (Session_09 완료)
- **주요 성과:** 게시글 복구(Restore) 및 자동 물리 삭제(Batch Cleanup) 시스템 구축, 관리자 기능 보안 최적화(Step 4) 완료.
- **상태:** 데이터의 생명주기(삭제/복구/영구삭제) 체계가 잡혔으며, 관리자 전용 기능 확장을 위한 보안 기반이 완비된 단계.

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

---

## 🚀 다음 할 일 (Todo)
- [x] 전사적 소프트 딜리트(Soft Delete) 시스템 구축
- [x] 게시글 일괄 삭제(Bulk Delete) API 구현
- [x] 상세 조회 시 실시간 스크랩 정보 연동
- [x] Spring Scheduling을 이용한 일괄 물리 삭제(Batch Cleanup) 구현
- [x] 게시글 복구(Restore) API 및 보안 고도화(Step 4)
- [ ] [관리자] 휴지통(삭제된 게시글 목록) 조회 API 개발
- [ ] 데이터 유예 기간(Retention Period) 설정 및 연동
- [ ] 카테고리 동적 관리 시스템(DB 기반) 설계 및 구현
--- End of Context from: GEMINI.md ---
