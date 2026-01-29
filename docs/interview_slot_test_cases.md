# 면접 슬롯 생성 로직 테스트 케이스

## 전제 조건
- 프로젝트 ID: 1
- 면접관 2명: adminId 1, adminId 2
- 테스트 날짜: 2024-08-01

---

## 테스트 케이스 1: 기본 연속 블록 (20분 슬롯, 5분 브레이크)

### 1-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "10:00",
  "endTime": "12:00",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 20,
  "slotBreakMin": 5
}
```

### 1-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30", "11:00"]
    }
  ]
}
```
**설명**: 면접관 1은 10:00~11:30 연속 범위 선택

### 1-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30"]
    }
  ]
}
```
**설명**: 면접관 2는 10:00~11:00 연속 범위 선택

### 1-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 1-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:20",
          "availableInterviewerCount": 2
        },
        {
          "startTime": "10:25",
          "endTime": "10:45",
          "availableInterviewerCount": 2
        },
        {
          "startTime": "10:50",
          "endTime": "11:10",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "11:15",
          "endTime": "11:35",
          "availableInterviewerCount": 1
        }
      ]
    }
  ]
}
```
**설명**: 
- 10:00~10:20: 면접관 1,2 모두 가능 (블록 10:00~10:30에 완전히 포함)
- 10:25~10:45: 면접관 1,2 모두 가능 (블록 10:00~10:30에 완전히 포함)
- 10:50~11:10: 면접관 1만 가능 (면접관 2는 11:00까지만)
- 11:15~11:35: 면접관 1만 가능

---

## 테스트 케이스 2: 불연속 블록 (30분 슬롯, 0분 브레이크)

### 2-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "09:00",
  "endTime": "15:00",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 30,
  "slotBreakMin": 0
}
```

### 2-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30", "14:00", "14:30"]
    }
  ]
}
```
**설명**: 면접관 1은 10:00~11:00, 14:00~15:00 두 개의 연속 범위 선택

### 2-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "11:00", "14:00"]
    }
  ]
}
```
**설명**: 면접관 2는 10:00~11:30, 14:00~14:30 두 개의 연속 범위 선택

### 2-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 2-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:30",
          "availableInterviewerCount": 2
        },
        {
          "startTime": "10:30",
          "endTime": "11:00",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "14:00",
          "endTime": "14:30",
          "availableInterviewerCount": 2
        }
      ]
    }
  ]
}
```
**설명**:
- 10:00~10:30: 면접관 1,2 모두 가능
- 10:30~11:00: 면접관 1만 가능 (면접관 2는 11:00 블록만 있어서 10:30~11:00 슬롯에 완전히 포함되지 않음)
- 14:00~14:30: 면접관 1,2 모두 가능

---

## 테스트 케이스 3: 슬롯이 블록 경계를 넘는 경우 (15분 슬롯, 5분 브레이크)

### 3-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "10:00",
  "endTime": "11:30",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 15,
  "slotBreakMin": 5
}
```

### 3-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30"]
    }
  ]
}
```
**설명**: 면접관 1은 10:00~11:00 연속 범위 선택

### 3-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:15", "10:45"]
    }
  ]
}
```
**설명**: 면접관 2는 10:15~11:15 연속 범위 선택 (10:15 블록은 10:00~10:30 범위에 포함되지 않음)

### 3-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 3-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:15",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "10:20",
          "endTime": "10:35",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "10:40",
          "endTime": "10:55",
          "availableInterviewerCount": 1
        }
      ]
    }
  ]
}
```
**설명**:
- 10:00~10:15: 면접관 1만 가능 (면접관 2는 10:15 블록이지만, 슬롯이 10:00~10:30 블록에 완전히 포함되지 않음)
- 10:20~10:35: 면접관 1만 가능 (면접관 2의 10:15 블록에 완전히 포함되지 않음)
- 10:40~10:55: 면접관 1만 가능 (면접관 2의 10:45 블록에 완전히 포함되지 않음)

---

## 테스트 케이스 4: 면접 시간 범위 밖의 블록 (필터링 테스트)

### 4-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "10:00",
  "endTime": "11:00",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 20,
  "slotBreakMin": 5
}
```

### 4-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["09:30", "10:00", "10:30", "11:00", "11:30"]
    }
  ]
}
```
**설명**: 면접관 1은 09:30~12:00 연속 범위 선택 (면접 시간 10:00~11:00과 교집합)

