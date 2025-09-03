package sh.user.supportershighuserbackend.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.Comment;
import sh.user.supportershighuserbackend.member.request.MemberUpdateInfoRequestDto;
import sh.user.supportershighuserbackend.share.TimeStamped;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@org.springframework.data.relational.core.mapping.Table
@Comment("회원")
@Entity
@Table(
        indexes = {
                @Index(name = "idx_member_email", columnList = "email"),
//                @Index(name = "idx_member_address", columnList = "address"),
                @Index(name = "idx_member_account_type", columnList = "accountType"),
        }
)
public class Member extends TimeStamped {

    @Comment("회원 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @Comment("회원 계정 이메일")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(50) not null")
    private String email;

    @Comment("회원 계정 닉네임")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20)")
    private String nickName;

    @Comment("회원 이름")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(10) not null")
    private String name;

    @Comment("회원 계정 비밀번호")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(70) not null")
    private String password;

    @Comment("회원 계정 유형")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(10) not null")
    private String accountType;

    @Comment("회원 주소")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(150) default null")
    private String address;

    @Comment("회원 생일번호 (예 : 19990101)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(8) not null")
    private String birthDate;

    @Comment("우편번호")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(6) default null")
    private String postalCode;

    @Comment("전화번호 (예 : 01033334444)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(15) default null")
    private String phone;

    @Comment("계정 유지 여부")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) default 'y'")
    private String live;

    @Comment("계정 프로필 이미지 호출 경로")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(100) default null")
    private String profileImage;

//    @Comment("간단 계정 자기소개")
//    @Collate("utf8mb4_general_ci")
//    @Column(columnDefinition = "text")
//    private String introduceSelf;

    @Comment("성별 (M:남자, F:여자)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) not null")
    private String gender;

    @Comment("선호 리그 ID")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "bigint not null")
    private Long leagueId;

    @Comment("선호 리그 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(15) not null")
    private String leagueName;

    @Comment("선호 팀 ID")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "bigint not null")
    private Long teamId;

    @Comment("선호 팀 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20) not null")
    private String teamName;

    @Comment("개인정보 수집 동의")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) not null")
    private String personalInfoAgreement;

    @Comment("마케팅 동의")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) not null")
    private String marketingAgreement;

    // 회원 정보 수정
    public void changeMemberInfo(MemberUpdateInfoRequestDto updateInfo, String password) {
        this.nickName = updateInfo.getNickName();
        this.password = password;
        this.address = updateInfo.getAddress();
        this.birthDate = updateInfo.getBirthDate();
        this.postalCode = updateInfo.getPostalCode();
        this.phone = updateInfo.getPhone();
        this.leagueId = updateInfo.getLeagueId();
        this.leagueName = updateInfo.getLeagueName();
        this.teamId = updateInfo.getTeamId();
        this.teamName = updateInfo.getTeamName();
        this.personalInfoAgreement = updateInfo.getPersonalInfoAgreement();
        this.marketingAgreement = updateInfo.getMarketingAgreement();
    }
}
