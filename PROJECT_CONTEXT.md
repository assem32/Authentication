# Project Context

## Purpose

Spring Boot authentication API with PostgreSQL, Spring Security, BCrypt password hashing, and email confirmation through a local MailDev SMTP server.

This file is a compact reference for future development. Do not put real passwords, database credentials, session IDs, or confirmation-token values in this file.

## Main Components

- `RegisterationController`: registration, confirmation, and custom JSON login endpoints.
- `RegisterationService`: coordinates registration, email confirmation, and authentication.
- `AppUserService`: loads users by email, hashes passwords, saves users, and creates confirmation tokens.
- `Users`: JPA entity and Spring Security `UserDetails` implementation.
- `ConfirmationTokenService` and `ConfirmationTokenRepo`: store and update email tokens.
- `SecurityConfig`: permits registration URLs, protects all other URLs, and enables form login.
- `EmailService`: sends HTML confirmation email through SMTP.

The code consistently spells the package and classes as `registeration`; preserve that spelling until a deliberate project-wide rename.

## Registration Flow

Endpoint: `POST /api/v1/registration`

```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "password": "example-password"
}
```

1. `RegisterationController.register` passes the JSON body to `RegisterationService.register`.
2. `EmailValidator` checks the email. It currently always returns `true`.
3. `AppUserService.signUpUser` rejects an existing email.
4. BCrypt hashes the password before the user is saved.
5. The new user starts with `enabled=false`, so login is blocked before confirmation.
6. A random UUID confirmation token is stored with a 15-minute expiration.
7. An activation link is sent through MailDev.
8. The endpoint currently returns the raw confirmation token. This is useful for development but should not be exposed in production.

## Email Confirmation Flow

Endpoint: `GET /api/v1/registration/confirm?token=<confirmation-token>`

1. The token is loaded from PostgreSQL.
2. Already-confirmed and expired tokens are rejected.
3. `confirmedAt` is set to the current time.
4. The associated user is changed to `enabled=true`.
5. The endpoint returns `confirmed`.

The confirmation token activates an account. It is not a login token or JWT.

## Login Flow

Custom endpoint: `POST /api/v1/registration/login`

```json
{
  "email": "test@example.com",
  "password": "example-password"
}
```

1. `AuthenticationManager` receives the email and plain-text password.
2. `AppUserService.loadUserByUsername` looks up the database user by email.
3. `DaoAuthenticationProvider` compares the submitted password with the BCrypt hash.
4. Spring Security also rejects disabled or locked users.
5. Successful authentication is placed in `SecurityContextHolder` and the endpoint returns `User logged in successfully!`.

There is no JWT implementation. Login is intended to be session-based. The custom JSON endpoint does not explicitly save the security context to a session repository, so authentication may not survive the next request. Spring Security's built-in `POST /login` form endpoint does persist session authentication and expects form fields named `username` and `password`; the `username` value must contain the user's email.

## Security Rules

- `/api/v*/registration/**` is public, including registration, confirmation, and the custom login endpoint.
- Every other URL requires authentication.
- CSRF protection is disabled.
- Form login is enabled.
- Roles are `USER` and `ADMIN`; authorities currently have no `ROLE_` prefix.

## Local Services

- PostgreSQL: `localhost:5432`, database `users`.
- MailDev SMTP: `localhost:1025`.
- Application: normally `http://localhost:8080`.

Use `application.properties` or environment-backed configuration for local credentials. Never add real secrets to this context file or commit production secrets.

## Known Issues

- `Users.getUsername()` returns the person's full name, while authentication lookup uses email. Returning email would make the security identity consistent.
- The custom JSON login may not persist authentication between requests.
- `EmailValidator` accepts every value.
- Registration DTOs have no Bean Validation constraints.
- Raw confirmation tokens are returned from registration.
- Exceptions currently become generic server errors rather than structured API responses.
- CSRF is disabled even though authentication is session-based.
- Database credentials are stored directly in `application.properties`.
- The email sender logs only the exception message and says `Could send` when sending fails.
- There are no meaningful authentication-flow tests yet.

## Safe Test Sequence

1. Start PostgreSQL and create the `users` database.
2. Start MailDev on SMTP port `1025`.
3. Run the application with `./mvnw spring-boot:run`.
4. Register using `POST /api/v1/registration`.
5. Open the activation link in MailDev, or call the confirmation endpoint with the development token.
6. Login only after confirmation.
7. For persistent session testing, use the built-in form login and retain the returned `JSESSIONID` cookie.
