package saechimdaeki.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saechimdaeki.auth.dto.JoinMemberDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * 간단한 AuthServer만 구현 할 것이므로 엔티티는 최소화.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String userName;

    private String email;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken; //가입 인증을 위함.

    private String role; //

    public void createRandomEmailToken(){
        this.emailCheckToken = UUID.randomUUID().toString().substring(0,8);
    }

    public void changeEmailVerified(){
        this.emailVerified=true;
    }

    @Builder
    public Member(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public static Member joinMember(JoinMemberDto memberDto){
        return Member.builder()
                     .email(memberDto.getEmail())
                     .password(memberDto.getPassword())
                     .userName(memberDto.getUserName())
                     .build();
    }
}
