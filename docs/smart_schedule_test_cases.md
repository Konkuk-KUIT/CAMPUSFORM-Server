# 스마트 시간표 알고리즘 테스트 케이스

## 공통 설정

- **프로젝트 ID**: `1`
- **Base URL**: `http://localhost:8080/api/projects/1`

### 면접관 vs 지원자 시간 단위 차이

| 구분 | 시간 단위 | 설명 |
|------|-----------|------|
| **면접관** | 30분 블록 | 고정 30분 단위 (10:00, 10:30, 11:00...) |
| **지원자** | 슬롯 단위 | `slotDurationMin + slotBreakMin` 간격 |

---

## 케이스 1: 기본 배정 (모든 지원자 배정 성공)

### 시나리오
- 면접관 3명, 지원자 4명
- 모든 지원자가 충분한 슬롯에 겹침
- 필수 면접관 없음
- **슬롯 간격: 20분** (15분 면접 + 5분 휴식)

### Step 1: 면접 정보 설정

```http
PUT /api/projects/1/interview-setting
Content-Type: application/json
```

```json
{
  "startDate": "2026-02-02",
  "endDate": "2026-02-03",
  "startTime": "10:00",
  "endTime": "12:00",
  "maxApplicantsPerSlot": 1,
  "minInterviewersPerSlot": 2,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 15,
  "slotBreakMin": 5
}
```

> **슬롯 간격**: 15분 + 5분 = 20분  
> **생성되는 슬롯**: `10:00`, `10:20`, `10:40`, `11:00`, `11:20`, `11:40`

### Step 2: 면접관 가능 시간 설정 (30분 블록 단위)

#### 면접관 1 (adminId: 1)
```http
PUT /api/projects/1/interviewers/1/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-02",
      "startTimes": ["10:00", "10:30", "11:00", "11:30"]
    },
    {
      "date": "2026-02-03",
      "startTimes": ["10:00", "10:30", "11:00"]
    }
  ]
}
```

#### 면접관 2 (adminId: 2)
```http
PUT /api/projects/1/interviewers/2/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-02",
      "startTimes": ["10:00", "10:30", "11:00"]
    },
    {
      "date": "2026-02-03",
      "startTimes": ["10:30", "11:00", "11:30"]
    }
  ]
}
```

#### 면접관 3 (adminId: 3)
```http
PUT /api/projects/1/interviewers/3/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-02",
      "startTimes": ["10:30", "11:00", "11:30"]
    },
    {
      "date": "2026-02-03",
      "startTimes": ["10:00", "10:30", "11:00", "11:30"]
    }
  ]
}
```

### Step 3: 지원자 슬롯 제출 (슬롯 단위: 20분 간격)

> ⚠️ **중요**: 지원자는 슬롯 시작 시간만 제출합니다.  
> 이 케이스의 가능한 슬롯: `10:00`, `10:20`, `10:40`, `11:00`, `11:20`, `11:40`

#### 지원자 1 (token: applicant1-token)
```http
POST /api/interview/public/submit?token=applicant1-token
Content-Type: application/json
```

```json
{
  "name": "김철수",
  "phone": "010-1234-5678",
  "selections": [
    {
      "date": "2026-02-02",
      "startTimes": ["10:00", "10:20", "10:40"]
    }
  ]
}
```

#### 지원자 2 (token: applicant2-token)
```http
POST /api/interview/public/submit?token=applicant2-token
Content-Type: application/json
```

```json
{
  "name": "이영희",
  "phone": "010-2345-6789",
  "selections": [
    {
      "date": "2026-02-02",
      "startTimes": ["10:20", "10:40", "11:00"]
    },
    {
      "date": "2026-02-03",
      "startTimes": ["10:20", "10:40", "11:00"]
    }
  ]
}
```

#### 지원자 3 (token: applicant3-token)
```http
POST /api/interview/public/submit?token=applicant3-token
Content-Type: application/json
```

