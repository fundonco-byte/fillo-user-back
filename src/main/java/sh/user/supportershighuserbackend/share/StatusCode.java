package sh.user.supportershighuserbackend.share;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // success response
    OK("정상 수행", "FO-200"),

    // bad response
    NOT_EXIST_ADMIN_ACCOUNT("관리자 계정이 존재하지 않습니다.", "FO-401"),
    CANT_REGIST_ADMIN_ACCOUNT("관리자 계정을 생성하실 수 없습니다.", "FO-402"),
    CANT_REGIST_USER_ACCOUNT("유저 계정을 생성하실 수 없습니다.", "FO-403"),
    EMPTY_LEAGUE_INFO("리그 정보가 존재하지 않습니다.", "FO-404"),
    EMPTY_TEAM_INTO("팀 정보들이 존재하지 않습니다.", "FO-405");


    private final String message;
    private final String code;
}
