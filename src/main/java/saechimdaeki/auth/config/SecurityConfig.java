package saechimdaeki.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import saechimdaeki.auth.filter.AuthFilter;
import saechimdaeki.auth.filter.JwtAuthFilter;
import saechimdaeki.auth.jwt.JwtProvider;
import saechimdaeki.auth.service.MemberService;
import saechimdaeki.auth.service.RedisService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtProvider jwtProvider;


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * TODO 조금씩 가닥 잡히면 권한 부여.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
            .antMatchers("/sign-up","/check-email-token","/test").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
            .addFilterBefore(new JwtAuthFilter(jwtProvider,redisService), UsernamePasswordAuthenticationFilter.class)
        .addFilter(getAuthFilter());
    }

    private AuthFilter getAuthFilter() throws Exception {
        AuthFilter authFilter = new AuthFilter(authenticationManager() , memberService,objectMapper,redisService,jwtProvider);
        authFilter.setAuthenticationManager(authenticationManager());
        return authFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
    }
}
