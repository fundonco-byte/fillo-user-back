package sh.user.supportershighuserbackend.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sh.user.supportershighuserbackend.common.base.AbstractExceptionHandler;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.jwt.domain.JwtToken;
import sh.user.supportershighuserbackend.jwt.repository.JwtTokenRepository;
import sh.user.supportershighuserbackend.jwt.response.JwtTokenDto;
import sh.user.supportershighuserbackend.jwt.service.JwtTokenProvider;
import sh.user.supportershighuserbackend.league.domain.League;
import sh.user.supportershighuserbackend.league.repository.LeagueRepository;
import sh.user.supportershighuserbackend.mail.MailService;
import sh.user.supportershighuserbackend.media.service.MediaUpload;
import sh.user.supportershighuserbackend.member.domain.Member;
import sh.user.supportershighuserbackend.member.repository.MemberRepository;
import sh.user.supportershighuserbackend.member.request.MemberLoginRequestDto;
import sh.user.supportershighuserbackend.member.request.MemberRegistRequestDto;
import sh.user.supportershighuserbackend.member.request.MemberUpdateInfoRequestDto;
import sh.user.supportershighuserbackend.member.request.UpdatePasswordRequestDto;
import sh.user.supportershighuserbackend.member.response.MemberInfoResponseDto;
import sh.user.supportershighuserbackend.member.response.MemberLoginResponseDto;
import sh.user.supportershighuserbackend.member.response.MemberRegistResponseDto;
import sh.user.supportershighuserbackend.share.ResponseBody;
import sh.user.supportershighuserbackend.share.StatusCode;
import sh.user.supportershighuserbackend.team.domain.Team;
import sh.user.supportershighuserbackend.team.repository.TeamRepository;


