# KakaoPay 과제01  ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-jupiter]

## 목적 
- 쿠폰 서비스 프로젝트에 대하여, 설계/구성 한 문서.

## 프로젝트 목적 
- REST API를 통한, 쿠폰 관리(발급,사용,삭제,취소) API 서비스.

## 요구사항 정리 
- 숫자 N을 요청하면 N개 만큼 쿠폰을 발급하는 API
- 쿠폰 번호를 입력하여, 사용 내역을 확인하는 API
- 지급된 쿠폰을 사용하는 API 
- 지급된 쿠폰을 사용하는 API 
- 당일 만료된 쿠폰을 전체 쿠폰 목록을 조회하는 API 

## API 설계
### [POST] /v1/coupon
쿠폰 생성 요청 API 
### Request
#### Parameter
- coupon-count :  100 (Required)

### Response 
### 응답 코드 
- 201 Created : 생성 완료 
- 500 Internal Server Error : 서버 오류 
- 400 Bad Request : 잘못된 요청 
- 416 Range Not Satisfiable : 이 API를 통해 생성 가능한 최대치 초과


### [GET] /v1/coupon/{coupon-id}
쿠폰 상세 정보 호출 API 
### Request 
- coupon-id : 32자리 UUID 스펙 

### Response
- 200 : 사용 가능한 쿠폰
- 403 : 이미 사용된 쿠폰
- 404 : 존재 하지 않는 쿠폰 


### [PUT] /v1/coupon/{coupon-id}
### Response
- 404 : 존재하지 않는 쿠폰 
- 403 : 이미 사용된 쿠폰 
- 204 : 사용 처리 완료 


### [DELETE] /v1/coupon/{coupon-id}/{coupon-result-id}
### Request 


### Response
- 404 : 존재하지 않는 쿠폰 
- 403 : 이미 사용된 쿠폰 
- 204 : 사용 처리 완료 

