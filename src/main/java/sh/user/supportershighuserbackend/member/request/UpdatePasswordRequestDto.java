package sh.user.supportershighuserbackend.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sh.user.supportershighuserbackend.common.base.AbstractVO;

@Getter
public class UpdatePasswordRequestDto extends AbstractVO {
    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String password;
    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String checkPassword;
}
