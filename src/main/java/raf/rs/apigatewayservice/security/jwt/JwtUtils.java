package raf.rs.apigatewayservice.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class JwtUtils {
    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    public boolean isTokenValid(String token){

        if (token.isEmpty()) return false;

        byte[] decodedSecret = Base64.getDecoder().decode(jwtSecret.getBytes());

        try {
            Jwts.parser()
                    .setSigningKey(decodedSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }
}
