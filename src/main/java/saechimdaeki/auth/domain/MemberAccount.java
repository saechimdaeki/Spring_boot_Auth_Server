package saechimdaeki.auth.domain;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberAccount extends User {

    private Member member;

    public MemberAccount(Member member) {
        super(member.getUserName(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+member.getRole())));
        this.member=member;
    }

}
