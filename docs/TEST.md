## 테스트 
### 테스트 방법
```bash
mvn test
```

### Github Action을 통한 지속적인 테스트  
- 목적 : 코드 품질 향상을 위한, 지속적인 테스트.
- 연동 Runner : Github Action
[테스트 결과 바로가기](./actions)
- 테스트 방법 : 전체 테스트 실행 (Unit & 시나리오 테스트)

### 서비스 유닛 테스트
 - 경로 : com.kakaopay.event.coupon.service.*
-  특징 
    -  실제 성공 동작에 대해서만 테스트 (시나리오 기반 테스트에서 엣지 케이스에 대한 테스트)
  
###  시나리오 기반 테스트
 - 경로 : com.kakaopay.event.coupon.controller/{ControllerName}/{테스트API}Test.java
 - 특징
     - 유저가 실제 Controller를 통해 요청하는 형태의 시나리오 테스트를 통해  A-Z까지 테스트가 가능하게 하려고 하였음.
     - 추후 리팩토링이 있을떄도,  
