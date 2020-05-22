# 사전과제01

## 목적 
- 쿠폰 서비스 프로젝트에 대하여, 설계/구성 한 문서.

## 프로젝트 목적 
- REST API를 통한, 쿠폰 관리(발급,사용,삭제,취소) API 서비스.

## 기본 요구사항 
- [x] 숫자 N을 요청하면 N개 만큼 쿠폰을 발급하는 API
- [x] 쿠폰 번호를 입력하여, 사용 내역을 확인하는 API
- [x] 지급된 쿠폰을 사용하는 API 
- [x] 지급된 쿠폰을 사용하는 API 
- [x] 당일 만료된 쿠폰을 전체 쿠폰 목록을 조회하는 API 

## 추가 요구사항 
- [ ] API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받은 토큰을 가지고 호출하세요.
- [ ] 100억개 이상 쿠폰 관리 저장 관리 가능하도록 구현할것
- [ ] 10만개 이상 벌크 csv Import 기능
- [ ] 대용량 트랙픽(TPS 10K 이상)을 고려한 시스템 구현
- [ ] 성능테스트 결과 / 피드백

## 문서

# 환경 
## 프레임워크 
- Spring Boot : 2.3.0.RELEASE
- hibernate-core : 5.4.8 Final
- jackson : 2.10.0

## 최소 동작 환경 
- Java : 1.8 이상
- Database : MariaDB, H2, `Hibernate`가 지원하는 디비셋 (단, 앞에 2개를 제외하고 테스트가 진행되지 않음.)

## 개발환경 
- OS : macOS Catalina 10.15 
- CPU : Intel Core i5 3.1 GHz Dual Core
- Memory : 16G 
- Database : H2 

## 성능 테스트 환경 
- OS : Ubuntu 16.04 LTS Server
- CPU : Intel(R) Core(TM) i5-2500 CPU @ 3.30GHz
- Memory : 8196M
- JVM Option : -server -Xms 2048m -Xmx 4096M
- Java Version : Openjdk 1.8.0_22
- Database : 10.3.6-MariaDB-1:10.3.6+maria~jessie