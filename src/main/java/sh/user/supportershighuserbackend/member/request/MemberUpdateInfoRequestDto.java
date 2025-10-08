package sh.user.supportershighuserbackend.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sh.user.supportershighuserbackend.common.base.AbstractVO;

@Setter
@Getter
public class MemberUpdateInfoRequestDto extends AbstractVO {
    @NotBlank
    @Size(min = 2, message = "닉네임은 최소 2자 이상이여야 합니다.")
    private String name; // 닉네임
    private String passwordChangeCheck; // 비밀번호 변경 유무
    private String password; // 기존 비밀번호
    private String newPassword; // 새로운 비밀번호
//    private String address; // 주소
    @NotBlank
    @Pattern(regexp = "^[0-9]{6,8}$", message = "생년월일과 뒷자리 주민번호 첫째 자리수를 명확히 입력해주십시오.")
    @Size(min = 8, max = 8, message = "생년월일은 8자여야 합니다.")
    private String birthDate; // 생년월일
//    @NotBlank
//    @Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 기호없이 11자여야 합니다.")
//    @Size(min = 10, max = 14, message = "전화번호는 최소 11자여야 합니다.")
//    private String phone; // 전화 번호
    private Long leagueId; // 선호 리그 id
    private String leagueName; // 선호 리그 명
    private Long teamId; // 선호 팀 id
    private String teamName; // 선호 팀 명
    private Long leagueId2;
    private String league2Name;
    private Long teamId2;
    private String team2Name;
//    private String personalInfoAgreement; // 개인정보 이용 동의
    private String marketingAgreement; // 마케팅 동의
}
