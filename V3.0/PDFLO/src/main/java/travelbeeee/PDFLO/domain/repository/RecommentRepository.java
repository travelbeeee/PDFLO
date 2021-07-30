package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO.domain.model.entity.Recomment;

public interface RecommentRepository extends JpaRepository<Recomment, Long> {
}
