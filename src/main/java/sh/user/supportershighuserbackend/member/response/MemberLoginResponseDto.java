package sh.user.supportershighuserbackend.member.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberLoginResponseDto implements AutoCloseable{
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;

    @Override
    public void close() throws Exception {
        throw new Exception();
    }
}