```json
{
  "name": "박민수",
  "phone": "010-3456-7890",
  "selections": [
    {
      "date": "2026-02-03",
      "startTimes": ["10:00", "10:20", "10:40"]
    }
  ]
}
```

#### 지원자 4 (token: applicant4-token)
```http
POST /api/interview/public/submit?token=applicant4-token
Content-Type: application/json
```

```json
{
  "name": "최지은",
  "phone": "010-4567-8901",
  "selections": [
    {
      "date": "2026-02-02",
      "startTimes": ["11:00", "11:20", "11:40"]
    },
    {
      "date": "2026-02-03",
      "startTimes": ["11:00", "11:20", "11:40"]
    }
  ]
}
```

### Step 4: 스마트 시간표 미리보기

```http
GET /api/projects/1/interview/smart-schedule
```

### Step 5: 스마트 시간표 확정 및 저장

```http
POST /api/projects/1/interview/smart-schedule
```

### 예상 결과
- 4명 모두 배정 성공
- 미배정 지원자: 0명
- 배정률: 100%

---

## 케이스 2: 필수 면접관 포함

### 시나리오
- 면접관 3명 중 1명이 필수 면접관
- 지원자 3명
- 필수 면접관이 없는 슬롯은 무효
- **슬롯 간격: 35분** (30분 면접 + 5분 휴식)

### Step 1: 면접 정보 설정

```http
PUT /api/projects/1/interview-setting
Content-Type: application/json
```

```json
{
  "startDate": "2026-02-05",
  "endDate": "2026-02-05",
  "startTime": "14:00",
  "endTime": "17:00",
  "maxApplicantsPerSlot": 1,
  "minInterviewersPerSlot": 2,
  "maxInterviewersPerSlot": 3,
  "slotDurationMin": 30,
  "slotBreakMin": 5
}
```

> **슬롯 간격**: 30분 + 5분 = 35분  
> **생성되는 슬롯**: `14:00`, `14:35`, `15:10`, `15:45`, `16:20`

### Step 2: 면접관 가능 시간 설정 (30분 블록 단위)

#### 면접관 1 - 필수 (adminId: 1)
```http
PUT /api/projects/1/interviewers/1/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-05",
      "startTimes": ["14:00", "14:30", "15:00"]
    }
  ]
}
```

#### 면접관 2 (adminId: 2)
```http
PUT /api/projects/1/interviewers/2/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-05",
      "startTimes": ["14:00", "14:30", "15:00", "15:30", "16:00", "16:30"]
    }
  ]
}
```

#### 면접관 3 (adminId: 3)
```http
PUT /api/projects/1/interviewers/3/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-05",
      "startTimes": ["14:30", "15:00", "15:30", "16:00", "16:30"]
    }
  ]
}
```

### Step 3: 필수 면접관 설정

```http
PUT /api/projects/1/required-interviewers
Content-Type: application/json
```

```json
{
  "adminIds": [1]
}
```

### Step 4: 지원자 슬롯 제출 (슬롯 단위: 35분 간격)

> ⚠️ **중요**: 가능한 슬롯: `14:00`, `14:35`, `15:10`, `15:45`, `16:20`  
> 필수 면접관 1의 30분 블록: `14:00~14:30`, `14:30~15:00`, `15:00~15:30`  
> 따라서 필수 면접관이 커버하는 슬롯: `14:00` (14:00~14:30 포함)만 유효

#### 지원자 1 (token: applicant1-token)
```http
POST /api/interview/public/submit?token=applicant1-token
Content-Type: application/json
```

```json
{
  "name": "김철수",
  "phone": "010-1234-5678",
  "selections": [
    {
      "date": "2026-02-05",
      "startTimes": ["14:00", "14:35", "15:10"]
    }
  ]
}
```

#### 지원자 2 (token: applicant2-token)
```http
POST /api/interview/public/submit?token=applicant2-token
Content-Type: application/json
```

