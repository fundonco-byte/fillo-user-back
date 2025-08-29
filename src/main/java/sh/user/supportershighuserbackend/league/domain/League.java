package sh.user.supportershighuserbackend.league.domain;

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
@Comment("리그")
@Entity
@Table(
        indexes = {
                @Index(name = "idx_league_name", columnList = "name"),
        }
)
public class League{

    @Comment("리그 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long leagueId;

    @Comment("리그 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20) not null")
    private String name;
}