### 4-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30"]
    }
  ]
}
```

### 4-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 4-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:20",
          "availableInterviewerCount": 2
        },
        {
          "startTime": "10:25",
          "endTime": "10:45",
          "availableInterviewerCount": 2
        }
      ]
    }
  ]
}
```
**설명**: 
- 09:30 블록은 면접 시간(10:00) 이전이므로 제외
- 11:00 블록은 슬롯 종료 시간(11:20)이 면접 종료 시간(11:00)을 넘으므로 제외
- 11:30 블록은 면접 시간 범위 밖이므로 제외

---

## 테스트 케이스 5: 겹치지 않는 블록 (면접관 1명만 가능한 슬롯)

### 5-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "10:00",
  "endTime": "12:00",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 20,
  "slotBreakMin": 10
}
```

### 5-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30"]
    }
  ]
}
```

### 5-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["11:00", "11:30"]
    }
  ]
}
```
**설명**: 면접관 2는 면접관 1과 다른 시간대 선택

### 5-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 5-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:20",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "10:30",
          "endTime": "10:50",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "11:00",
          "endTime": "11:20",
          "availableInterviewerCount": 1
        },
        {
          "startTime": "11:30",
          "endTime": "11:50",
          "availableInterviewerCount": 1
        }
      ]
    }
  ]
}
```
**설명**: 각 슬롯은 면접관 1명만 가능 (겹치지 않는 시간대)

---

## 테스트 케이스 6: 복잡한 패턴 (40분 슬롯, 10분 브레이크)

### 6-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "09:00",
  "endTime": "13:00",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 40,
  "slotBreakMin": 10
}
```

### 6-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30", "11:00", "11:30", "12:00"]
    }
  ]
}
```
**설명**: 면접관 1은 10:00~12:30 연속 범위 선택

### 6-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00", "10:30", "11:00"]
    }
  ]
}
```
**설명**: 면접관 2는 10:00~11:30 연속 범위 선택

### 6-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 6-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:40",
          "availableInterviewerCount": 2
        },
        {
          "startTime": "10:50",
          "endTime": "11:30",
          "availableInterviewerCount": 2
        },
        {
          "startTime": "11:40",
          "endTime": "12:20",
          "availableInterviewerCount": 1
        }
      ]
    }
  ]
}
```
**설명**:
- 10:00~10:40: 면접관 1,2 모두 가능 (10:00~10:30, 10:30~11:00 블록에 완전히 포함)
- 10:50~11:30: 면접관 1,2 모두 가능 (10:30~11:00, 11:00~11:30 블록에 완전히 포함)
- 11:40~12:20: 면접관 1만 가능 (면접관 2는 11:30까지만)

---

## 테스트 케이스 7: 슬롯이 블록에 완전히 포함되지 않는 엣지 케이스

### 7-1. 면접 정보 설정
```http
PUT /api/recruiting/projects/1/interview-setting
Content-Type: application/json

{
  "startDate": "2024-08-01",
  "endDate": "2024-08-01",
  "startTime": "10:00",
  "endTime": "11:00",
  "maxApplicantsPerSlot": 3,
  "minInterviewersPerSlot": 1,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 25,
  "slotBreakMin": 5
}
```

### 7-2. 면접관 1 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/1/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00"]
    }
  ]
}
```
**설명**: 면접관 1은 10:00~10:30 블록만 선택

### 7-3. 면접관 2 가능 시간 등록
```http
PUT /api/recruiting/projects/1/interviewers/2/availability
Content-Type: application/json

{
  "availabilities": [
    {
      "date": "2024-08-01",
      "startTimes": ["10:00"]
    }
  ]
}
```

### 7-4. 슬롯 목록 조회
```http
GET /api/recruiting/projects/1/interview-slots
```

### 7-5. 예상 결과
```json
{
  "summaries": [
    {
      "date": "2024-08-01",
      "slots": [
        {
          "startTime": "10:00",
          "endTime": "10:25",
          "availableInterviewerCount": 2
        }
      ]
    }
  ]
}
```
**설명**: 
- 10:00~10:25 슬롯만 생성됨 (10:00~10:30 블록에 완전히 포함)
- 10:30~10:55 슬롯은 생성되지 않음 (10:00~10:30 블록에 완전히 포함되지 않음)

---

## 검증 포인트

각 테스트 케이스에서 확인해야 할 사항:

1. **연속 범위 그룹화**: 연속된 블록들이 하나의 범위로 묶이는지 확인
2. **면접 시간 범위 필터링**: 면접 시간 범위 밖의 블록이 제외되는지 확인
3. **슬롯 생성 규칙**: slotDurationMin + slotBreakMin 간격으로 슬롯이 생성되는지 확인
4. **면접관 카운트**: 슬롯이 블록에 완전히 포함되어야만 면접관이 카운트되는지 확인
5. **경계 케이스**: 슬롯이 블록 경계를 넘는 경우 올바르게 처리되는지 확인
