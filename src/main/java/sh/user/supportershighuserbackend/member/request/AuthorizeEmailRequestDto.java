package sh.user.supportershighuserbackend.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthorizeEmailRequestDto {
    @NotBlank
    @Email(message = "이메일 정보가 옳바르지 않습니다.")
    private String email;
}
