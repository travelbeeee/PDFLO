package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO.domain.model.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.member.id = :memberId")
    List<Order> findAllByMember(@Param("memberId") Long memberId);

    @Query("select distinct(o) from Order o join fetch o.orderItems oi join fetch oi.item where o.member.id = :memberId")
    List<Order> findAllByMemberWithItem(@Param("memberId") Long memberId);

    @Query("select distinct(o) from Order o join fetch o.orderItems where o.id = :orderId")
    List<Order> findWithOrderItemById(@Param("orderId") Long orderId);

    @Query("select distinct(o) from Order o join fetch o.orderItems where o.member.id = :memberId")
    List<Order> findAllWithOrderItemByMember(@Param("memberId") Long memberId);
}
