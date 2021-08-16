package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO.domain.model.entity.PointHistory;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    @Query(value = "select ph from PointHistory ph where ph.member.id = :memberId",
        countQuery = "select ph from PointHistory ph")
    Page<PointHistory> findMemberPointHistory(@Param("memberId") Long memberId, Pageable pageable);
}
