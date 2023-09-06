## User Authentication and Role Management System

### Introduction:
This system is developed in Java using Spring Boot and provides user authentication and role management functionality.

### Features:
- Create, retrieve, and delete users.
- Create and delete roles.
- Assign roles to users.
- Generate and invalidate authentication tokens.
- Check user roles.
- Get a user's roles.

### Structure
project-root/
│
├── .idea/ # IntelliJ IDEA 配置文件夹
├── .mvn/ # Maven Wrapper 配置文件夹
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── example/
│ │ │ └── demo/
│ │ │ ├── controller/
│ │ │ │ └── UserController.java
│ │ │ ├── model/
│ │ │ │ ├── Role.java
│ │ │ │ ├── TokenInfo.java
│ │ │ │ └── User.java
│ │ │ ├── service/
│ │ │ │ ├── DataInitializer.java
│ │ │ │ ├── DemoApplication.java
│ │ │ │ └── ...
│ │ │ └── ...
│ │ │
│ │ ├── resources/
│ │ │ └── application.properties # 项目配置文件
│ │ │
│ │ └── ...
│ │
│ └── ...
│
├── test/
│ ├── java/
│ │ └── com/
│ │ └── example/
│ │ └── demo/
│ │ ├── AuthServiceTest.java
│ │ ├── UserControllerTest.java
│ │ └── ...
│ │
│ └── ...
│
├── .gitignore 
├── README.md
├── mvnw
├── mvnw.cmd 
└── pom.xml

#### Prerequisites:
- Java 8 or later
- Maven
- Git
- A Java IDE (e.g., IntelliJ IDEA, Eclipse)

#### Getting Started:
1. Clone the repository
2. Build the project using Maven
3. Run the application


### Usage:

#### Create User:
- Endpoint: POST /api/auth/user

#### Get User Information:
- Endpoint: GET /api/auth/{username}

#### Get All Users:
- Endpoint: GET /api/auth/users

#### Delete User:
- Endpoint: DELETE /api/auth/user/{username}

#### Create Role:
- Endpoint: POST /api/auth/role

#### Get All Roles:
- Endpoint: GET /api/auth/roles

#### Delete Role:
- Endpoint: DELETE /api/auth/role/{roleName}

#### Assign Role to User:
- Endpoint: PUT /api/auth/user/{username}/role/{roleName}

#### Generate Authentication Token:
- Endpoint: POST /api/auth/token

#### Invalidate Authentication Token:
- Endpoint: DELETE /api/auth/token/{token}

#### Check User Role:
- Endpoint: GET /api/auth/check-role

#### Get User Roles:
- Endpoint: GET /api/auth/user-roles
