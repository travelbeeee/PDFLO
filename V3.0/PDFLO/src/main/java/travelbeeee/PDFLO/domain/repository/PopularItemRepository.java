package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;

public interface PopularItemRepository extends JpaRepository<PopularItem, Long> {
}
