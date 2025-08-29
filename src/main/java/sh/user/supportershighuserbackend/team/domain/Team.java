package sh.user.supportershighuserbackend.team.domain;

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
@org.springframework.data.relational.core.mapping.Table
@Comment("팀")
@Entity
@Table(
        indexes = {
                @Index(name = "idx_team_name", columnList = "name"),
        }
)
public class Team{

    @Comment("팀 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long teamId;

    @Comment("팀 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20) not null")
    private String name;

    @Comment("연관 리그 ID")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "bigint not null")
    private Long leagueId;
}
