package sh.user.supportershighuserbackend.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.share.ResponseBody;
import sh.user.supportershighuserbackend.share.StatusCode;
import sh.user.supportershighuserbackend.team.domain.Team;
import sh.user.supportershighuserbackend.team.repository.TeamRepository;
import sh.user.supportershighuserbackend.team.response.RegistTeamResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    // 회원가입 시 선택할 수 있도록 선택한 리그에 해당하는 전체 팀 정보 호출 service
    public ResponseBody getAllTeam(Long leagueId) {
        log.info("회원가입 시 선택할 수 있도록 선택한 리그에 해당하는 전체 팀 정보 호출 service");

        // 선택한 리그에 해당하는 전체 팀 정보 호출
        List<Team> teams = teamRepository.findAllByLeagueId(leagueId);

        // 선택한 리그에 해당하는 전체 팀 정보가 존재하지 않는지 확인
        if (teams.isEmpty()) {
            LogUtil.logError(StatusCode.EMPTY_TEAM_INTO.getMessage(), teams);
            return new ResponseBody(StatusCode.EMPTY_TEAM_INTO, null);
        }

        // 호출한 전체 팀 정보를 반환 객체로 변환
        List<RegistTeamResponseDto> result = teams.stream()
                .map(eachTeam ->
                        RegistTeamResponseDto.builder()
                                .teamId(eachTeam.getTeamId())
                                .teamName(eachTeam.getName())
                                .build()
                ).collect(Collectors.toList());

        return new ResponseBody(StatusCode.OK, result);
    }
}
