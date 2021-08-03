package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;

import java.util.List;

public interface PopularItemRepository extends JpaRepository<PopularItem, Long> {
    @Query(value ="select pi from PopularItem pi join fetch pi.item i join fetch i.thumbnail " +
            "where i.type = travelbeeee.PDFLO.domain.model.enumType.ItemType.SELL",
        countQuery = "select count(i) from Item i where i.type = travelbeeee.PDFLO.domain.model.enumType.ItemType.SELL")
    Page<PopularItem> findSellPopularItemWithItemAndThumbnailPaging(Pageable pageable);

    @Query(value ="select pi from PopularItem pi join fetch pi.item i join fetch i.thumbnail where i.member.id = :memberId",
            countQuery = "select count(pi) from PopularItem pi where pi.item.member.id = :memberId")
    Page<PopularItem> findPopularItemWithItemAndThumbnailPagingByMember(Pageable pageable, Long memberId);

    @Modifying
    @Query("update PopularItem pi SET pi.score = :totalScore, pi.commentAvg = :commentAvg, pi.commentCnt = :commentCnt, pi.orderCnt = :orderCnt where pi.item.id = :itemId")
    void updatePopular(Long itemId, Double totalScore, Double commentAvg, Integer commentCnt, Integer orderCnt);
}