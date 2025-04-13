# 버킷플레이스 백엔드 과제 설명



## 준비사항

해당 경로 아래에 샘플 데이터 적재<br>
+ C:/Temp/jsonUpload/sample_user_log.json<br>

개발환경<br>
+ JAVA : jdk 17<br>
+ IDE : intellj<br>
+ Framework : springboot 3.4.4<br>
+ DB : mysql & mybatis<br>
+ build : gradle<br>

사용포트(참고)<br>
+ 8081(java) , 13306(mysql)<br><br>

데이터 덤프파일 위치<br>
+ src/main/resources/dumpFile

>테이블을 모두 생성 후, 코드스크립트를 순차적으로 수행시 data insert 가 됩니다.<br>

<br>

## ER-Diagram
![erd](src/main/resources/img/erdiagram.png)<br>

***
## 코드 설명
### 데이터 생성 스크립트 개발
>StoreManagingSystemApplicationTests.java 클래스의 각 메소드를 run 하여 실행할 수 있습니다

1. 2-a 번 문항<br>
* setUsers
* 활동 로그에서 추출한 회원 ID수만큼 회원 정보 생성
* ID 외 정보는 임의 입력

2. 2-b 번 문항<br>
3. 2-c 번 문항<br>
4. 2-d 번 문항<br>



