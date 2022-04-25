# Auth_Server
## Spring boot playground

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
