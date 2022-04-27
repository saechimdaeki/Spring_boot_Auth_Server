# Auth_Server

<details>
<summary>smtp를 사용하기 위한 준비</summary>

## [gmail smtp 구성](https://support.google.com/a/answer/176600?hl=ko#zippy=%2Cgmail-smtp-%EC%84%9C%EB%B2%84-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0)

### 현재 application.yml이 다음과 같음
```yaml
spring:
  profiles:
    active: local
    include:
      - secret #application-secret.yml에 내 smtp정보 전부 저장 (gitignore처리)
  redis:
    host: localhost
    port: 6379

server:
  ssl:
    key-store: keystore.p12
    key-store-type: PKCS12
    key-alias: saechim
    key-store-password: 940215
  http2:
    enabled: true
---

spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/authlocal
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
  config:
    activate:
      on-profile: local

---

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/auth
    username: root
    password: 1234
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
    show-sql: true
  config:
    activate:
      on-profile: mysql
```
### 따라서 application-scret.yml을 만든 후 다음과 같이 설정
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${gmail 계정}
    password: ${발급받은 비밀번호}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

</details>

<details>
<summary>self-signed ssl https 요청이 보이지 않을때</summary>

### 해당 링크를 클릭로 이동 ->  chrome://flags/#allow-insecure-localhost
<img src=https://user-images.githubusercontent.com/40031858/165410707-86b0202f-c589-4afb-ae75-3c7bf176a964.png width=500px>

### Allow invalid certificates for resources loaded from localhost를 enabled.
</details>


## Configuration for run (mac m1)
```markdown

## profiles mysql 기준
1. docker run --platform linux/amd64 -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=1234 -e -d mysql
2. docker start mysql
3. docker exec -it mysql bash

## profiles local 기준
1. run h2
2. login with this url : jdbc:h2:~/authlocal
3. disconnect
4. jdbc:h2:tcp://localhost/~/authlocal

## 공통 redis
1. docker run -p 6379:6379 --name redis_boot -d redis
2. docer start redis_boot
3. 확인하고 싶다면 docker exec -i -t redis_boot redis-cli
```
