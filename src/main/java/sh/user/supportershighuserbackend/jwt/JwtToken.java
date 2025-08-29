package sh.user.supportershighuserbackend.jwt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.Comment;
import sh.user.supportershighuserbackend.share.TimeStamped;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Comment("JWT")
@Entity
@Table(indexes = {
        @Index(name = "idx_token_member_id", columnList = "memberId")
})
public class JwtToken extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long tokenId;

    @Comment("토큰 권한 유형")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(6) not null")
    private String grantType;

    @Comment("리프레시 토큰")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(90) not null")
    private String refreshToken;

    @Comment("토큰 발급 계정 ID")
    @Column(columnDefinition = "bigint not null")
    private Long memberId;
}
