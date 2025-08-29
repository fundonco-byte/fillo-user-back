package sh.user.supportershighuserbackend.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.user.supportershighuserbackend.media.domain.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
