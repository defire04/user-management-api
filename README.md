# User Management API

This project demonstrates an example of a User Management API, implementing CRUD operations for user entities and providing API documentation using Swagger.

## Requirements

1. **Fields**: The user entity has the following fields:
   - Email (required, with email pattern validation)
   - First name (required)
   - Last name (required)
   - Birth date (required, must be earlier than the current date)
   - Address (optional)
   - Phone number (optional)
   
2. **Functionality**: The API provides the following functionality:
   - Create user: Registers users who are more than 18 years old.
   - Update user fields: Allows updating one or some user fields.
   - Update all user fields: Allows updating all user fields.
   - Delete user: Deletes a user.
   - Search users by birth date range: Returns a list of users within the specified birth date range. Validates that the "From" date is less than the "To" date.

3. **Unit Tests**: The code is covered by unit tests using Spring framework.

4. **Error Handling**: The code has error handling for REST operations.

5. **API Responses**: Responses are in JSON format.

6. **Data Persistence**: Database usage is not necessary; the data persistence layer is not required.

7. **Spring Boot Version**: Any version of Spring Boot can be used.

8. **Technologies**:
   - Lombok
   - ModelMapper
   - H2 Database
   - Spring Data Envers
   - Spring Boot Starter Web
   - Spring Boot Starter Test
   - Spring Boot Starter Security
   - Spring Boot Starter Data JPA
   - Spring Boot Starter Validation
   - Springdoc OpenAPI Starter WebMvc UI
   - Spring Boot Starter OAuth2 Resource Server
 
10. **Authentication**: Keycloak is used for authentication.

11. **Deployment**: Docker Compose is used for deployment.

## Getting Started

1. Clone this repository to your local machine:

    ```bash
    git clone [https://github.com/defire04/user-management-api](https://github.com/defire04/user-management-api.git)
    ```

2. Navigate to the project directory:

    ```bash
    cd <project-directory>
    ```

3. Start the Keycloak server and the application using Docker Compose:

    ```bash
    docker-compose up -d
    ```

    This command will start the Keycloak server and the application in detached mode.

4. Once the Keycloak server is running, you can obtain an access token by making a POST request to the Keycloak token endpoint with `application/x-www-form-urlencoded` body format.

    ```bash
    curl -X POST \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "username=manager" \
      -d "password=1234" \
      -d "client_id=user-management-api" \
      -d "client_secret=2zWPrsWvhVbNTB9hz3QhcPOWTibL8JsL" \
      -d "grant_type=password" \
      "http://localhost:9999/realms/user-management-realm/protocol/openid-connect/token"
    ```

    This command will retrieve the access token which can be used to authenticate requests to protected endpoints.


    This command will retrieve the access token which can be used to authenticate requests to protected endpoints.

5. Access the API documentation using Swagger UI by navigating to:

    [Swagger UI](http://localhost:8080/swagger-ui/index.html?continue#/)

    The Swagger UI provides an interactive documentation for exploring and testing the API endpoints.

