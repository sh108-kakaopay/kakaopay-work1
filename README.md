# 사전과제01

## 목적 
- 쿠폰 서비스 프로젝트에 대하여, 설계/구성 한 문서.

## 프로젝트 목적 
- REST API를 통한, 쿠폰 관리(발급,사용,삭제,취소) API 서비스.


## 문제해결 전략
### 기본 요구사항 
- [x] 숫자 N을 요청하면 N개 만큼 쿠폰을 발급하는 API
    - INPUT으로 N개를 선택 받으면, 디비에 Insert 하게 개발.
    서비스 성능을 고려하여, 한 요청당 1000개 까지 가능하게 함.
    - [UUID4(UUID.randomUUID())](https://en.wikipedia.org/wiki/Universally_unique_identifier){:target="_blank"} 를 통한 유니크 쿠폰 키 생성
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
    - 요구사항상 '전체' 쿠폰을 반환 하는 API 였기 때문에, 전체를 반환 하게 하였음.
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
    - Application에 대해 `StateLess` 한 설계 및 개발 
    - LB를 위한 하트비트 API 추가
    - 성능을 위하여, 파티셔닝을 고민 하였으나, 어떤 DBMS를 사용할지 구체적인 명시가 되어있지 않기 때문에 진행 불가.
- [ ] 성능테스트 결과 / 피드백
- [ ] 10만개 이상 벌크 csv Import 기능

## 문서화
- [API](./docs/API.md)
- [환경](./docs/ENV.md)