```json
{
  "name": "이영희",
  "phone": "010-2345-6789",
  "selections": [
    {
      "date": "2026-02-05",
      "startTimes": ["14:35", "15:10", "15:45"]
    }
  ]
}
```

#### 지원자 3 (token: applicant3-token)
```http
POST /api/interview/public/submit?token=applicant3-token
Content-Type: application/json
```

```json
{
  "name": "박민수",
  "phone": "010-3456-7890",
  "selections": [
    {
      "date": "2026-02-05",
      "startTimes": ["15:45", "16:20"]
    }
  ]
}
```

### Step 5: 스마트 시간표 미리보기

```http
GET /api/projects/1/interview/smart-schedule
```

### 예상 결과
- 지원자 1: 배정 성공 (14:00 슬롯 - 필수 면접관 가능)
- 지원자 2, 3: **미배정** (제출한 시간대에 필수 면접관 불가능)
- 미배정 사유: "필수 면접관이 해당 시간에 불가능합니다."

---

## 케이스 3: 슬롯 부족으로 일부 미배정

### 시나리오
- 면접관 2명, 지원자 5명
- 슬롯당 1명만 배정 가능
- 가용 슬롯 수 < 지원자 수
- **슬롯 간격: 25분** (20분 면접 + 5분 휴식)

### Step 1: 면접 정보 설정

```http
PUT /api/projects/1/interview-setting
Content-Type: application/json
```

```json
{
  "startDate": "2026-02-10",
  "endDate": "2026-02-10",
  "startTime": "09:00",
  "endTime": "11:00",
  "maxApplicantsPerSlot": 1,
  "minInterviewersPerSlot": 2,
  "maxInterviewersPerSlot": 2,
  "slotDurationMin": 20,
  "slotBreakMin": 5
}
```

> **슬롯 간격**: 20분 + 5분 = 25분  
> **생성되는 슬롯**: `09:00`, `09:25`, `09:50`, `10:15`, `10:40`

### Step 2: 면접관 가능 시간 설정 (30분 블록 단위)

#### 면접관 1 (adminId: 1)
```http
PUT /api/projects/1/interviewers/1/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:00", "09:30", "10:00", "10:30"]
    }
  ]
}
```

#### 면접관 2 (adminId: 2)
```http
PUT /api/projects/1/interviewers/2/availability
Content-Type: application/json
```

```json
{
  "availabilities": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:00", "09:30", "10:00"]
    }
  ]
}
```

> **유효 슬롯 분석** (면접관 블록 30분 vs 슬롯 25분):  
> - `09:00~09:20`: 면접관 1(09:00블록), 2(09:00블록) 모두 커버 ✅
> - `09:25~09:45`: 면접관 1(09:30블록), 2(09:30블록) 모두 커버 ✅
> - `09:50~10:10`: 면접관 1(10:00블록), 2(10:00블록) 모두 커버 ✅
> - `10:15~10:35`: 면접관 1(10:30블록), 2 불가능 ❌
> - `10:40~11:00`: 면접관 1(10:30블록), 2 불가능 ❌
>
> **유효 슬롯**: 3개 (`09:00`, `09:25`, `09:50`)

### Step 3: 지원자 슬롯 제출 (슬롯 단위: 25분 간격)

> ⚠️ **중요**: 지원자가 선택할 수 있는 슬롯: `09:00`, `09:25`, `09:50`, `10:15`, `10:40`  
> 단, 유효 슬롯은 3개뿐이므로 최대 3명만 배정 가능

#### 지원자 1 (token: applicant1-token)
```http
POST /api/interview/public/submit?token=applicant1-token
Content-Type: application/json
```

```json
{
  "name": "김철수",
  "phone": "010-1111-1111",
  "selections": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:00", "09:25"]
    }
  ]
}
```

#### 지원자 2 (token: applicant2-token)
```http
POST /api/interview/public/submit?token=applicant2-token
Content-Type: application/json
```

