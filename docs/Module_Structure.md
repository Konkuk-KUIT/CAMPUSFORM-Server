  1. 🆔 Identity Context (신원/인증)
  시스템의 사용자(관리자)를 관리하는 기반 컨텍스트입니다.

   * 📦 User 애그리거트
       * 👑 Root: User
       * VO: email, nickname, profileImageUrl (Primitive Fields)

  ---

  2. 📁 Project Context (프로젝트/모집)
  모집 공고의 설정과 운영 권한을 관리합니다.

   * 📦 Project 애그리거트
       * 👑 Root: Project
       * VO: ProjectState, SyncStatus, sheetUrl, period(startAt, endAt)

   * 📦 ProjectAdmin 애그리거트
       * 👑 Root: ProjectAdmin
       * VO: ProjectRole

   * 📦 GoogleOAuthToken 애그리거트
       * 👑 Root: GoogleOAuthToken

  ---

  3. 📝 Recruiting Context (채용/지원)
  가장 복잡한 비즈니스 로직(지원서, 심사, 면접 스케줄링)이 존재하는 핵심 컨텍스트입니다.

   * 📦 Applicant 애그리거트
       * 👑 Root: Applicant
       * Entity: ApplicantExtraAnswer
       * VO: ApplicantStatus

   * 📦 Comment 애그리거트
       * 👑 Root: Comment
    

   * 📦 Interview (Setup/Availability/Schedule) 애그리거트 군
       * 면접은 여러 객체가 유기적으로 연결되어 있어 하나의 거대한 도메인 영역을 형성합니다.
       * 👑 주요 Roots: InterviewSetting, InterviewScheduledSlot
       * Entity:
           * InterviewDay
           * InterviewerAvailabilityBlock
           * IntervieweeAvailabilitySlot
           * InterviewScheduledSlotApplicant
           * InterviewScheduledSlotInterviewer

  ---

  4. 🔔 Notification Context (알림)
  결과 및 상태 변경 알림을 처리합니다.

   * 📦 Notification 애그리거트
       * 👑 Root: Notification
       * VO: NotificationType, payload(JSON)
