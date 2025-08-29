package sh.user.supportershighuserbackend.media.domain;

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
@Comment("미디어")
@Table(
        indexes = {
                @Index(name = "idx_media_content_type", columnList = "content_type"),
                @Index(name = "idx_media_mapping_content_id", columnList = "mapping_content_id")
        }
)
@Entity
public class Media extends TimeStamped {

    @Comment("미디어 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long mediaId;

    @Comment("미디어 파일 원본 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(50) not null")
    private String mediaTitle;

    @Comment("미디어 파일 난수화 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(80) not null")
    private String mediaUuidTitle;

    @Comment("미디어 파일 업로드 경로")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(150) not null")
    private String mediaUploadUrl;

    @Comment("미디어 파일 호출 경로")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(100) not null")
    private String mediaUrl;

    @Comment("미디어 파일 관련 컨텐츠 타입 (예 : 배너, 제품)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20) not null")
    private String contentType;

    @Comment("미디어 파일 타입 (예 : 비디오, 이미지)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(10) not null")
    private String type;

    @Comment("대표 이미지 확인 여부")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1)")
    private String representCheck;

    @Comment("미디어 파일 관련 컨텐츠 ID (예 : 제품 ID)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "bigint not null")
    private Long mappingContentId;
}
