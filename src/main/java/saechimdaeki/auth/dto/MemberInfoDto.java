package saechimdaeki.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberInfoDto {
    private final String userName;
    private final String email;
    private String role;
}