import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
    private final MailService mailService;

    @Value("${active.host}")
    private String activeHost;

    // 고객 회원가입 service
    public ResponseBody registMember(MemberRegistRequestDto memberRegistRequestDto) throws IOException {
        log.info("고객 회원가입 service");

        try {
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
            
            if(memberRegistRequestDto.getLeagueId() != 0){
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
            }else{
                // 회원가입 정보 저장
                Member member = Member.builder()
                        .email(memberRegistRequestDto.getEmail())
                        .name(memberRegistRequestDto.getName())
                        .password(passwordEncoder.encode(memberRegistRequestDto.getPassword()))
                        .accountType("default")
                        .gender(memberRegistRequestDto.getGender())
                        .birthDate(memberRegistRequestDto.getBirthDate())
                        .leagueId(0L)
                        .leagueName("없음")
                        .teamId(0L)
                        .teamName("없음")
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
            
        } catch (Exception e) {
            LogUtil.logException(e, memberRegistRequestDto);
            return null;
        }
    }


    // 이메일 인증
    public ResponseBody authorizeEmail(String email) {
        log.info("이메일 인증 service");

        String authorizeValue = mailService.sendAuthorizeEmail(email);
        return new ResponseBody(StatusCode.OK, authorizeValue);
    }


    // 계정 로그인 service
    @Transactional
    public ResponseBody loginAccount(HttpServletResponse response, MemberLoginRequestDto memberLoginRequestDto) {
        log.info("계정 로그인 service");

        try {
            Optional<Member> loginMember = Optional.ofNullable(memberRepository.getMemberByEmail(memberLoginRequestDto.getEmail())
                    .orElseThrow(() -> new NullPointerException("")));

            // 로그인 시도 시 해당 계정이 존재하는지 확인
            if ((loginMember.isPresent() && !passwordEncoder.matches(memberLoginRequestDto.getPassword(), loginMember.get().getPassword())) ||
                    (loginMember.isPresent() && !loginMember.get().getLive().equals("y"))) {
                log.info("로그인 시도 시 해당 계정이 존재하지 않습니다.");
                LogUtil.logError("로그인 시도 시 해당 계정이 존재하지 않습니다.", memberLoginRequestDto);
                return new ResponseBody(StatusCode.NOT_EXIST_USER_ACCOUNT, null);
            }

            // 기존에 이전 토큰이 존재하는지 확인
            Optional<JwtToken> existCheckToken = jwtTokenRepository.getJwtTokenByMemberId(loginMember.get().getMemberId());

            // 잉여 토큰 존재 시 삭제 처리
            if (existCheckToken.isPresent()) {
                jwtTokenRepository.deleteJwtTokenByMemberId(loginMember.get().getMemberId());
            }

            // 토큰을 발급하고 Dto 개체에 저장하는 과정
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 추출한 JWT를 반환객체에 담기
            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication, "client");

            // 발급된 토큰 정보를 토대로 Token 엔티티에 input
            JwtToken token = JwtToken.builder()
                    .grantType(jwtTokenDto.getGrantType())
                    .refreshToken(jwtTokenDto.getRefreshToken())
                    .memberId(loginMember.get().getMemberId())
                    .build();

            // 토큰 저장
            jwtTokenRepository.save(token);

            // Response Header에 액세스 토큰 리프레시 토큰, 토큰 만료일 input
            response.addHeader("Authorization", "Bearer " + jwtTokenDto.getAccessToken());
            response.addHeader("RefreshToken", jwtTokenDto.getRefreshToken());
            response.addHeader("AccessTokenExpireTime", jwtTokenDto.getAccessTokenExpiresIn().toString());
            response.addHeader("RefreshTokenExpireTime", jwtTokenDto.getRefreshTokenExpiresIn().toString());

            return new ResponseBody(
                    StatusCode.OK,
                    MemberLoginResponseDto.builder()
                            .email(loginMember.get().getEmail())
                            .name(loginMember.get().getName())
                            .accessToken(activeHost.equals("LOCAL") ? "Bearer " + jwtTokenDto.getAccessToken() : "토큰 비공개")
                            .refreshToken(activeHost.equals("LOCAL") ? jwtTokenDto.getRefreshToken() : "토큰 비공개")
                            .build()
            );
        } catch (Exception e) {
            LogUtil.logException(e, response, memberLoginRequestDto);
            return null;
        }
    }


    // 계정 로그아웃 service
    @Transactional
    public ResponseBody logoutAccount(HttpServletRequest request, HttpServletResponse response) {
        log.info("계정 로그아웃 service");

        try {
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("토큰이 만료되었습니다.");
                LogUtil.logError("토큰이 만료되었습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.TOKEN_ISSUE, null);
            }

            // 현재 로그인 중인 인증 계정 조회
            Member loginMember = jwtTokenProvider.getMemberFromAuthentication();

            // 토큰이 정상적으로 존재하는지 확인
            Optional<JwtToken> existCheckToken = jwtTokenRepository.getJwtTokenByMemberId(loginMember.getMemberId());

            // 잉여 토큰 존재 시 삭제 처리
            if (existCheckToken.isPresent()) {
                jwtTokenRepository.deleteJwtTokenByMemberId(loginMember.getMemberId());
            }

            // Servlet 정보 삭제 및 Session에 넣어진 토큰 정보 삭제
            // 로그아웃 시 헤더에 넣어진 값들은 지울 수 없으므로
            // 이 API가 정상적으로 실행된 이후 앞단에서 헤더에 토큰 주입을 중단 처리 할 것
            request.getSession().invalidate();
            request.logout();
            SecurityContextHolder.clearContext();

            return new ResponseBody(StatusCode.OK, "정상적으로 로그아웃 되셨습니다.");
        } catch (Exception e) {
            LogUtil.logException(e, request, response);
            return null;
        }
    }


    // 마이페이지에 조회될 로그인 회원 정보 조회 service
    public ResponseBody getAccountInfo(HttpServletRequest request) {
        log.info("마이페이지에 조회될 로그인 회원 정보 조회 service");

        try {
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.TOKEN_ISSUE, null);
            }

            // 로그인한 회원 객체 조회
            Member authMember = jwtTokenProvider.getMemberFromAuthentication();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            return new ResponseBody(
                    StatusCode.OK,
                    MemberInfoResponseDto.builder()
                            .memberId(authMember.getMemberId())
                            .email(authMember.getEmail())
                            .nickName(authMember.getNickName())
                            .name(authMember.getName())
                            .accountType(authMember.getAccountType())
                            .address(authMember.getAddress())
                            .birthDate(authMember.getBirthDate())
                            .postalCode(authMember.getPostalCode())
                            .phone(authMember.getPhone())
                            .live(authMember.getLive())
                            .profileImage(authMember.getProfileImage())
                            .gender(authMember.getGender())
                            .leagueId(authMember.getLeagueId())
                            .leagueName(authMember.getLeagueName())
                            .teamId(authMember.getTeamId())
                            .teamName(authMember.getTeamName())
                            .personalInfoAgreement(authMember.getPersonalInfoAgreement())
                            .marketingAgreement(authMember.getMarketingAgreement())
                            .joinDate(authMember.getCreatedAt().format(formatter))
                            .build());
        } catch (Exception e) {
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 로그인한 유저의 자신의 정보 수정 service
    @Transactional
    public ResponseBody updateAccountInfo(HttpServletRequest request, MemberUpdateInfoRequestDto memberUpdateInfoRequestDto, MultipartFile profileImage){
        log.info("로그인한 유저의 자신의 정보 수정 service");

        try{
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.TOKEN_ISSUE, null);
            }

            // 로그인한 회원 객체 조회
            Member authMember = jwtTokenProvider.getMemberFromAuthentication();

            // 수정할 프로필 이미지 존재 시 수정
            if(profileImage != null || !profileImage.isEmpty()) {
                HashMap<String, String> profileImageUploadInfo = mediaUpload.uploadMemberMedia(profileImage);
                // 회원 정보 수정
                authMember.changeMemberInfo(memberUpdateInfoRequestDto, profileImageUploadInfo.get("mediaUrl"));
            }else{
                // 수정할 프로필 이미지 없을 시 회원 정보 수정
                authMember.changeMemberInfo(memberUpdateInfoRequestDto, null);
            }
            
            // 이전 비밀번호와 일치하는지 확인
//            if(passwordEncoder.matches(memberUpdateInfoRequestDto.getPassword(), authMember.getPassword())) {
//                log.info("이전 비밀번호와 동일합니다. 다시 입력해주십시오.");
//                LogUtil.logError("이전 비밀번호와 동일합니다. 다시 입력해주십시오.", memberUpdateInfoRequestDto.getPassword());
//                return new ResponseBody(StatusCode.CANT_UPDATE_PASSWORD, null);
//            }

            return new ResponseBody(StatusCode.OK, "정상적으로 수정되었습니다.");
        }catch (Exception e){
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 사전 등록한 유저수 조회 service
    public ResponseBody getPreRegistrationCount(){
        log.info("사전 등록한 유저수 조회 service");

        try{
            // 회원 가입되어 현재까지도 유지된 상태인 사전 등록 계정 수를 조회
            long preRegistrationCount = memberRepository.countByLive("y");

            return new ResponseBody(StatusCode.OK, preRegistrationCount);
        }catch (Exception e){
            LogUtil.logException(e);
            return null;
        }
    }


    // 비밀번호 수정
    public ResponseBody updateMemberPassword(HttpServletRequest request, UpdatePasswordRequestDto updatePasswordRequestDto){
        log.info("비밀번호 수정 service");

        try{
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 비밀번호를 수정할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 비밀번호를 수정할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.TOKEN_ISSUE, null);
            }

            // 입력한 비밀번호와 재확인용 비밀번호가 일치하지 않는지 확인
            if(!updatePasswordRequestDto.getPassword().equals(updatePasswordRequestDto.getCheckPassword())){
                log.info("입력한 비밀번호와 재확인용 비밀번호가 일치하지 않습니다.");
                LogUtil.logError("입력한 비밀번호와 재확인용 비밀번호가 일치하지 않습니다.", updatePasswordRequestDto.getPassword(), updatePasswordRequestDto.getCheckPassword());
                return new ResponseBody(StatusCode.DIDNT_MATCH_PASSWORD_AND_CHECKPASSWORD, null);
            }

            // 로그인한 회원 객체 조회
            Member authMember = jwtTokenProvider.getMemberFromAuthentication();

            // 비밀번호 수정
            authMember.changeMemberPassword(passwordEncoder.encode(updatePasswordRequestDto.getPassword()));

            return new ResponseBody(StatusCode.OK, "정상적으로 수정되었습니다.");
        }catch(Exception e){
            LogUtil.logException(e);
            return null;
        }
    }

}


