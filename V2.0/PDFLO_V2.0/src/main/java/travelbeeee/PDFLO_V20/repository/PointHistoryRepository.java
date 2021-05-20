package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import travelbeeee.PDFLO_V20.domain.entity.PointHistory;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    @Query("select p from PointHistory p where p.member.id = :memberId")
    List<PointHistory> findMemberPointHistory(@Param("memberId") Long memberId);
}
