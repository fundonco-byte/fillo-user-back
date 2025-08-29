package sh.user.supportershighuserbackend.league.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.user.supportershighuserbackend.league.domain.League;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

    // 회원가입 시 선택할 수 있도록 전체 리그 정보 호출
    List<League> findAllBy();

    // 회원가입 시 유저가 선택한 리그 정보 호출
    League getLeagueByLeagueId(Long leagueId);
}
