package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;

import java.util.List;

public interface PopularItemRepository extends JpaRepository<PopularItem, Long> {
    @Query("select pi from PopularItem pi join fetch pi.item i join fetch i.thumbnail")
    List<PopularItem> findPopularItemWithItemAndThumbnail();

    @Query("select pi from PopularItem pi join fetch pi.item i join fetch i.thumbnail order by pi.score desc")
    List<PopularItem> findPopularItemWithItemAndThumbnailOrderByScore();

    @Query("select pi from PopularItem pi join fetch pi.item i join fetch i.thumbnail order by i.createdDate desc")
    List<PopularItem> findPopularItemWithItemAndThumbnailOrderByDate();

    @Modifying(clearAutomatically = true)
    @Query("update PopularItem pi SET pi.score = :totalScore, pi.commentAvg = :commentAvg, pi.commentCnt = :commentCnt, pi.orderCnt = :orderCnt where pi.item.id = :itemId")
    void updatePopular(Long itemId, Double totalScore, Double commentAvg, Integer commentCnt, Integer orderCnt);
}