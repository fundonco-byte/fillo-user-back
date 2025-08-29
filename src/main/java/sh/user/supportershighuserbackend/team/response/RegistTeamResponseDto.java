package sh.user.supportershighuserbackend.team.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistTeamResponseDto {
    private Long teamId;
    private String teamName;
}
