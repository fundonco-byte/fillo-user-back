package sh.user.supportershighuserbackend.member.request;

import lombok.Getter;

@Getter
public class MemberFindPasswordRequestDto {
    private String loginId;
    private String email;
    private String phone;
}
