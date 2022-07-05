package com.video.modules.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtUtils {

    @Value("${my.jwt.expiration}")
    private long jwtExpiration;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/verification";
    private final String SECRET = "ArmanVideoMKeyAuth8503";

    public String generateToken(String username, Long id) {
        return TOKEN_PREFIX + Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 60*60*24*50*1000))
                .setId(id.toString())
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String getUsername(String token) {
        token = token.replace(TOKEN_PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserId(HttpServletRequest request) {
        String jwt = request.getHeader(HEADER_STRING);
        if (jwt != null) {
            if (jwt.startsWith(new JwtUtils().TOKEN_PREFIX)) {
                jwt = jwt.replace(TOKEN_PREFIX, "");
                return Long.parseLong(Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt).getBody().getId());
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
