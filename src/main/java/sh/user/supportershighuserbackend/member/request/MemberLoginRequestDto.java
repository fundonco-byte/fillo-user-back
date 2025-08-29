package sh.user.supportershighuserbackend.member.request;

import lombok.Getter;
import sh.user.supportershighuserbackend.common.base.AbstractVO;

@Getter
public class MemberLoginRequestDto extends AbstractVO {
    private String loginId;
    private String password;
}
