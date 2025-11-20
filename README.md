# 프로젝트 F

## 서버 구성 정보
- 웹서버 및 게임 서버로 분리

### 웹 서버
- java spring-boot 활용
- Redis & MySQL 연동
- JWT token 활용한 OAuth 2.0 Bearer 활용
- DB 정보는 환경변수 의존성 주입 => GitHub 정책 위반

### 참고 서버
- MySQL Server: https://console.aiven.io/
- Redis Server: https://console.upstash.com/