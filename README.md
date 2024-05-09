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
   - Search users by birthdate range: Returns a list of users within the specified birth date range. Validates that the "From" date is less than the "To" date.

3. **Unit Tests**: The code is covered by unit tests using Spring framework.

4. **Error Handling**: The code has error handling for REST operations.

5. **API Responses**: Responses are in JSON format.

6. **Data Persistence**: Database usage is not necessary; the data persistence layer is not required.

7. **Spring Boot Version**: Any version of Spring Boot can be used.

8. **Technologies**:
   - Lombok
   - ModelMapper
   - Postgresql Database
   - Spring Data Envers
   - Spring Boot Starter Web
   - Spring Boot Starter Test
   - Spring Boot Starter Security
   - Spring Boot Starter Data JPA
   - Spring Boot Starter Validation
   - Springdoc OpenAPI Starter WebMvc UI
   - Spring Boot Starter OAuth2 Resource Server

10. **Authentication**: Keycloak is used for authentication. For Keycloak, MySQL is used:

    ```yaml
    mysql-kc:
      image: mysql:8.0.27
      ports:
        - "3366:3306"
      restart: unless-stopped
      environment:
        MYSQL_USER: keycloak_user
        MYSQL_PASSWORD: keycloak_password
        MYSQL_DATABASE: keycloak_db
        MYSQL_ROOT_PASSWORD: root_password
    ```

11. **Deployment**: Docker Compose is used for deployment. For the main service, PostgreSQL is used:

    ```yaml
    project-db:
      image: postgres:14.7-alpine
      environment:
        POSTGRES_USER: username
        POSTGRES_PASSWORD: password
        POSTGRES_DB: user-management-db
      ports:
        - "5432:5432"
      restart: unless-stopped
    ```

## Getting Started

1. Clone this repository to your local machine:

    ```bash
    git clone https://github.com/defire04/user-management-api.git
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

    ```http request
   POST http://localhost:9999/realms/user-management-realm/protocol/openid-connect/token
   Content-Type: application/x-www-form-urlencoded
   
   username=manager&password=1234&client_id=user-management-api&client_secret=2zWPrsWvhVbNTB9hz3QhcPOWTibL8JsL&grant_type=password
    ```

   The resulting JSON should look like this
   ```json
   {
      "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDWmd2cHlyR0xsdWdLR19CVU5hV1hDQUhoSUZhNjdLNzgyZnlvbGNzdGI0In0.eyJleHAiOjE3MTUyODMwMjMsImlhdCI6MTcxNTI3OTQyMywianRpIjoiNjAyZGVhOGYtN2M2OS00NDIzLWJmZDQtNWJkMmRkNmNkYjk2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5OTk5L3JlYWxtcy91c2VyLW1hbmFnZW1lbnQtcmVhbG0iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMDc0OGQyNDYtYjBjNy00YmJlLTljNDgtY2FmMmE4ZDUyN2Y4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidXNlci1tYW5hZ2VtZW50LWFwaSIsInNlc3Npb25fc3RhdGUiOiJhMTI0M2ViNS1lMjQ2LTQ5YTItOTEzNy02YTE5OWU1MzRkMmMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy11c2VyLW1hbmFnZW1lbnQtcmVhbG0iLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiYTEyNDNlYjUtZTI0Ni00OWEyLTkxMzctNmExOTllNTM0ZDJjIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtYW5hZ2VyIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.Gtb2L3dI9tUchm4t3YTfJeJsE_6Vpp483ulTTPJfn4C-JB3M6sAnQ62RiI4BBa39VibiFUskCm1U7vsXGSMB0TUaA18Xat4P0Kcd5O_Po0vSwKG_4HoO82QCboeC7KbcxaituVX6vgmSpW_hvYs-wkUofDIcWwhUjU8Ji1hntu-WGU2qOKX-h32CTFpEYDV47oOAjlT2EegJflvXH704xs36rFpbgVUGrq_mZ8RnZkoquIqSA-44f2rJAhauE-pm3dmzUawaRNCwyds-keCDj5-qnBLFghyEDTBl67Zn8LndW6ouSa0IFYL6YkvbQL0I671u4WukCcX9SPCQHrMbkw",
      "expires_in": 3600,
      "refresh_expires_in": 1800,
      "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmNzZlODY5Yy0xNjYzLTQ4NDUtYTFkNi1kZmJmNzVjNzcyZDgifQ.eyJleHAiOjE3MTUyODEyMjMsImlhdCI6MTcxNTI3OTQyMywianRpIjoiMDg4ZGVkNjMtODk2Ni00Yzg1LWFjYWQtMGNhMWU1NzM1OTdmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5OTk5L3JlYWxtcy91c2VyLW1hbmFnZW1lbnQtcmVhbG0iLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0Ojk5OTkvcmVhbG1zL3VzZXItbWFuYWdlbWVudC1yZWFsbSIsInN1YiI6IjA3NDhkMjQ2LWIwYzctNGJiZS05YzQ4LWNhZjJhOGQ1MjdmOCIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJ1c2VyLW1hbmFnZW1lbnQtYXBpIiwic2Vzc2lvbl9zdGF0ZSI6ImExMjQzZWI1LWUyNDYtNDlhMi05MTM3LTZhMTk5ZTUzNGQyYyIsInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6ImExMjQzZWI1LWUyNDYtNDlhMi05MTM3LTZhMTk5ZTUzNGQyYyJ9.48IlGe1DNSnJkkRjgCVl8OTBcMcMpgcke0fg4xBGXfA",
      "token_type": "Bearer",
      "not-before-policy": 0,
      "session_state": "a1243eb5-e246-49a2-9137-6a199e534d2c",
      "scope": "email profile"
   }
   ```

    This command will retrieve the access token which can be used to authenticate requests to protected endpoints.


5. Access the API documentation using Swagger UI by navigating to:

    [Swagger UI](http://localhost:8888/swagger-ui/index.html?continue#/)

    The Swagger UI provides an interactive documentation for exploring and testing the API endpoints.

6. To access the Keycloak administration console, navigate to:

   [Keycloak Admin Console](http://localhost:9999/admin/master/console/#/user-management-realm)

   You can log in with the following credentials:
   - Username: root
   - Password: 1