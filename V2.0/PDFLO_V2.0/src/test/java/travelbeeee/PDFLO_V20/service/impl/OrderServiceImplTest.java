package travelbeeee.PDFLO_V20.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.repository.*;
import travelbeeee.PDFLO_V20.service.OrderService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class OrderServiceImplTest {

    @Autowired
    OrderService orderService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 오더_정상() throws Exception{
        // given
        Member member1 = new Member("member1", null, null, null, null, 10000);
        Member member2 = new Member("member2", null, null, null, null, 10000);

        Item item1 = new Item(member2, "item1", null, 1000, null, null);
        Item item2 = new Item(member2, "item2", null, 2000, null, null);
        Item item3 = new Item(member2, "item3", null, 3000, null, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        // when
        List<Long> items = new ArrayList<>();
        items.add(item1.getId());
        items.add(item2.getId());
        items.add(item3.getId());

        orderService.putOrder(member1.getId(), items);

        em.flush();
        em.clear();

        // then
        // 회원 포인트가 차감되야됨
        Member member = memberRepository.findById(member1.getId()).get();
        Assertions.assertThat(member.getPoint()).isEqualTo(4000);
        // 포인트 내역이 3개 생겨야됨
        List<PointHistory> pointHistories = pointHistoryRepository.findMemberPointHistory(member1.getId());
        Assertions.assertThat(pointHistories.size()).isEqualTo(3);
        Assertions.assertThat(pointHistories.get(0).getType()).isEqualTo(PointType.USE);
        // 주문이 생겨야됨
        List<Order> orders = orderRepository.findAllByMember(member1.getId());
        Assertions.assertThat(orders.size()).isEqualTo(1);
        Assertions.assertThat(orders.get(0).getMember().getId()).isEqualTo(member1.getId());
        // OrderItem 3개
        List<OrderItem> orderItems = orderItemRepository.findAll();
        Assertions.assertThat(orderItems.size()).isEqualTo(3);
    }

    @Test
    public void 회원_주문내역조회() throws Exception{
        // given
        Member member1 = new Member("member1", null, null, null, null, 10000);
        Member member2 = new Member("member2", null, null, null, null, 10000);

        Item item1 = new Item(member2, "item1", null, 1000, null, null);
        Item item2 = new Item(member2, "item2", null, 2000, null, null);
        Item item3 = new Item(member2, "item3", null, 3000, null, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        List<Long> items1 = new ArrayList<>();
        items1.add(item1.getId());
        items1.add(item2.getId());

        List<Long> items2 = new ArrayList<>();
        items2.add(item3.getId());

        orderService.putOrder(member1.getId(), items1);
        orderService.putOrder(member1.getId(), items2);

        em.flush();
        em.clear();

        // then
        List<Order> orders = orderService.findOrderByMember(member1.getId());

        Assertions.assertThat(orders.size()).isEqualTo(2);
        Assertions.assertThat(orders.get(0).getMember().getUsername()).isEqualTo("member1");
        Assertions.assertThat(orders.get(1).getMember().getUsername()).isEqualTo("member1");
        Assertions.assertThat(orders.get(0).getOrderItems().size()).isEqualTo(2);
        Assertions.assertThat(orders.get(1).getOrderItems().size()).isEqualTo(1);
    }

}