package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;

public interface PopularItemRepository extends JpaRepository<PopularItem, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update PopularItem pi SET pi.score = :totalScore, pi.commentAvg = :commentAvg, pi.commentCnt = :commentCnt, pi.orderCnt = :orderCnt where pi.item.id = :itemId")
    void updatePopular(Long itemId, Double totalScore, Double commentAvg, Integer commentCnt, Integer orderCnt);
}