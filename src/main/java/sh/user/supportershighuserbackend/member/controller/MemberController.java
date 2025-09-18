package sh.user.supportershighuserbackend.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sh.user.supportershighuserbackend.aop.MethodCallMonitor;
import sh.user.supportershighuserbackend.aop.TimeMonitor;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.member.request.*;
import sh.user.supportershighuserbackend.member.service.MemberService;
import sh.user.supportershighuserbackend.share.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @MethodCallMonitor
    @TimeMonitor
    @PostMapping(value = "/regist", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> registAccount(
            HttpServletRequest request,
            @Valid @RequestBody MemberRegistRequestDto memberRegistRequestDto){
        log.info("[Member] 계정 회원 가입");

        try{
            return new ResponseEntity<>(memberService.registMember(memberRegistRequestDto), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, memberRegistRequestDto);
            return null;
        }
    }


    // 이메일 인증
    @MethodCallMonitor
    @TimeMonitor
    @PostMapping(value = "/email/authorize", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> authorizeEmail(
            HttpServletRequest request,
            @Valid @RequestBody AuthorizeEmailRequestDto authorizeEmailRequestDto){
        log.info("[Member] 이메일 인증");

        try{
            return new ResponseEntity<>(memberService.authorizeEmail(authorizeEmailRequestDto.getEmail()), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, authorizeEmailRequestDto);
            return null;
        }
    }


    // 로그인
    @MethodCallMonitor
    @TimeMonitor
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> loginAccount(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto){
        log.info("[Member] 로그인");

        try{
            return new ResponseEntity<>(memberService.loginAccount(response, memberLoginRequestDto), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, memberLoginRequestDto);
            return null;
        }
    }


    // 로그아웃
    @MethodCallMonitor
    @TimeMonitor
    @DeleteMapping (value = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> logoutAccount(
            HttpServletRequest request,
            HttpServletResponse response){
        log.info("[Member] 로그아웃");

        try{
            return new ResponseEntity<>(memberService.logoutAccount(request, response), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, response);
            return null;
        }
    }


    // 마이페이지에 조회될 로그인 회원 정보
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getAccountInfo(
            HttpServletRequest request){
        log.info("[Member] 마이페이지에 조회될 로그인 회원 정보");

        try{
            return new ResponseEntity<>(memberService.getAccountInfo(request), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 로그인한 유저의 자신의 정보 수정
    @MethodCallMonitor
    @TimeMonitor
    @PutMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseBody> updateAccountInfo(
            HttpServletRequest request,
            @RequestPart(required = false, name = "profileImage") MultipartFile profileImage,
            @Valid @RequestPart(name = "updateInfo") MemberUpdateInfoRequestDto memberUpdateInfoRequestDto){
        log.info("[Member] 로그인한 유저의 자신의 정보 수정");

        try{
            return new ResponseEntity<>(memberService.updateAccountInfo(request, memberUpdateInfoRequestDto, profileImage), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, memberUpdateInfoRequestDto);
            return null;
        }
    }


    // 사전 등록한 유저수 조회
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/pre-registration", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getPreRegistrationCount(){
        log.info("[Member] 사전 등록한 유저수 조회");

        try{
            return new ResponseEntity<>(memberService.getPreRegistrationCount(), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e);
            return null;
        }
    }


    // 비밀번호 수정
    @MethodCallMonitor
    @TimeMonitor
    @PutMapping(value = "/update/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> updateMemberPassword(
            HttpServletRequest request,
            @Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto){
        log.info("[Member] 비밀번호 수정");

        try{
            return new ResponseEntity<>(memberService.updateMemberPassword(request, updatePasswordRequestDto), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, updatePasswordRequestDto);
            return null;
        }
    }


}
