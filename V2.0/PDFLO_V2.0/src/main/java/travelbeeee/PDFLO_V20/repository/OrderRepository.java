package travelbeeee.PDFLO_V20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.member = :member")
    List<Order> findAllByMember(@Param("member") Member member);

    @Query("select o from Order o join fetch o.orderItems")
    List<Order> findWithOrderItemById(@Param("orderId") Long orderId);
}
