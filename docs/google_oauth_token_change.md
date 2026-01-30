# Google OAuth2 토큰 획득 테스트 가이드

## 사전 준비

2. **환경 변수 확인**
   - `GOOGLE_CLIENT_ID`: Google Cloud Console에서 발급받은 클라이언트 ID
   - `GOOGLE_CLIENT_SECRET`: Google Cloud Console에서 발급받은 클라이언트 시크릿
   - `FRONTEND_URL`: 리다이렉트 URI (기본값: `http://localhost:3000`)

3. **Google Cloud Console 설정**
   - 승인된 리디렉션 URI에 `http://localhost:3000` (또는 설정한 `FRONTEND_URL`)이 등록되어 있어야 합니다.

## 테스트 단계

### 1단계: 브라우저에서 로그인 (세션 생성)

먼저 일반 Google 로그인을 통해 세션을 생성해야 합니다.

1. 브라우저에서 다음 URL로 이동:
   ```
   http://localhost:8080/oauth2/authorization/google
   ```

2. Google 로그인 화면에서 계정 선택 및 로그인
3. 권한 승인 (email, profile 권한)
4. 로그인 완료 후 프론트엔드로 리다이렉트됨

- 또는 다른 방식으로 JSESSIONID 획득

### 2단계: 권한 요청 URL 획득

Postman에서 다음 API를 호출하여 Google Sheets 권한 요청 URL을 받습니다.

**요청:**
```
GET http://localhost:8080/api/projects/google-oauth/authorize-url
```

**응답 예시:**
```json
{
  "authorizeUrl": "https://accounts.google.com/o/oauth2/v2/auth?client_id=...&redirect_uri=...&..."
}
```

### 3단계: 브라우저에서 Google Sheets 권한 승인

1. 2단계에서 받은 `authorizeUrl` 값을 복사
2. 브라우저 주소창에 붙여넣고 이동
3. Google 권한 승인 화면에서 "허용" 클릭
4. Google이 `redirectUri`로 리다이렉트하며 URL에 `code` 파라미터를 포함

**예시 리다이렉트 URL:**
```
http://localhost:3000?code=4/0AeanS...&scope=...&authuser=0&prompt=consent
```

**중요:** URL에서 `code` 파라미터 값을 복사해두세요!

### 4단계: Postman으로 토큰 교환

Postman에서 code를 access_token으로 교환합니다.

**요청:**
```
POST http://localhost:8080/api/projects/google-oauth/exchange-code
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "code": "4/0AeanS...",  // 3단계에서 받은 code 값
  "redirectUri": "http://localhost:3000"  // application.yml에 설정한 redirect-uri와 동일해야 함
}
```

**응답 예시:**
```json
{
  "accessToken": "ya29.a0AfH6SMBx...",
  "refreshToken": "1//0gX7Yq...",
  "expiresIn": 3600,
  "tokenType": "Bearer",
  "scope": "https://www.googleapis.com/auth/spreadsheets",
  "email": "user@example.com"
}
```