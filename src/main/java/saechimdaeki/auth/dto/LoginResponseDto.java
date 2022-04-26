package saechimdaeki.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor @Builder
public class LoginResponseDto {
    private String userName;
    private String email;
    private String role;
}
