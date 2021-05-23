package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Comment;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.member.id = :memberId")
    List<Item> findByMember(@Param("memberId") Long memberId);

    @Query("select c from Comment c where c.item_id = :itemId")
    List<Comment> findComment(@Param("itemId") Long itemId);
}
