package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Comment;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;

import javax.mail.FetchProfile;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i join fetch i.thumbnail where i.member.id = :memberId")
    List<Item> findWithThumbnailByMember(@Param("memberId") Long memberId);

    @Query("select distinct(i) from Item i join fetch i.member where i.id in :itemIds")
    List<Item> findSelectedItemWithMember(@Param("itemIds") List<Long> itemIds);

    @Query("select distinct(i) from Item i join fetch i.member join fetch i.thumbnail where i.type = 'SELL'")
    List<Item> findSellItemWithMemberAndThumbnail();

    @Query("select distinct(i) from Item i join fetch i.member join fetch i.thumbnail join fetch i.pdf where i.id = :itemId")
    Optional<Item> findWithMemberAndPdfAndThumbnailById(@Param("itemId") Long itemId);

    @Query("select i from Item i where i.member.id = :memberId")
    List<Item> findByMember(@Param("memberId") Long memberId);
}
