# 🏃‍♂️운동 기록 공유 게시판🏃‍♂️

오늘의 운동에 대한 기록을 작성하여 공유하는 게시판 프로젝트

## 💪기획 배경💪
- 혼자 헬스장을 다니면서 흥미도 잃어가고 열심히하고자 하는 의지가 사라짐
- 의지가 없어져 헬스장을 방문하지 않게 됨

## 해결 컨셉
- 오늘 실시한 운동량을 기록함
- 서로의 게시글에 댓글, 좋아요 등 반응할 수 있음

## 기대 효과
- 서로의 기록을 보며 자극받을 수 있음
- 운동의지를 불태울 수 있음

## ⚙Setting⚙
- Java 17
- Project : Gradle - Groovy
- Spring Boot 3.2.3
- gradle 8.6 

## 🕹USE🕹
- Spring Security
- Spring Data Jpa
- MariaDB
- swagger
- SSE (Server-Sent Event)
- SMTP

## ✔프로젝트 기능 및 설계✔
- [x] 회원가입 기능
    - 모든 사용자는 회원가입을 할 수 있다. 이때 회원 가입한 모든 사용자는 USER 권한(일반 권한)을 가진다.
    - 회원가입 시 아이디, 패스워드, 이메일 을 입력받는다.
    - 아이디는 유니크해야한다. 패스워드는 PasswordEncoder를 사용하여 인코딩 한 후 저장한다.
    - 이메일을 입력받을 때 해당 이메일에 대한 인증을 실시한다.

- [x] 로그인 기능
    - 방법1) 회원가입시 사용한 아이디와 패스워드가 일치 -> 성공

- [x] 게시물 작성 기능
    - 로그인한 사용자는 게시물을 작성할 수 있다.
    - 게시글 작성 항목 : 5개의 운동 항목 (데드리프트, 힙 쓰러스트, 스쿼트, 런지, 유산소) , 운동 목록별 kg, 세트당 횟수, 몇세트
    - 게시글 작성 시 JSON_ARRAY 형태로 입력받는다.

- [x] 게시물 수정 기능
    - 게시물 작성자만 수정 가능

- [x] 게시물 삭제 기능
    - 게시물 작성자만 삭제 가능
    - 게시물이 삭제되면 그 게시물에 달린 댓글 모두 삭제

- [x] 게시물 목록 조회 기능
    - 모든 사용자는 게시물 조회 가능 (로그인 하지 않아도 가능)
    - 최신순으로 게시물 정렬
    - 게시물이 많으면 Paging 처리를 통해 한 페이지당 게시물 10개씩 보여지도록 함
    - 게시글 제목, 게시글 내용, 작성자를 보여준다.

- [x] 댓글 작성 기능
    - 댓글은 로그인한 회원만 작성 가능
    - 댓글이 작성되면 게시물 작성자에게 알림이 가도록 함
        - Spring 에서 제공하는 SSE (Server-Sent Event) 사용

- [x] 댓글 수정 기능
    - 댓글 작성자만 수정 가능

- [x] 댓글 삭제 기능
    - 댓글 작성자와 게시물 작성자만 댓글 삭제 가능

- [x] 특정 게시물 검색 기능
    - 키워드를 입력하여 일기의 제목에 해당값이 있을 경우 조회하는 기능
    - 입력받을 값 : 키워드
    - JPA 를 이용하여 기능 구현
    - 로그인을 하지 않아도 조회 가능
    - 해당 게시물이 많으면 Paging 처리를 통해 한 페이지당 10개만 보여지도록 함

- [x] 게시물, 댓글 '좋아요' 추가
    - 로그인한 사용자만 좋아요 기능 사용 가능
    - 한 사람이 동일한 게시물을 중복하여 누를 수 없도록 아이디로 구별

## 🏛ERD🏛
![image](https://github.com/leejaeeun59357/exercise_board/assets/149572895/3703b401-7995-43e9-85f1-341d627310f6)


## 😂Trouble Shooting😂
### 🚨 @AuthenticationPrincipal 어노테이션 사용 시 객체에 null 값이 NullPointerException 에러 발생

**[해결 과정]**

![image](https://github.com/user-attachments/assets/64d14e66-0d17-455c-8fa8-1f48324f9f16)
디버깅 과정에서 정상적으로 생성된 Authentication 이 SecurityContext 에 저장되는 것을 파악함.

그렇다면 생성된 Authentication 객체에 문제가 있음을 파악함.

![image](https://github.com/user-attachments/assets/c22aa435-6270-4ff3-aed2-c220eb5335ae)

Authentication 을 생성하는 UsernamePasswordAuthenticationToken 의 생성자를 살펴보았을 때 
![image](https://github.com/user-attachments/assets/4b808f8e-3eb7-405f-9456-2365557c3a32)

Principal 객체를 잘못 넣어서 Authentication 객체를 만들고 있었다.

User 객체를 넣어서 생성하는 것이 아닌 String 형식의 이름만 넣어서 문제가 발생한 것이다.

따라서 accessToken에 포함된 User 정보를 Authentication 객체에 넣어 생성하였더니 정상적으로 작동했다.


### 🚨 로그인 후 생성된 Token 사용 시 사용할 수 없는 문제 발생
- 로그인 과정을 거친 후 JWT이 생성되었고 해당 Token을 사용 시 filter에서 걸러지는 문제가 발생했다.

디버깅 과정을 통해 
`SecurityContextHolder.getContext().setAuthentication(authentication);` 
에서 Authentication 객체가 `Authentication = false`로 되어 있고 `Granted Authorities = null` 이 들어가 있었다.

**[문제코드]**

`return new UsernamePasswordAuthenticationToken(accessToken, authorities);` 에서 Authentication을 잘못 생성한 것이였다.

**[해결 과정]**

UsernamePasswordAuthenticationToken 생성자를 살펴보았다.

``public UsernamePasswordAuthenticationToken(
    java.lang.Object principal, 
    java.lang.Object credentials, 
    java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities
    ) { /* compiled code */ }
``

해당 생성자에서 권한을 설정할 때 3가지의 값을 넣어서 생성해야하는 것을 파악했다.

**[변경 후 코드]**

`return new UsernamePasswordAuthenticationToken(accessToken, "", authorities);`로 변경하였다.
