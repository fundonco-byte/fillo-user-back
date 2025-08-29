package sh.user.supportershighuserbackend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.user.supportershighuserbackend.member.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일 정보를 통한 회원 게정 조회
    Optional<Member> getMemberByEmail(String loginEmail);
}
