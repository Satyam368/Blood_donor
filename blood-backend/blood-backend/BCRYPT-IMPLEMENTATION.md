# BCrypt Password Security Implementation

## Overview
This document describes the implementation of secure password storage using BCrypt hashing in the Blood Backend application.

## What Changed

### 1. Password Configuration
- Added `PasswordConfig.java` with BCrypt password encoder bean
- BCrypt automatically handles salt generation and secure hashing

### 2. Services Updated
- `UserSignupService`: Now hashes passwords before saving and verifies them during login
- `UserService`: Updated to hash passwords on registration and verify hashed passwords on login

### 3. Repository Changes
- Removed `findByEmailAndPassword` method from `UserSignupRepository` since we can't query by hashed passwords directly
- Login now uses `findByEmail` and then verifies the password using BCrypt

## How It Works

### Registration Process
1. User submits registration form with plain text password
2. `UserSignupService.registerUser()` calls `passwordEncoder.encode(password)`
3. BCrypt generates a unique salt and hashes the password
4. Hashed password (with salt) is stored in database

### Login Process
1. User submits login form with plain text password
2. Service finds user by email using `findByEmail()`
3. Uses `passwordEncoder.matches(plainPassword, hashedPassword)` to verify
4. BCrypt extracts salt from stored hash and compares

### Example Password Hashes
```
Plain password: "mySecurePassword123!"
BCrypt hash: "$2a$10$N9qo8uLOickgx2ZMRZoMye/FVvf6/mJaBQfj.Nvexl/Rh7O6S9q5."

Plain password: "anotherPassword456@"
BCrypt hash: "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi"
```

## Security Benefits

1. **Salt Protection**: Each password gets a unique salt, preventing rainbow table attacks
2. **Adaptive Cost**: BCrypt uses configurable rounds (default 10) making it computationally expensive
3. **No Plaintext Storage**: Passwords are never stored in readable form
4. **Industry Standard**: BCrypt is a widely trusted and battle-tested algorithm

## Testing

### Manual Testing
1. Start the backend application
2. Register a new user via `/api/signup` endpoint
3. Check database - password should be hashed (starts with `$2a$10$`)
4. Login with the same credentials via `/api/users/login`
5. Verify successful authentication

### API Examples

**Registration:**
```bash
POST http://localhost:8080/api/signup
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890"
}
```

**Login:**
```bash
POST http://localhost:8080/api/users/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "SecurePass123!"
}
```

## Files Modified

1. `src/main/java/com/example/blood_backend/config/PasswordConfig.java` (NEW)
2. `src/main/java/com/example/blood_backend/service/UserSignupService.java`
3. `src/main/java/com/example/blood_backend/service/UserService.java`
4. `src/main/java/com/example/blood_backend/repository/UserSignupRepository.java`
5. `src/main/java/com/example/blood_backend/dto/LoginRequest.java` (NEW)
6. `src/main/java/com/example/blood_backend/controller/UserController.java`

## Migration Notes

- **Existing Users**: If you have existing users with plain text passwords, they will need to re-register or you'll need a migration script
- **Password Validation**: Frontend validation remains the same - BCrypt hashing happens on the server side
- **Performance**: BCrypt is intentionally slow for security - each hash/verify operation takes ~100ms

## Next Steps

1. Consider adding password strength requirements on the frontend
2. Implement password reset functionality with secure token generation
3. Add rate limiting for login attempts to prevent brute force attacks
4. Consider implementing JWT tokens for session management