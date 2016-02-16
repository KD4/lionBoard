lionboard
=========

공부삼아 만들어보는 java-spring-mybatis 게시판

모듈 다이어그램
-----------
![diagram_1](lionboard-modules.png)


설치 & 설정 & 빌드
-----------

```
$ git clone git@github.com:KD4/LionBoard.git
$ cd lionboard
$ vim lionboard-common/resources/custom.properties // properties 설정
$ mvn clean package -Dmaven.test.skip=true

```

Deploy war file on /lionboard-web/target
