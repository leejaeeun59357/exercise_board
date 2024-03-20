# 🏃‍♂️운동 기록 공유 게시판🏃‍♂️

오늘의 운동에 대한 기록을 작성하여 공유하는 게시판 프로젝트

## ⚙Setting⚙
- Java 17
- Spring Boot 3.2.3

## USE
- Spring Security
- Spring Data Jpa
- MariaDB
- swagger
- JWT (Json Web Token)
- SSE (Server-Sent Event)
- OAuth 기능

## 프로젝트 기능 및 설계
- 회원가입 기능
    - 모든 사용자는 회원가입을 할 수 있다. 이때 회원 가입한 모든 사용자는 USER 권한(일반 권한)을 가진다.
    - 회원가입 시 아이디, 패스워드, 이메일 을 입력받는다.
    - 아이디는 유니크해야한다. 패스워드는 PasswordEncoder를 사용하여 인코딩 한 후 저장한다.
    - 이메일을 입력받을 때 해당 이메일에 대한 인증을 실시한다.

- 로그인 기능
    - 방법1) 회원가입시 사용한 아이디와 패스워드가 일치 -> 성공
    - 방법2) 카카오 로그인
        - OAuth 사용

- 게시물 작성 기능
    - 로그인한 사용자는 게시물을 작성할 수 있다.
    - 게시글 작성 항목 : 5개의 운동 항목 (데드리프트, 힙 쓰러스트, 스쿼트, 런지, 유산소) , 운동 목록별 kg, 세트당 횟수, 몇세트

- 게시물 수정 기능
    - 게시물 작성자만 수정 가능

- 게시물 삭제 기능
    - 게시물 작성자만 삭제 가능
    - 게시물이 삭제되면 그 게시물에 달린 댓글 모두 삭제

- 게시물 목록 조회 기능
    - 모든 사용자는 게시물 조회 가능 (로그인 하지 않아도 가능)
    - 최신순으로 게시물 정렬
    - 게시물이 많으면 Paging 처리를 통해 한 페이지당 게시물 10개씩 보여지도록 함
    - 일기 번호, 일기 제목, 작성자, 작성일시, 수정일시 를 보여준다.

- 댓글 작성 기능
    - 댓글은 로그인한 회원만 작성 가능
    - 댓글이 작성되면 게시물 작성자에게 알림이 가도록 함
        - Spring 에서 제공하는 SSE (Server-Sent Event) 사용

- 댓글 수정 기능
    - 댓글 작성자만 수정 가능

- 댓글 삭제 기능
    - 댓글 작성자와 게시물 작성자만 댓글 삭제 가능

- 특정 게시물 검색 기능
    - 키워드를 입력하여 일기의 제목에 해당값이 있을 경우 조회하는 기능
    - 사용할 기능 : Elastic Search 사용
    - 입력받을 값 : 키워드
    - 로그인을 하지 않아도 조회 가능
    - 해당 게시물이 많으면 Paging 처리를 통해 한 페이지당 10개만 보여지도록 함

- 게시물, 댓글 '좋아요' 추가
    - 로그인한 사용자만 좋아요 기능 사용 가능
    - 한 사람이 동일한 게시물을 중복하여 누를 수 없도록 아이디로 구별
 
