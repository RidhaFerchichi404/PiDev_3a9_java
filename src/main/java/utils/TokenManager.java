package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import entities.User;

import java.util.Date;

public class TokenManager {
    private static final String SECRET_KEY = "your-secret-key"; // Replace with a strong secret key
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 minutes

    // Generate a token for the user
    public static String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail()) // Use email as the subject
                .withClaim("role", user.getRole()) // Add custom claims (e.g., role)
                .withIssuedAt(new Date()) // Token creation time
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token expiration time
                .sign(ALGORITHM); // Sign the token
    }

    // Validate and decode the token
    public static DecodedJWT validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            return verifier.verify(token); // Throws JWTVerificationException if invalid
        } catch (JWTVerificationException e) {
            throw new SecurityException("Invalid or expired token");
        }
    }

    // Get the user email from the token
    public static String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getSubject();
    }

    // Get the user role from the token
    public static String getRoleFromToken(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getClaim("role").asString();
    }
}