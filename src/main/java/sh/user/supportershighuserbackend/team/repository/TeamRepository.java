package sh.user.supportershighuserbackend.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.user.supportershighuserbackend.team.domain.Team;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // 회원가입 시 선택할 수 있도록 선택한 리그에 해당하는 전체 팀 정보 호출
    List<Team> findAllByLeagueId(Long leagueId);

    // 회원가입 시 유저가 선택한 팀 정보 호출
    Team getTeamByTeamId(Long teamId);
}
