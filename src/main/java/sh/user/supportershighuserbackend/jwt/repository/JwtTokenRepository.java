package sh.user.supportershighuserbackend.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.user.supportershighuserbackend.jwt.domain.JwtToken;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    // 기존에 남아있는 토큰 확인
    Optional<JwtToken> getJwtTokenByMemberId(Long memberId);

    // 로그인 시 기존에 토큰이 남아있을 경우 삭제 처리
    void deleteJwtTokenByMemberId(Long memberId);
}
