package saechimdaeki.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class JoinMemberDto {
    private String userName;

    private String email;

    private String password;

    public void encodePassword(String newPassword){
        this.password=newPassword;
    }


}
