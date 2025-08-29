package sh.user.supportershighuserbackend.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.user.supportershighuserbackend.aop.MethodCallMonitor;
import sh.user.supportershighuserbackend.aop.TimeMonitor;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.member.request.MemberRegistRequestDto;
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

}
