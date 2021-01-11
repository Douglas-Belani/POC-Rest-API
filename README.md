# POC-Rest-API
This is a POC(Proof of Concept) backend marketplace-like REST API project, which tries to apply the concepts of unity testing, authentication and authorization with JWT Tokens, CRUD operations and database modelling in a relational database (MySQL) using layered application architecture and Java as programming language.

A documentation of the API and its endpoints can be found [here.](https://documenter.getpostman.com/view/11450721/TVzREczv) 
This API uses Tomcat as web server listening on port 8080.

It is necessary to provide a database url, user and password to the MySQL.properties file inside src/main/resources. It's is not necessary to provide an user and password in the
email.properties file in the same path for the application to work, altough this may cause stack traces to be printed in the console depending on which HTTP method is invoked(User, Order and Product POST methods). The passwordSecret and jwt_secret can be modified at will, the first is used for password hashing and the last for token signing.

This is a Maven project and all dependencies can be found in the pom.xml file.

The tests in the src/test/it folder are run in an in-memory database (H2 Database with MySQL Mode enabled), so the database used by the application is not affected or changed by the tests. The create.sql in /src/test/resources is run every time a test is executed.
