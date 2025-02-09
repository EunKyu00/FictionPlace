## 📌 픽션플레이스 (FictionPlace)
- 웹툰 작가와 기업을 연결하는 매칭 플랫폼

**메인페이지**
![메인페이지](https://github.com/user-attachments/assets/e580e5db-a87a-4fc3-8eaa-1afa41aef838) 

## 📅 개발 기간
- 2024년 11월 17일 ~ 2024년 12월 20일 (총 33일)
  
## 📢 프로젝트 목표

- 웹툰 작가와 기업 간 매칭
- 웹툰 작가 작품 관리 및 홍보 지원
- 기업회원 인재 발굴 및 채용 지원
- 사용자 간 소통 및 커뮤니티 활성화
- 웹툰 창작 및 채용 정보 공유
- 직관적인 UI/UX 제공   

## 📌 주요 기능
- **웹툰 작품 등록**
  - 작가가 웹툰 작품을 등록하고, 해당 작품에 대해 제목, 장르, 썸네일, 줄거리 등 기본 정보를 입력할 수 있습니다.
  
- **웹툰 회차 관리** 
  - 작가가 등록한 웹툰의 각 회차를 관리하고, 새로운 회차를 추가하거나 기존 회차를 수정할 수 있습니다.
  
- **관심 작품 등록** 
  - 사용자가 관심 있는 웹툰을 즐겨찾기하여 본인 프로필에 등록하고, 나중에 쉽게 접근할 수 있습니다.
  
- **게시판 및 커뮤니티 기능** 
  - 게시글 작성, 수정, 삭제 및 댓글 기능을 제공하여 사용자가 웹툰 관련 소식이나 정보를 공유하고 소통할 수 있습니다.
  
- **기업 채용 공고 등록** 
  - 기업이 웹툰 작가를 찾기 위해 채용 공고를 게시하고, 이를 통해 적합한 작가를 발굴할 수 있는 기능을 제공합니다.
  
- **메시지 기능** 
  - 회원 간 1:1 메시지를 통해 소통하고, 웹툰 작품이나 채용 공고에 대해 피드백을 주고받을 수 있습니다.
  
- **추천 시스템** 
  - 사용자가 선호하거나 활동한 웹툰이나 게시글을 추천할 수 있으며, 추천수는 바로 확인할 수 있습니다.
  
- **프로필 관리** 
  - 작가와 기업 모두 자신만의 프로필을 관리하고, 필요한 정보를 업데이트할 수 있는 기능을 제공합니다.


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
  - **Ajax**

- **데이터베이스**
  - **MariaDB**

- **도구**
  - **ERDCloud**
  - **Figma** 
  - **Git / GitHub** 


## 🗂️ DB 테이블 설계

- **board**: 게시판에 대한 정보를 저장하는 테이블.
- **board_type**: 게시판의 유형을 정의하는 테이블.
- **comment**: 게시물과 웹툰 에피소드에 달린 댓글을 저장하는 테이블.
- **site_user**: 플랫폼 사용자(일반회원이자 작가 회원)의 정보를 저장하는 테이블.
- **company_user**: 기업 사용자의 정보를 저장하는 테이블.
- **episode_image**: 웹툰 회차에 해당하는 이미지 파일을 저장하는 테이블.
- **favorite**: 사용자가 즐겨찾기한 웹툰 정보를 저장하는 테이블.
- **genre_type**: 웹툰의 장르 유형을 정의하는 테이블.
- **message**: 사용자 간에 주고받은 메시지를 저장하는 테이블.
- **my_profile**: 사용자의 프로필 정보를 저장하는 테이블.
- **recommend**: 웹툰과 게시글에 대한 추천 정보를 저장하는 테이블.
- **web_toon**: 웹툰 작품에 대한 정보를 저장하는 테이블.
- **web_toon_episode**: 웹툰의 개별 회차 정보를 저장하는 테이블.



## 📊 E-R 다이어그램

![Frame 2](https://github.com/user-attachments/assets/a26b7273-ae9c-433c-8c08-8790e26c43f7)

## 🎬 소개영상
<a href="https://www.youtube.com/watch?v=P1Mu25DWq70&t=3s" target="_blank">
    <img src="https://img.youtube.com/vi/P1Mu25DWq70/maxresdefault.jpg" alt="소개영상" />
</a>

  
## 👥 역할 분담
| 이름     | 역할               | 담당 업무                                                            |
|----------|--------------------|---------------------------------------------------------------------|
| 이은규   | 팀장 / Backend 개발 | Spring Boot 기반 백엔드 개발, DB 설계, ERD 설계                        |
| 길현수   | 팀원 / Frontend 개발 | Thymeleaf, HTML, CSS, JavaScript를 사용한 프론트엔드 개발 및 UI/UX 구현 |


## 🗂️ 프로젝트 구조

```
src
│  ├─main
│  │  ├─generated
│  │  ├─java
│  │  │  └─com
│  │  │      └─example
│  │  │          └─fiction_place1
│  │  │              │  FictionPlace1Application.java
│  │  │              │
│  │  │              ├─domain
│  │  │              │  ├─board
│  │  │              │  │  ├─controller
│  │  │              │  │  │      CreateBoardController.java
│  │  │              │  │  │      DeleteBoardController.java
│  │  │              │  │  │      ModifyBoardController.java
│  │  │              │  │  │
│  │  │              │  │  ├─entity
│  │  │              │  │  │      Board.java
│  │  │              │  │  │
│  │  │              │  │  ├─form
│  │  │              │  │  │      BoardForm.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      BoardRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          BoardService.java
│  │  │              │  │
│  │  │              │  ├─board_type
│  │  │              │  │  ├─entity
│  │  │              │  │  │      BoardType.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      BoardTypeRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          BoardTypeService.java
│  │  │              │  │
│  │  │              │  ├─comment
│  │  │              │  │  ├─controller
│  │  │              │  │  │      CommentController.java
│  │  │              │  │  │
│  │  │              │  │  ├─entity
│  │  │              │  │  │      Comment.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      CommentRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          CommentService.java
│  │  │              │  │
│  │  │              │  ├─favorite
│  │  │              │  │  ├─entity
│  │  │              │  │  │      Favorite.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      FavoriteRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          FavoriteService.java
│  │  │              │  │
│  │  │              │  ├─genre_type
│  │  │              │  │  ├─entity
│  │  │              │  │  │      GenreType.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      GenreTypeRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          GenreTypeService.java
│  │  │              │  │
│  │  │              │  ├─message
│  │  │              │  │  ├─controller
│  │  │              │  │  │      MessageController.java
│  │  │              │  │  │
│  │  │              │  │  ├─entity
│  │  │              │  │  │      Message.java
│  │  │              │  │  │
│  │  │              │  │  ├─form
│  │  │              │  │  │      MessageForm.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      MessageRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          MessageService.java
│  │  │              │  │
│  │  │              │  ├─profile
│  │  │              │  │  ├─controller
│  │  │              │  │  │      MyProfileController.java
│  │  │              │  │  │
│  │  │              │  │  ├─entity
│  │  │              │  │  │      MyProfile.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      MyProfileRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          MyProfileService.java
│  │  │              │  │
│  │  │              │  ├─recommend
│  │  │              │  │  ├─entity
│  │  │              │  │  │      Recommend.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      RecommendRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          RecommendService.java
│  │  │              │  │
│  │  │              │  ├─user
│  │  │              │  │  ├─controller
│  │  │              │  │  │      CompanyUserController.java
│  │  │              │  │  │      LoginController.java
│  │  │              │  │  │      SiteUserController.java
│  │  │              │  │  │
│  │  │              │  │  ├─entity
│  │  │              │  │  │      CompanyUser.java
│  │  │              │  │  │      SiteUser.java
│  │  │              │  │  │      User.java
│  │  │              │  │  │
│  │  │              │  │  ├─form
│  │  │              │  │  │      CompanyUserCreateForm.java
│  │  │              │  │  │      SiteUserCreateForm.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      CompanyUserRepository.java
│  │  │              │  │  │      SiteUserRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          CompanyUserService.java
│  │  │              │  │          SiteUserService.java
│  │  │              │  │
│  │  │              │  ├─webtoon
│  │  │              │  │  ├─controller
│  │  │              │  │  │      FileController.java
│  │  │              │  │  │      WebToonController.java
│  │  │              │  │  │
│  │  │              │  │  ├─entity
│  │  │              │  │  │      WebToon.java
│  │  │              │  │  │
│  │  │              │  │  ├─form
│  │  │              │  │  │      WebToonForm.java
│  │  │              │  │  │
│  │  │              │  │  ├─repository
│  │  │              │  │  │      WebToonRepository.java
│  │  │              │  │  │
│  │  │              │  │  └─service
│  │  │              │  │          FileService.java
│  │  │              │  │          WebToonService.java
│  │  │              │  │
│  │  │              │  └─webtoon_episode
│  │  │              │      ├─controller
│  │  │              │      │      WebToonEpisodeController.java
│  │  │              │      │
│  │  │              │      ├─entity
│  │  │              │      │      EpisodeImage.java
│  │  │              │      │      WebToonEpisode.java
│  │  │              │      │
│  │  │              │      ├─form
│  │  │              │      │      WebToonEpisodeForm.java
│  │  │              │      │
│  │  │              │      ├─repository
│  │  │              │      │      EpisodeImageRepository.java
│  │  │              │      │      WebToonEpisodeRepository.java
│  │  │              │      │
│  │  │              │      └─service
│  │  │              │              WebToonEpisodeService.java
│  │  │              │
│  │  │              └─global
│  │  │                  ├─initData
│  │  │                  │      Init.java
│  │  │                  │
│  │  │                  ├─jpa
│  │  │                  │      BaseEntity.java
│  │  │                  │
│  │  │                  └─security
│  │  │                          UserSecurityConfig.java
│  │  │                          WebMvcConfig.java
│  │  │
│  │  └─resources
│  │      │  application-dev.yml
│  │      │  application-secret.yml.default
│  │      │  application.properties
│  │      │  application.yml
│  │      │
│  │      ├─static
│  │      │  │  access_denied.css
│  │      │  │  board_detail.css
│  │      │  │  board_list.css
│  │      │  │  create_board.css
│  │      │  │  episode_detail.css
│  │      │  │  episode_list.css
│  │      │  │  episode_modify.css
│  │      │  │  main_page_episode_detail.css
│  │      │  │  modify_board.css
│  │      │  │  my_favorite_webtoon.css
│  │      │  │  my_webtoon.css
│  │      │  │  webtoon_create_form.css
│  │      │  │  webtoon_list.css
│  │      │  │  webtoon_modify.css
│  │      │  │
│  │      │  ├─CSS
│  │      │  │      companysignup.css
│  │      │  │      company_login.css
│  │      │  │      myprofile.css
│  │      │  │      Navber.css
│  │      │  │      siteusersignup.css
│  │      │  │      userlogin.css
│  │      │  │      user_modify.css
│  │      │  │      Webtoon_list.css
│  │      │  │
│  │      │  ├─images
│  │      │  │  │  logoimage.gif
│  │      │  │  │  slick1.png
│  │      │  │  │  slick2.png
│  │      │  │  │  slick3.jpg
│  │      │  │  │  unnamed.png
│  │      │  │  │
│  │      │  │  └─cursor
│  │      │  │          얼룩냥_연결.cur
│  │      │  │          얼룩냥_일반.cur
│  │      │  │
│  │      │  ├─JS
│  │      │  │      dropdown.js
│  │      │  │
│  │      │  └─upload
│  │      │          13_profile.png
│  │      │
│  │      └─templates
│  │              access_denied.html
│  │              board_detail.html
│  │              board_list.html
│  │              company_login.html
│  │              company_user_signup.html
│  │              create_board.html
│  │              episode_detail.html
│  │              episode_list.html
│  │              episode_modify.html
│  │              login_user2.html
│  │              main_page_episode_detail.html
│  │              message_detail.html
│  │              message_menu.html
│  │              message_send.html
│  │              message_sent.html
│  │              modify_board.html
│  │              modify_user.html
│  │              myprofile.html
│  │              my_favorite_webtoon.html
│  │              my_webtoon.html
│  │              navbar.html
│  │              site_user_signup.html
│  │              user_login.html
│  │              webtoon_create_form.html
│  │              webtoon_episode_create.html
│  │              webtoon_like_list.html
│  │              webtoon_list.html
│  │              webtoon_modify.html
│  │
│  └─test
│      └─java
│          └─com
│              └─example
│                  └─fiction_place1
│                          FictionPlace1ApplicationTests.java
│
└─upload
        13_profile.png
```

## 🛠️ 트러블슈팅
<details>
  <summary>📑 이은규</summary>
1️⃣ 이미지 저장 시 DB 용량 부족 문제
- 문제 원인:기존에 이미지 파일을 데이터베이스에 직접 저장할 때 VARBINARY(255) 형식을 사용했으며, 이는 최대 255바이트까지만 저장할 수 있었습니다. 이로 인해 이미지 파일이 약 0.25KB 이하일 때만 저장 가능했으며, 여러 장의 이미지를 저장할 경우 용량 부족으로 저장이 불가능한 문제가 발생했습니다.
- 해결 방법:
   - 파일 시스템 사용: 이미지 데이터를 데이터베이스 대신 서버 파일 시스템에 저장하고, Episode Image  엔티티를 생성하여 이미지 경로(URL)만 저장하도록 설계를 변경했습니다.
   - URL 저장 방식: 이미지 파일 자체는 서버의 파일 시스템에 저장되고, 데이터베이스에는 해당 이미지의 URL 경로만 저장됩니다. 또한, 이미지 URL을 저장하는 필드의 데이터 타입을 VARCHAR(1000) 으로 설정하여, 최대 1000자까지 저장할 수 있게 했습니다. 이를 통해 이미지 파일 크기와 관계없이 이미지 경로만 관리할 수 있어 효율적인 이미지 관리가 가능해졌습니다.

2️⃣ 웹툰 장르별 추천 기능 오류
 - 문제 원인:장르별 필터링과 추천순 정렬을 처리하는 로직이 명확히 분리되지 않았습니다. 장르 ID가 있을 때는 해당 장르의 웹툰을 추천순(좋아요 수 기준)으로 가져오도록 해야 했지만, 코드상에서 장르 ID 유무에 따라 처리 방식이 명확하게 구분되지 않아 문제가 발생했습니다.
 - 해결 방법:
    - 로직 수정: 장르별 웹툰을 추천순으로 가져오기 위해, 장르 ID가 있는 경우 해당 장르별 웹툰을 좋아요 수 기준으로 내림차순 정렬하여 가져오도록 했습니다. 장르 ID가 없는 경우에는 전체 웹툰을 추천순으로 가져오도록 수정했습니다.
- 성과
    - 서버 파일 시스템에 저장함으로써 이미지 파일 크기 제한이 사라지고, 데이터베이스 용량 절약으로 성능 향상 및 확장성 확보
    - 정확한 추천 로직을 통해 사용자가 원하는 장르에 대한 추천 품질 향상

  
  1️⃣ 이미지 저장 시 DB 용량 부족 문제
  
- 문제 원인
  - 이미지 파일을 데이터베이스에 직접 저장하면서 용량 부족 문제가 발생했습니다.

- 해결 방법
  -  파일 시스템 사용: 이미지 데이터를 데이터베이스 대신 서버 파일 시스템에 저장하도록 변경.
  -  엔티티 설계 변경: EpisodeImage 엔티티를 새로 생성하고 이미지 경로(URL)만 저장하도록 설계 변경.

2️⃣ 사용자 인증 및 세션 관리 문제

- 문제 원인
  - Principal 객체가 null 값을 반환하여 사용자 인증에 문제가 발생했습니다.

- 해결 방법
  - 세션을 통한 사용자 정보 관리: Principal 대신 세션을 통해 사용자 정보를 직접 관리하도록 수정.

3️⃣ 웹툰 장르별 추천 기능 오류

- 문제 원인
  -  장르를 선택한 후 추천순 정렬이 제대로 동작하지 않았습니다.
  
- 해결 방법
  - WebtoonRepository를 통해 장르별 필터링 및 추천 순 정렬을 분리하여 처리했습니다.
</details>

---

<details>
  <summary>📑 길현수</summary>
  - 추가 예정
</details>


## 🖥️ 아쉬웠던 점 & 배운 점

<details>
  <summary><b>📑 이은규</b></summary>
  
  ## 📈 **아쉬웠던 점**  
  - 프로젝트 초기 단계에서 설계의 중요성을 간과하여 중간에 여러 번 구조를 수정해야 했다.  
  - OAuth2 (Kakao 로그인) 기능이 완벽하게 구현되지 못했다.  
  - UI/UX 디자인이 모든 페이지에서 일관적이지 않았고, 일부 사용자 경험이 부족했다.  
  
  ---

  ## 📚 **배운 점**  
  - 사용자 유형별 권한 관리 및 데이터베이스 테이블 설계의 중요성을 깨달았다.  
  - 초기 설계 단계에서 기능 명세와 구조를 명확하게 정의하는 것이 중요하다.  
  - 팀원 간의 원활한 소통과 협업이 프로젝트의 완성도를 높이는 핵심 요소임을 배웠다.  
</details>

---

<details>
  <summary><b>📑 길현수</b></summary>
  
  ## 📈 **아쉬웠던 점**
  - 추가 예정
  
  ## 📚 **배운 점**
  - 추가 예정
</details>



## 📄 기타 참고사항
프로젝트는 지속적으로 개선 및 업데이트될 예정입니다.
버그나 개선사항이 있다면 이슈로 등록해 주세요.

|제목|내용|설명|
|------|---|---|
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|
