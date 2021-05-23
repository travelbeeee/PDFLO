package travelbeeee.PDFLO_V20.repository;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.memberId = :memberId and c.itemId = :itemId")
    Optional<Comment> findByMemberIdAndItemId(@Param("memberId")Long memberId, @Param("itemId")Long itemId);
}
