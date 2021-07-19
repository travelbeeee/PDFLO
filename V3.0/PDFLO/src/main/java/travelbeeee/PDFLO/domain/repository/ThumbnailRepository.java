package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO.domain.model.entity.Thumbnail;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
