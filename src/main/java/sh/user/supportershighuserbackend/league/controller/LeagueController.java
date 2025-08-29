package sh.user.supportershighuserbackend.league.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.user.supportershighuserbackend.aop.MethodCallMonitor;
import sh.user.supportershighuserbackend.aop.TimeMonitor;
import sh.user.supportershighuserbackend.common.util.LogUtil;
import sh.user.supportershighuserbackend.league.service.LeagueService;
import sh.user.supportershighuserbackend.share.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/league")
@RestController
public class LeagueController {

    private final LeagueService leagueService;

    // 회원가입 시 선택할 수 있도록 전체 리그 정보 호출
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/all", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getAllLeague(
            HttpServletRequest request){
        log.info("[League] 회원가입 시 선택할 수 있도록 전체 리그 정보 호출");

        try{
            return new ResponseEntity<>(leagueService.getAllLeague(), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request);
            return null;
        }
    }
}
