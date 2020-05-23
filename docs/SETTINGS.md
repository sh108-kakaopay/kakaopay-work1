# 설정 파일 
서버 구동을 위해 필요한 설정 파일에 대해 이야기 합니다.

## application.properties
| 키 | 설명 | 기본값 | 타입 | 비고 |
| --- | --- | --- | --- | --- |
| coupon.app.batchEnable | 만료 메세지 발송을 사용 할 것인지? | false | boolean (true/false) | - |
| coupon.jwt.secret | JWT 인증 키  | yangs | String | - |
| coupon.jwt.tokenAvailableHour| JWT 토큰 만료 시간  | 24 | int | - |
| coupon.app.batchTargetDay| 만료 메세지 발송시, 몇일전 유저를 기준으로 할지 | 3 | int | - |