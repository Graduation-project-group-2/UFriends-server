package com.gachon.ufriendsserver.api.common.config;

import com.gachon.ufriendsserver.api.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String create(Member member){
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET_KEY).setSubject(String.valueOf(member.getMemberId())).setIssuer("UFriends").setIssuedAt(new Date()).setExpiration(expiryDate).compact();
    }

    public String validateAndGetUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}