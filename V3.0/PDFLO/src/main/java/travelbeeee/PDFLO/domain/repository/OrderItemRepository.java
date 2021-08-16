package travelbeeee.PDFLO.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("select oi from OrderItem oi where oi.order.member = :member and oi.item = :item")
    Optional<OrderItem> findByMemberAndItem(@Param("member") Member member, @Param("item") Item item);

    @Query("select oi from OrderItem oi where oi.order.member = :member and oi.item in :items")
    Optional<OrderItem> findByMemberAndItems(@Param("member") Member member, @Param("items") List<Item> items);

    @Query("select oi from OrderItem oi where oi.item.id in :itemIds")
    List<OrderItem> findAllByItems(@Param("itemIds") List<Long> itemIds);

    @Query("select oi from OrderItem oi join fetch oi.item i join fetch i.thumbnail where oi.order.id = :orderId")
    List<OrderItem> findAllWithItemWithThumbnailByOrder(@Param("orderId") Long orderId);

    @Query(value = "select oi from OrderItem oi join fetch oi.order o join fetch o.member where oi.item.id = :itemId",
    countQuery = "select count(oi) from OrderItem oi where oi.item.id = :itemId")
    Page<OrderItem> findPagingWithOrderMemberByItem(@Param("itemId") Long itemId, Pageable pageable);
}