```json
{
  "name": "이영희",
  "phone": "010-2222-2222",
  "selections": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:00", "09:25", "09:50"]
    }
  ]
}
```

#### 지원자 3 (token: applicant3-token)
```http
POST /api/interview/public/submit?token=applicant3-token
Content-Type: application/json
```

```json
{
  "name": "박민수",
  "phone": "010-3333-3333",
  "selections": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:25", "09:50"]
    }
  ]
}
```

#### 지원자 4 (token: applicant4-token)
```http
POST /api/interview/public/submit?token=applicant4-token
Content-Type: application/json
```

```json
{
  "name": "최지은",
  "phone": "010-4444-4444",
  "selections": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:50", "10:15"]
    }
  ]
}
```

#### 지원자 5 (token: applicant5-token)
```http
POST /api/interview/public/submit?token=applicant5-token
Content-Type: application/json
```

```json
{
  "name": "정수진",
  "phone": "010-5555-5555",
  "selections": [
    {
      "date": "2026-02-10",
      "startTimes": ["09:00"]
    }
  ]
}
```

### Step 4: 스마트 시간표 미리보기

```http
GET /api/projects/1/interview/smart-schedule
```

### 예상 결과
- **유효 슬롯**: `09:00`, `09:25`, `09:50` (3개)
- **배정**: 3명
- **미배정**: 2명
- 미배정 사유: "모든 슬롯의 정원이 가득찼습니다."

---

## 응답 예시 (예상)

```json
{
  "days": [
    {
      "date": "2026-02-10",
      "slots": [
        {
          "startTime": "09:00",
          "endTime": "09:20",
          "applicants": [
            { "id": 1, "name": "김철수", "school": "서울대", "major": "컴퓨터공학", "position": "백엔드" }
          ],
          "interviewers": [
            { "id": 1, "name": "임형택", "required": false },
            { "id": 2, "name": "박성근", "required": false }
          ]
        },
        {
          "startTime": "09:25",
          "endTime": "09:45",
          "applicants": [
            { "id": 2, "name": "이영희", "school": "연세대", "major": "경영학", "position": "기획" }
          ],
          "interviewers": [
            { "id": 1, "name": "임형택", "required": false },
            { "id": 2, "name": "박성근", "required": false }
          ]
        },
        {
          "startTime": "09:50",
          "endTime": "10:10",
          "applicants": [
            { "id": 3, "name": "박민수", "school": "고려대", "major": "디자인", "position": "디자인" }
          ],
          "interviewers": [
            { "id": 1, "name": "임형택", "required": false },
            { "id": 2, "name": "박성근", "required": false }
          ]
        }
      ]
    }
  ],
  "unassignedApplicants": [
    { "id": 4, "name": "최지은", "school": "한양대", "major": "마케팅", "position": null, "reason": "모든 슬롯의 정원이 가득찼습니다." },
    { "id": 5, "name": "정수진", "school": "성균관대", "major": "통계학", "position": "데이터", "reason": "모든 슬롯의 정원이 가득찼습니다." }
  ],
  "statistics": {
    "totalApplicants": 5,
    "assignedApplicants": 3,
    "unassignedApplicants": 2,
    "usedSlots": 3,
    "assignmentRate": 60.0
  }
}
```

---

## 빠른 테스트 체크리스트

| 케이스 | 면접관 | 지원자 | 필수면접관 | 슬롯 간격 | 유효 슬롯 | 예상 배정률 |
|--------|--------|--------|------------|-----------|-----------|-------------|
| 케이스 1 | 3명 | 4명 | 없음 | **20분** (15+5) | 6+6개 | 100% |
| 케이스 2 | 3명 | 3명 | 1명 | **35분** (30+5) | 1개(필수) | 33.3% (1/3) |
| 케이스 3 | 2명 | 5명 | 없음 | **25분** (20+5) | 3개 | 60% (3/5) |
