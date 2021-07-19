package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO.domain.model.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select distinct(c) from Cart c join fetch c.item join fetch c.member where c.member.id = :memberId")
    List<Cart> findAllByMemberWithItemMember(@Param("memberId") Long memberId);

    @Query("select c from Cart c where c.member.id = :memberId and c.item.id = :itemId")
    Optional<Cart> findByMemberAndItem(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
}
