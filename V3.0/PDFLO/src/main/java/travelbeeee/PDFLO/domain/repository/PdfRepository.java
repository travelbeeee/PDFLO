package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO.domain.model.entity.Pdf;

public interface PdfRepository extends JpaRepository<Pdf, Long> {
}
