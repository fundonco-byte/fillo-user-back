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
    EMPTY_TEAM_INTO("팀 정보들이 존재하지 않습니다.", "FO-405"),
    NOT_EXIST_USER_ACCOUNT("계정이 존재하지 않습니다.", "FO-406"),
    CANT_GET_MEMBER_INFO("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.", "FO-407"),
    CANT_UPDATE_PASSWORD("이전 비밀번호와 동일합니다. 다시 입력해주십시오.", "FO-408"),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", "FO-409"),
    DIDNT_MATCH_PASSWORD_AND_CHECKPASSWORD("수정하고자 하는 비밀번호와 재확인 비밀번호가 일치하지 않습니다.", "FO-410"),

    // token issue
    TOKEN_ISSUE("토큰 이슈", "FO-999");


    private final String message;
    private final String code;
}
