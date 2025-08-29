package sh.user.supportershighuserbackend.league.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistLeagueResponseDto {
    private Long leagueId;
    private String leagueName;
}
