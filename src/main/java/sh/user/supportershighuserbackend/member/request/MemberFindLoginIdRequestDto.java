package sh.user.supportershighuserbackend.member.request;

import lombok.Getter;

@Getter
public class MemberFindLoginIdRequestDto {
    private String email;
    private String phone;
}
