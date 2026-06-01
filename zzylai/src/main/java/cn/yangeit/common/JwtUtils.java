package cn.yangeit.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    public static final String SECRET_KEY1 = "sdfsdadsfsdfsdffsdfdsadsdfsdadsfsdfsdffsdfdsadsdfsdadsfsdfsdffsdfdsadsdfsdadsfsdfsdffsdfdsadsdfsdadsfsdfsdffsdfdsad";
    private static final long EXPIRE = 30 * 24 * 60 * 60 * 1000L; // 30天过期时间

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY1)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY1)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args) {
        Map<String, Object> claims = Map.of("userId", 1, "username", "admin");
        String jwt = generateJwt(claims);
        System.out.println(jwt);

        Claims claims1 = parseJWT(jwt);
        System.out.println(claims1.get("userId"));
        System.out.println(claims1.get("username"));
    }
}