package saechimdaeki.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder @Getter
public class MemberDto {

    private String userName;

    private String email;

    private String emailCheckToken; //가입 인증을 위함.

}
