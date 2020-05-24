# 실행 방법 
해당 어플리케이션을 실행하는 방법에 대해 이야기 합니다.

## Pre
### Lombok (https://projectlombok.org/)
-  IDEA : https://www.baeldung.com/lombok-ide#intellij
-  eclipse : https://www.baeldung.com/lombok-ide#eclipse
-  ETC : 'lombok plugin' 으로 검색

## Build 
```bash
mvn -B package --file pom.xml
```

## Run 
```bash
java -cp *:. -jar coupon-0.0.1-SNAPSHOT.jar
```