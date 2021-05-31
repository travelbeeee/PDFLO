package travelbeeee.PDFLO_V20.repository;

import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select distinct(c) from Cart c join fetch c.item join fetch c.member where c.member.id = :memberId")
    List<Cart> findAllByMemberWithItemMember(@Param("memberId") Long memberId);
}
