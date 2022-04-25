package saechimdaeki.auth.dto;

import lombok.Getter;

@Getter
public class JoinMemberDto {
    private String userName;

    private String email;

    private String password;

    public void encodePassword(String newPassword){
        this.password=newPassword;
    }
}
