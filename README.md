[Restaurant voting system](https://github.com/LeonidErmolicDmitriev/restaurant_voting_system)
==============================================================

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/0a6c350cede347309afb9d4ffd259cf3)](https://www.codacy.com/gh/LeonidErmolicDmitriev/restaurant_voting_system/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LeonidErmolicDmitriev/restaurant_voting_system&amp;utm_campaign=Badge_Grade)

-------------------------------------------------------------
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

-------------------------------------------------------------

- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 2.5, Lombok, H2, Caffeine Cache, Swagger/OpenAPI 3.0, Mapstruct,
  Liquibase
- Run: `mvn spring-boot:run` in root directory.

-------------------------------------------------------------
[REST API documentation](http://localhost:8080/swagger-ui.html)  
Credenshels:

```
Admin: admin@gmail.com / admin
User1: user1@yandex.ru / password1
User2: user2@yandex.ru / password2
User3: user3@yandex.ru / password3
```