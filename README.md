##LionBoard
=========
The core goal of this project was to learn how to make board service.

I have made this project based on below frameworks.
- Spring
- Maven
- Tomcat
- Mybatis
.. etc

If you have any questions, please leave a message in issues to contact me.

Features:
-----------
- Sign in and sign up as member both basic and social (OAuth)
- Write, Modify and Delete components of board ( posts, comments )
- Manage components of board on admin page.
- ...etc


Project Modules diagram.
-----------
![diagram_1](lionboard-modules.png)


Set up Lionboard environment
-----------
firstly, you need to install mysql, tomcat and maven.

after dependencies is installed , you can follow below commands.

To deploy a war file on tomcat, you have to config the authentication about tomcat and maven.

```
# %TOMCAT_PATH%/conf/tomcat-user.xml
<?xml version='1.0' encoding='utf-8'?>
<tomcat-users>

	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="password" roles="manager-gui,manager-script" />

</tomcat-users>
```
------------------------------------------------
```
# %MAVEN_PATH%/conf/settings.xml
<?xml version="1.0" encoding="UTF-8"?>
<settings ...>
	<servers>
	   
		<server>
			<id>TomcatServer</id>
			<username>admin</username>
			<password>password</password>
		</server>

	</servers>
</settings>
```
-------------------------------------------------


How to Implement
--------

```
$ git clone git@github.com:KD4/LionBoard.git
$ cd lionboard
$ mysql> create database lionboard
$ mysql -u {mysql.user} -p {mysql.password} lionboard < database/updates/create_tables.sql
$ vi lionboard-web/src/main/resources/config/custom.properties # Fill up the blank.
```

Run with tomcat
-------

```
$ cd lionboard # the root of project.
$ mvn clean install tomcat7:run-war -Dwarfile=lionboard-web/target/lionboard-web-0.1.war -Dmaven.test.skip=true
```



