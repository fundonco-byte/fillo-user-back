package sh.user.supportershighuserbackend.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sh.user.supportershighuserbackend.common.base.AbstractVO;

@Getter
public class MemberRegistRequestDto extends AbstractVO {
    @NotBlank
    @Email(message = "이메일 정보가 옳바르지 않습니다.")
    private String email; // 로그인 아이디
    //    @NotBlank
//    @Size(min=2, message="닉네임은 최소 2자 이상이여야 합니다.")
//    private String nickName; // 닉네임
    @NotBlank
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이여야 합니다.")
    private String password; // 비밀번호
    @NotBlank
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이여야 합니다.")
    private String checkPassword; // 재확인용 비밀번호
    //    private String accountType; // 계정 유형 (예 : 카카오, 일반, 구글..)
//    @NotBlank
//    private String address; // 주소
    @NotBlank
    private String name; // 이름
    @NotBlank
    private String gender; // 성별
    @NotBlank
    @Pattern(regexp = "^[0-9]{6,8}$", message = "생년월일과 뒷자리 주민번호 첫째 자리수를 명확히 입력해주십시오.")
    @Size(min = 8, max = 8, message = "생년월일은 8자여야 합니다.")
    private String birthDate; // 생년월일
    //    @NotBlank
//    @Size(min = 5, max = 6, message = "우편번호는 5자 혹은 6자여야 합니다.")
//    private String postalCode; // 우편 번호
//    @NotBlank
//    @Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 기호없이 11자여야 합니다.")
//    @Size(min = 10, max = 14, message = "전화번호는 최소 11자여야 합니다.")
//    private String phone; // 전화 번호
//    private String introduceSelf; // 간단 자기 소개
    private Long leagueId; // 선호 리그 id
    private Long teamId; // 선호 팀 id
    @NotBlank
    private String personalInfoAgreement; // 개인정보 이용 동의
    @NotBlank
    private String marketingAgreement; // 마케팅 동의
}
