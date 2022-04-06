# KONEPS API Service
"G2B 나라장터 오픈API"를 활용한 IT 소프트웨어 공고 찾기 서비스입니다.
Frontend는 https://github.com/starrything/bookclub-front 를 참고해주세요!

## 기능
- 메인 페이지
- 사전규격공개
- 입찰공고
- (게시판)
- (즐겨찾기)
- (마이페이지)

## 기술 스택
- Frontend : React.js
- Backend : Spring boot, JPA
- DB : PostgreSQL
- Session : Redis

## 프로젝트 구조
DDD을 참고하여 도메인 단위로 sub-package를 구성합니다.
- /libs : 외부 라이브러리
- /src : source 디렉토리
- /src/docs : api documents
- /src/main : 메인 패키지
- /src/main/java : 프로젝트 소스
- /src/main/java/.../domain : 도메인 패키지 (비즈니스 로직 구현)
- /src/main/java/.../domain/model : 도메인 엔티티 객체들이 공통적으로 사용할 객체들 (Enum, Embeddable)
- /src/main/java/.../global : 공통 기능 구현
- /src/main/resources : 정적 리소스 디렉토리
- /src/test : api 테스트 코드 작성

```
project
└───libs
└───src
│   └───docs
│   │   └───asciidoc
│   │       │   api-doc.adoc
│   └───main
│   │   └───java
│   │   │   └───com.jonghyun.koneps
│   │   │       └───domain
│   │   │       │   └───beforespec
│   │   │       │   └───bidnotice
│   │   │       │   └───mainpage
│   │   │       │   └───model
│   │   │       │   └───system
│   │   │       └───global
│   │   │       │   └───config
│   │   │       │   └───mail
│   │   │       │   └───schedule
│   │   │       │   └───security
│   │   │       │   └───util
│   │   │       │   KonepsApiApplication.java
│   │   └───resources
│   │       │   application.properties
│   │       │   application-dev.properties
│   │       │   application-production.properties
│   │       │   banner.txt
│   │       │   logback-spring.xml
│   └───test
│       └───java
│           └───com.jonghyun.koneps
│               └───domain
│   
│   .gitignore
│   build.gradle
│   gradlew
│   gradlew.bat
│   README.md
│   settings.gradle
```

## Request/Response Flow
1. request by Controller
2. Controller call Service interface
3. ServiceImpl access Repository
4. parameters transfer by Dto (Controller-Service)

