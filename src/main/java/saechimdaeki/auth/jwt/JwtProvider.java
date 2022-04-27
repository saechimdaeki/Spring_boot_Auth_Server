package saechimdaeki.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import saechimdaeki.auth.repository.MemberRepository;
import saechimdaeki.auth.service.MemberService;
import saechimdaeki.auth.service.RedisService;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component @Slf4j
public class JwtProvider {

    @Value("${token.secretKey}")
    private String secretKey;

    @Value("${token.accessTokenTime}")
    private int accessTokenValidTime;

    @Value("${token.refreshTokenTime}")
    private int refreshTokenValidTime;

    private Key key;

    private final RedisService redisService;
    private final MemberService memberService;

    @PostConstruct
    public void keygen(){
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJsonToken(String userName){
        Claims claims = Jwts.claims().setSubject(userName);
        return Jwts.builder()
            .setClaims(claims).setSubject(userName)
            .setExpiration(new Date(System.currentTimeMillis()+ accessTokenValidTime))
            .signWith(key,SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(){
        return Jwts.builder()
                   .setSubject("refresh")
            .setExpiration(new Date(System.currentTimeMillis()+refreshTokenValidTime))
            .signWith(key,SignatureAlgorithm.HS256)
            .compact();
    }

    public Authentication getAuthByAccessToken(String accessToken){
        UserDetails userDetails = memberService.loadUserByUsername(extractUserName(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public boolean validationToken(String jsonWebToken){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jsonWebToken);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public String extractUserName(String accessToken){
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(accessToken).getBody().getSubject();
    }

    public boolean hasRefreshToken(String token){
        return redisService.getUserName(token) != null;
    }

}
