package saechimdaeki.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder @Getter
@ToString
public class EmailMessage {
    private final String to;
    private final String from;
    private final String subject;
    private final String body;

    public static EmailMessage sendSignUpEmail(MemberDto memberDto){
        return EmailMessage.builder()
                           .to(memberDto.getEmail())
                           .from("saechimdaeki_Auth_Server_Service")
                           .subject(memberDto.getUserName()+"님 회원가입을 축하합니다")
                           .body(memberDto.getEmailCheckToken())
                           .build();
    }
}
