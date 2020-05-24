# 설정 파일

서버 구동을 위해 필요한 설정 파일에 대해 이야기 합니다.

## application.properties

| 키 | 설명 | 기본값 | 타입 | 비고 |
| --- | --- | --- | --- | --- |
| coupon.app.batchEnable | 만료 메세지 발송을 사용 할 것인지? | false | boolean (true/false) | - |
| coupon.jwt.secret | JWT 인증 키 | yangs | String | - |
| coupon.jwt.tokenAvailableHour | JWT 토큰 만료 시간 | 24 | int | - |
| coupon.app.batchTargetDay | 만료 메세지 발송시, 몇일전 유저를 기준으로 할지 | 3 | int | - |
| spring.datasource.url | JDBC 연결 URL  | - | String | 사용할 DBMS별로 Connection URL에 들어가는 파라미터가 다를 수 있습니다.<br>각 제조사가 제공하는 드라이버 스펙을 확인하고 입력 하시기 바랍니다. |
| spring.datasource.username | 연결 계정  | - | String |  |
| spring.datasource.password | 연결 패스워드 | - | String |  |
| spring.jpa.hibernate.ddl-auto | DLD Auto로 Table 생성을 할 것 이냐? | None | None, Update, Create, Create-Drop | 테스트를 할때를 제외하고<br>라이브 환경에서는 자동 생성/수정을  추천하지는 않습니다. |