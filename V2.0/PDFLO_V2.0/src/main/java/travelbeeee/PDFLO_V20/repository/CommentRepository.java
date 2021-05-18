package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
