package sh.user.supportershighuserbackend.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sh.user.supportershighuserbackend.common.base.AbstractExceptionHandler;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.jwt.JwtTokenProvider;
import sh.user.supportershighuserbackend.jwt.JwtTokenRepository;
import sh.user.supportershighuserbackend.league.domain.League;
import sh.user.supportershighuserbackend.league.repository.LeagueRepository;
import sh.user.supportershighuserbackend.media.service.MediaUpload;
import sh.user.supportershighuserbackend.member.domain.Member;
import sh.user.supportershighuserbackend.member.repository.MemberRepository;
import sh.user.supportershighuserbackend.member.request.MemberRegistRequestDto;
import sh.user.supportershighuserbackend.member.response.MemberRegistResponseDto;
import sh.user.supportershighuserbackend.share.ResponseBody;
import sh.user.supportershighuserbackend.share.StatusCode;
import sh.user.supportershighuserbackend.team.domain.Team;
import sh.user.supportershighuserbackend.team.repository.TeamRepository;


import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService extends AbstractExceptionHandler {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private final MediaUpload mediaUpload;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    // 고객 회원가입 service
    public ResponseBody registMember(MemberRegistRequestDto memberRegistRequestDto) throws IOException {
        log.info("고객 회원가입 service");

        HashMap<String, String> checkData = new HashMap<>();
        
        // 회원가입 정보 확인
        String checkInfo = checkRegistInfo(memberRegistRequestDto);

        // 회원가입 정보 확인 후 옳바르지 않은 정보라면 예외 처리
        if (checkInfo != null) {
            checkData.put("회원가입 정보", checkInfo);
            LogUtil.logError(StatusCode.CANT_REGIST_USER_ACCOUNT.getMessage(), memberRegistRequestDto, checkData);
            return new ResponseBody(StatusCode.CANT_REGIST_USER_ACCOUNT, checkInfo);
        }

        // 비밀번호와 재확인 비밀번호 매칭 확인
        String checkPassword = checkRightPassword(memberRegistRequestDto.getPassword(), memberRegistRequestDto.getCheckPassword());

        // 회원가입 비밀번호가 옳바르지 않을 경우 예외 처리
        if (checkPassword != null) {
            checkData.put("비밀번호 일치 여부", checkPassword);
            LogUtil.logError(StatusCode.CANT_REGIST_USER_ACCOUNT.getMessage(), memberRegistRequestDto, checkData);
            return new ResponseBody(StatusCode.CANT_REGIST_USER_ACCOUNT, checkPassword);
        }

        // 이미 존재한 이메일의 계정이 있는지 확인
        Optional<Member> alreadyExistMember = memberRepository.getMemberByEmail(memberRegistRequestDto.getEmail());

        // 이미 존재한 계정이 있을 경우 예외 처리
        if (alreadyExistMember.isPresent()) {
            checkData.put("계정 중복 여부", "이미 존재한 이메일 계정입니다.");
            LogUtil.logError(StatusCode.CANT_REGIST_USER_ACCOUNT.getMessage(), memberRegistRequestDto, checkData);
            return new ResponseBody(StatusCode.CANT_REGIST_USER_ACCOUNT, "이미 존재한 이메일 계정입니다.");
        }

        // 선호하는 리그 정보 호출
        League selectLeague = leagueRepository.getLeagueByLeagueId(memberRegistRequestDto.getLeagueId());
        // 선호하는 팀 정보 호출
        Team selectTeam = teamRepository.getTeamByTeamId(memberRegistRequestDto.getTeamId());

        // 회원가입 정보 저장
        Member member = Member.builder()
                .email(memberRegistRequestDto.getEmail())
                .name(memberRegistRequestDto.getName())
                .password(passwordEncoder.encode(memberRegistRequestDto.getPassword()))
                .accountType("default")
                .gender(memberRegistRequestDto.getGender())
                .birthDate(memberRegistRequestDto.getBirthDate())
                .leagueId(selectLeague.getLeagueId())
                .leagueName(selectLeague.getName())
                .teamId(selectTeam.getTeamId())
                .teamName(selectTeam.getName())
                .live("y")
                .personalInfoAgreement(memberRegistRequestDto.getPersonalInfoAgreement())
                .marketingAgreement(memberRegistRequestDto.getMarketingAgreement())
                .build();

        Member registMember = memberRepository.save(member);

        return new ResponseBody(
                StatusCode.OK,
                MemberRegistResponseDto.builder()
                        .memberId(registMember.getMemberId())
                        .email(registMember.getEmail())
                        .nickName(registMember.getNickName() == null ? "" : registMember.getNickName())
                        .name(registMember.getName())
                        .accountType(registMember.getAccountType())
                        .address(registMember.getAddress() == null ? "" : registMember.getAddress())
                        .birthDate(registMember.getBirthDate())
                        .postalCode(registMember.getPostalCode() == null ? "" : registMember.getPostalCode())
                        .phone(registMember.getPhone() == null ? "" : registMember.getPhone())
                        .live(registMember.getLive())
                        .profileImage(registMember.getProfileImage() == null ? "" : registMember.getProfileImage())
                        .gender(registMember.getGender())
                        .leagueId(registMember.getLeagueId())
                        .leagueName(registMember.getLeagueName())
                        .teamId(registMember.getTeamId())
                        .teamName(registMember.getTeamName())
                        .personalInfoAgreement(registMember.getPersonalInfoAgreement())
                        .marketingAgreement(registMember.getMarketingAgreement())
                        .build());
    }


    // 고객 계정 로그인 service
//    @Transactional(transactionManager = "MasterTransactionManager")
//    public MemberLoginResponseDto loginMember(HttpServletResponse response, MemberLoginRequestDto memberLoginRequestDto) {
//        log.info("고객 계정 로그인 service");
//
//        // 로그인 시도 시 해당 계정이 존재하는지 확인
//        if (memberException.checkLoginAccount(memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword())) {
//            log.info("로그인 시도 시 해당 계정이 존재하지 않습니다.");
//            LogUtil.logError("로그인 시도 시 해당 계정이 존재하지 않습니다.", memberLoginRequestDto);
//            return null;
//        }
//
//        // 고객 계정 호출
//        Member loginMember = memberQueryData.getMember(memberLoginRequestDto.getLoginId());
//
//        // 만약 회원탈퇴 상태인 계정의 경우 예외 처리
//        if (loginMember.getStatus().equals("N")) {
//            log.info("회원 탈퇴된 계정입니다.");
//            LogUtil.logError("회원 탈퇴된 계정입니다.", memberLoginRequestDto);
//            return null;
//        }
//
//        // 토큰을 구분할 mappingAccount 변수 저장
//        String mappingAccount = loginMember.getMemberId() + ":" + loginMember.getLoginId();
//
//        // 기존에 이전 토큰이 존재하면 삭제
//        jwtTokenQueryData.deletePrevToken(mappingAccount, loginMember.getType());
//
//        // 토큰을 발급하고 Dto 개체에 저장하는 과정
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginMember.getType() + "-" + memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword());
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//        if (loginMember.getType().equals("C")) {
//
//            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication, "client");
//
//            // 발급된 토큰 정보를 토대로 Token 엔티티에 input
//            JwtToken token = JwtToken.builder()
//                    .grantType(jwtTokenDto.getGrantType())
//                    .accessToken(jwtTokenDto.getAccessToken())
//                    .refreshToken(jwtTokenDto.getRefreshToken())
//                    .mappingAccount(mappingAccount)
//                    .type(loginMember.getType())
//                    .build();
//
//            // 토큰 저장
//            jwtTokenRepository.save(token);
//
//            // Response Header에 액세스 토큰 리프레시 토큰, 토큰 만료일 input
//            response.addHeader("Authorization", "Bearer " + token.getAccessToken());
//            response.addHeader("RefreshToken", token.getRefreshToken());
//            response.addHeader("AccessTokenExpireTime", jwtTokenDto.getAccessTokenExpiresIn().toString());
//
//        } else if (loginMember.getType().equals("B")) {
//
//            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication, "business");
//
//            // 발급된 토큰 정보를 토대로 Token 엔티티에 input
//            JwtToken token = JwtToken.builder()
//                    .grantType(jwtTokenDto.getGrantType())
//                    .accessToken(jwtTokenDto.getAccessToken())
//                    .refreshToken(jwtTokenDto.getRefreshToken())
//                    .mappingAccount(mappingAccount)
//                    .type(loginMember.getType())
//                    .build();
//
//            // 토큰 저장
//            jwtTokenRepository.save(token);
//
//            // Response Header에 액세스 토큰 리프레시 토큰, 토큰 만료일 input
//            response.addHeader("Authorization", "Bearer " + token.getAccessToken());
//            response.addHeader("RefreshToken", token.getRefreshToken());
//            response.addHeader("AccessTokenExpireTime", jwtTokenDto.getAccessTokenExpiresIn().toString());
//        }
//
//        return MemberLoginResponseDto.builder()
//                .loginId(loginMember.getLoginId())
//                .type(loginMember.getType())
//                .userName(loginMember.getUserName())
//                .build();
//    }

}


