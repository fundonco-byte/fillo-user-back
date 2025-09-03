package sh.user.supportershighuserbackend.share;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sh.user.supportershighuserbackend.member.domain.Member;
import sh.user.supportershighuserbackend.member.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> loadUser = memberRepository.getMemberByEmail(email);

        if (loadUser.isPresent()) {
            log.info("유저 계정 존재 (고객) : {}", loadUser.get().getEmail());
            return createUserDetailsOfMember(loadUser.get());
        }else{
            log.info("유저 인증 계정 없음");
            return null;
        }
    }

    private UserDetails createUserDetailsOfMember(Member member) {
        log.info("회원용 createUserDetails 실행");

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles("client")
                .build();
    }

}