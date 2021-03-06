# 사전과제01

## 목적 
- 쿠폰 서비스 프로젝트에 대하여, 설계/구성 한 문서.

## 프로젝트 목적 
- REST API를 통한, 쿠폰 관리(발급,사용,삭제,취소) API 서비스.

# 바로가기 
## CI/CD 
- [Github Action](https://github.com/sh108-kakaopay/kakaopay-work1/actions) 
(~~CD의 경우는 Artifact 용량 제한 , 현재 중단.~~)

## 문서화 
- [API](./docs/API.md)
- [환경](./docs/ENV.md)
- [설정파일](./docs/SETTINGS.md)
- [빌드 및 실행방법](./docs/RUN.md)
- [테스트](./docs/TEST.md) 
- [마치며-아쉬운점](./docs/END.md)

## 문제해결 전략
### API 디자인 방향 
- [x] Path를 통해 어떤 API인지 알 수 있도록 Path 디자인 
- [x] /v1 이라는 prefix URL Path 추가. (추후 시스템 리팩토링 혹은 신규 버전 출시시, 경로를 기반으로, 분기 가능.)

### 공통 설계 방향
1. 서버가 따로 가지고 있는 상태 개념은 없음 (스케일 아웃이 가능)
2. 해당 서버는 시스템에 `쿠폰` 을 발급하는 비즈니스 로직에 집중함.
3. 로그인 기능은 인증만 있으며, 인가는 없음. (추후 요구사항이 생긴다면 구현 가능한 구조) 


### 기본 요구사항 
- [x] 숫자 N을 요청하면 N개 만큼 쿠폰을 발급하는 API
    - INPUT으로 N개를 선택 받으면, 디비에 Insert 하게 개발.
    서비스 성능을 고려하여, 한 요청당 1000개 까지 가능하게 함.
    - [UUID4(UUID.randomUUID())](https://en.wikipedia.org/wiki/Universally_unique_identifier) 를 통한 유니크 쿠폰 키 생성
    - 데이터베이스의 유니크 인덱스를 통한 유니크 보장
   
- [x] 생성된 쿠폰중 하나를 사용자에게 지급하는 API
    - 생성된 쿠폰의 ROW에 status를 CREATE -> ASSIGN 으로 플래그 변경 

- [x] 사용자에게 지급된 쿠폰을 조회하는 API
    - 쿠폰 시리얼을 기준으로 조회하여, 'serial', 'expired_timestamp'등을 반환
    
- [x] 지급된 쿠폰을 사용하는 API 
    - 생성된 쿠폰의 ROW에 status를 ASSIGN -> USE로 플래그 변경 
    
- [x] 지급된 쿠폰을 사용취소하는 API 
    - 생성된 쿠폰의 ROW에 status를 USE -> ASSIGN으로 플래그 변경 
    
- [x] 당일 만료된 쿠폰을 전체 쿠폰 목록을 조회하는 API 
    - 오늘 날짜 기준으로 00:00:00 ~ 23:59:59 에서 사용하지 않은 상태 (ASSIGN)만 불러오게 함.
    - 요구사항상 '전체' 쿠폰을 반환 하는 API 였기 때문에, 한 요청에 모두 반환 하게 하였음.
    - 실 서비스였다면, offset을 파라미터로 받고 1,000개씩 반환하고, 1,000개가 오지 않을때까지 무한 요청.
 

### 추가 요구사항 
- [x] API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받은 토큰을 가지고 호출하세요.
    - Spring Security를 통한 인증 필터 기능 구현 
    - OAuth2를 통해 로그인 하는 API를 개발 하려고 하였으나, 배보다 배꼽이 커지는 상황이 생겨 일반 로그인으로 구현.
    - '단, 패스워드는 안전한 방법으로 저장한다.' [BCrypt](https://en.wikipedia.org/wiki/Bcrypt) 방식의 암호화 채택
- [x] 100억개 이상 쿠폰 관리 저장 관리 가능하도록 구현할것
    - Long Type을 통해 저장 가능하게변경.
    - 파티셔닝에 대한 고민을 하였으나, 어떤 DBMS를 사용할지 구체적인 명시가 되어있지 않기 때문에 진행 불가.
- [x] 대용량 트래픽(TPS 10K 이상)을 고려한 시스템 구현
    - 서버 Application에 대해 스케일아웃이 가능한 설계 (StateLess)
    - LB를 위한 하트비트 API 추가
    - 성능을 위하여, 파티셔닝을 고민 하였으나, 어떤 DBMS를 사용할지 구체적인 명시가 되어있지 않기 때문에 진행 불가.
- [x] 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하세요. (실제 메세지를 발송하는것이 아닌 stdout 등으로 출력하시면 됩니다.)
    - Spring Batch 를 통해 개발하는것이 failover, Task 관리, lifeCycle 관리 등 더 이점이 많으나, 배보다 배꼽이 커지는 현상이 있기 떄문
    - Spring Scheduled (매일 아침 09시에 발송. 푸시 전송 관련 법안 이슈를 고려.)를 통해 동작하게 설계. 실 서비스시, Batch가 동작하는 Application을 따로 올리거나, 특정서버에는 배치를 활성화 하는 flag on 필요.
- [ ] 성능테스트 결과 / 피드백
- [ ] 10만개 이상 벌크 csv Import 기능
    