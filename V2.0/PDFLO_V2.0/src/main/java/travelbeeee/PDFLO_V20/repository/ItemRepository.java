package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO_V20.domain.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
