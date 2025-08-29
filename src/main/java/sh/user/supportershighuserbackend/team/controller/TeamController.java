package sh.user.supportershighuserbackend.team.controller;

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
import sh.user.supportershighuserbackend.share.ResponseBody;
import sh.user.supportershighuserbackend.team.service.TeamService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
@RestController
public class TeamController {

    private final TeamService teamService;

    // 회원가입 시 선택할 수 있도록 선택한 리그에 해당하는 전체 팀 정보 호출
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping (value = "/all", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getAllTeam(
            HttpServletRequest request, @RequestParam Long leagueId){
        log.info("[Team] 회원가입 시 선택할 수 있도록 선택한 리그에 해당하는 전체 팀 정보 호출");

        try{
            return new ResponseEntity<>(teamService.getAllTeam(leagueId), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, leagueId);
            return null;
        }
    }
}
