package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.member.id = :memberId and c.item.id = :itemId")
    Optional<Comment> findByMemberIdAndItemId(@Param("memberId")Long memberId, @Param("itemId")Long itemId);

    @Query("select c from Comment c where c.item.id = :itemId")
    List<Comment> findAllByItem(@Param("itemId") Long itemId);
}
