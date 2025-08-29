package sh.user.supportershighuserbackend.league.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.league.domain.League;
import sh.user.supportershighuserbackend.league.repository.LeagueRepository;
import sh.user.supportershighuserbackend.league.response.RegistLeagueResponseDto;
import sh.user.supportershighuserbackend.share.ResponseBody;
import sh.user.supportershighuserbackend.share.StatusCode;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;

    // 회원가입 시 선택할 수 있도록 전체 리그 정보 호출 service
    public ResponseBody getAllLeague() {
        log.info("회원가입 전체 리그 정보 호출 service");

        // 전체 리그 정보 호출
        List<League> allLeague = leagueRepository.findAllBy();

        if (allLeague.isEmpty()) {
            LogUtil.logError(StatusCode.EMPTY_LEAGUE_INFO.getMessage(), allLeague);
            return new ResponseBody(StatusCode.EMPTY_LEAGUE_INFO, null);
        }

        // 호출한 전체 리그 정보들을 반환 객체로 변환
        List<RegistLeagueResponseDto> result = allLeague.stream()
                .map(eachLeague ->
                        RegistLeagueResponseDto.builder()
                                .leagueId(eachLeague.getLeagueId())
                                .leagueName(eachLeague.getName())
                                .build()
                ).collect(Collectors.toList());

        return new ResponseBody(StatusCode.OK, result);
    }
}
