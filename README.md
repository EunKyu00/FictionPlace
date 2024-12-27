## 📌 픽션플레이스 (FictionPlace)
웹툰 작가와 기업을 연결하는 매칭 플랫폼

## 📅 개발 기간
2024년 11월 17일 ~ 2024년 12월 20일

## 💻 개발 환경

- **Java**: Java 23 (GraalVM JDK 23 - GraalVM 24.1.1)
- **Spring**: Spring Framework
- **Spring Boot**: Spring Boot 3.3.5
- **MariaDB**
- **JQuery**
- **JPA**
- **HTML / CSS**
- **JavaScript**
- **Thymeleaf**
- **ERDCloud**
- **Figma**
- **Git / GitHub**


## 🛠️ 기술 스택
- **백엔드**
  - **Java 23** (GraalVM JDK 23)
  - **Spring Boot 3.3.5**
  - **JPA (Hibernate)**
  - **Spring Security**
  - **Thymeleaf**

- **프론트엔드**
  - **HTML / CSS**
  - **JavaScript**
  - **JQuery**

- **데이터베이스**
  - **MariaDB**
  - **PostgreSQL**

- **도구**
  - **ERDCloud**
  - **Figma** 
  - **Git / GitHub** 

## 🗂️ DB 테이블 설계
주요 테이블
회원 (siteUser)

일반회원 및 기업회원 정보 관리
웹툰 (webtoon)

웹툰 작품 정보 관리
웹툰 에피소드 (webtoon_episode)

각 웹툰의 회차 정보 관리
게시판 (board)

게시글 및 댓글 관리
쪽지 (message)

회원 간 메시지 송수신


## 📊 E-R 다이어그램
(E-R 다이어그램 이미지 또는 링크 첨부)

## 👥 역할 분담
이름	역할	담당 업무
[이은규]	팀장 / Backend 개발	Spring Boot 기반 백엔드 개발, DB 설계 
[길현수] 팀원 / Frontend 개발	Thymeleaf, HTML, CSS, JavaScript를 사용한 프론트엔드 개발 및 UI/UX 구현

## 🐞 트러블슈팅
1. 사용자 인증 및 세션 관리 문제
문제: Principal 객체가 null 값을 반환
해결 방법: 사용자 정보를 Session을 통해 직접 관리
2. 웹툰 장르별 추천 기능 오류
문제: 장르를 선택한 뒤 추천순 정렬이 제대로 동작하지 않음
해결 방법: WebToonRepository에 findByGenreTypeIdOrderByLikesDesc 메서드 추가로 문제 해결
## 📄 기타 참고사항
프로젝트는 지속적으로 개선 및 업데이트될 예정입니다.
버그나 개선사항이 있다면 이슈로 등록해 주세요.

|제목|내용|설명|
|------|---|---|
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|
