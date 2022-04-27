package saechimdaeki.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import saechimdaeki.auth.jwt.JwtProvider;
import saechimdaeki.auth.service.RedisService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
                                                                                                                       ServletException,
                                                                                                                       IOException {

        String accessToken = request.getHeader("accessToken");
        String refreshToken = request.getHeader("refreshToken");

        log.info("\n accessToken = {} \n refreshToken = {}",accessToken,refreshToken);
        if(accessToken!=null){
            if(jwtProvider.validationToken(accessToken)){
                SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthByAccessToken(accessToken));
            }else if(!jwtProvider.validationToken(accessToken) && refreshToken!=null){
                boolean validRefreshToken = jwtProvider.validationToken(refreshToken);
                boolean redisHasRefreshToken = jwtProvider.hasRefreshToken(refreshToken);
                if(validRefreshToken && redisHasRefreshToken){
                    String userName = redisService.getUserName(refreshToken);
                    log.info("userName {}",userName);
                    String newAccessJwt = jwtProvider.generateJsonToken(userName);
                    response.setHeader("accessToken",newAccessJwt);
                    SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthByAccessToken(newAccessJwt));

                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
