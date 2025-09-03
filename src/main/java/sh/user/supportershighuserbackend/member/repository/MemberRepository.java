package sh.user.supportershighuserbackend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.user.supportershighuserbackend.member.domain.Member;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일 정보를 통한 회원 계정 조회
    Optional<Member> getMemberByEmail(String loginEmail);

    // 사전 등록한 회원 수 조회
    long countByLive(String live);
}
