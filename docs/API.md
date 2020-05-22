
# API 
## 공통 스펙 


## [POST] /v1/coupons 
## 숫자 N을 요청하면 N개 만큼 쿠폰을 발급하는 API
### Request
~~테이블~~
### Response
### Success (200 OKAY)
```json
[
    "52a40c98-5a96-4017-b2d5-8a80d676d4e7"
]
```

### Error 
~~테이블~~



## [GET] /v1/coupon/{serial}
### 사용자에게 지급된 쿠폰을 조회하는 API
### Response
### Success (200 OKAY)
```json
{
    "serial": "c32663dd-3a26-41fe-b660-079fadb7b8b5",
    "expired": "2024-10-27T17:13:40+00:00"
}
```

## [PUT] /v1/coupon/{serial}/assign
### 쿠폰 지급 API
### Response
### Success (200 OKAY)
```http request
empty response
```



## [PUT] /v1/coupon/{serial}/use
### 지급된 쿠폰을 사용하는 API
### Response
### Success (200 OKAY)
```http request
empty response
```

## [DELETE] /v1/coupon/{serial}/use
### 지급된 쿠폰을 사용취소하는 API 
### Response
### Success (200 OKAY)
```http request
empty response
```