package saechimdaeki.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import saechimdaeki.auth.domain.MemberAccount;
import saechimdaeki.auth.dto.LoginDto;
import saechimdaeki.auth.dto.LoginResponseDto;
import saechimdaeki.auth.service.MemberService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    public AuthFilter(AuthenticationManager authenticationManager,
                      MemberService memberService,  ObjectMapper objectMapper) {
        super(authenticationManager);
        this.memberService = memberService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDto loginInfo = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getUserName(),loginInfo.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException("Login Io Error"+e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String username= ((MemberAccount)authResult.getPrincipal()).getUsername();
        LoginResponseDto loginResponseInfo = memberService.getLoginResponseInfo(username);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String result = objectMapper.writeValueAsString(loginResponseInfo);
        response.getWriter().write(result);
    }
}
