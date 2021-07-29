package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO.domain.model.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.member.id = :memberId")
    List<Order> findAllByMember(@Param("memberId") Long memberId);

    @Query("select distinct(o) from Order o join fetch o.orderItems oi join fetch oi.item where o.member.id = :memberId")
    List<Order> findAllByMemberWithItem(@Param("memberId") Long memberId);

    @Query("select distinct(o) from Order o join fetch o.orderItems where o.member.id = :memberId")
    List<Order> findAllWithOrderItemByMember(@Param("memberId") Long memberId);

    @Query("select o from Order o join fetch o.member m join fetch o.orderItems oi join fetch oi.item i where m.id = :memberId and i.id = :itemId")
    Optional<Order> findOrderWithMemberOrderItemAndItem(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
}
