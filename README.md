## 📌 픽션플레이스 (FictionPlace)
- 웹툰 작가와 기업을 연결하는 매칭 플랫폼
- 
## 📢 프로젝트 목표
- 웹툰 작가와 기업 간 매칭
- 웹툰 작가 작품 관리 및 홍보 지원
- 기업회원 인재 발굴 및 채용 지원
- 사용자 간 소통 및 커뮤니티 활성화
- 웹툰 창작 및 채용 정보 공유
- 직관적인 UI/UX 제공   


## 📅 개발 기간
- 2024년 11월 17일 ~ 2024년 12월 20일   

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

- board: 게시판에 대한 정보를 저장하는 테이블.
- board_type: 게시판의 유형을 정의하는 테이블.
- comment: 게시물과 웹툰 에피소드에 달린 댓글을 저장하는 테이블.
- site_user: 플랫폼 사용자(일반회원)의 정보를 저장하는 테이블.
- company_user: 기업 사용자의 정보를 저장하는 테이블.
- episode_image: 웹툰 회차에 해당하는 이미지 파일을 저장하는 테이블.
- favorite: 사용자가 즐겨찾기한 웹툰 정보를 저장하는 테이블.
- genre_type: 웹툰의 장르 유형을 정의하는 테이블.
- message: 사용자 간에 주고받은 메시지를 저장하는 테이블.
- my_profile: 사용자의 프로필 정보를 저장하는 테이블.
- recommend: 웹툰과 게시글에 대한 추천 정보를 저장하는 테이블.
- web_toon: 웹툰 작품에 대한 정보를 저장하는 테이블.
- web_toon_episode: 웹툰의 개별 회차 정보를 저장하는 테이블.


## 📊 E-R 다이어그램

![Frame 2](https://github.com/user-attachments/assets/a26b7273-ae9c-433c-8c08-8790e26c43f7)


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
