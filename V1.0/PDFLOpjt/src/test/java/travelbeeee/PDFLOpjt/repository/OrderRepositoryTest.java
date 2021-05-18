package travelbeeee.PDFLOpjt.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.Order;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @AfterEach
    void afterEach(){ orderRepository.deleteAll();}

    Order getOrder(){
        Order order = new Order();
        order.setContentId(1);
        order.setUserId(1);
        order.setLocaldate(LocalDate.now());
        return order;
    }

    @Test
    public void 주문insert테스트() throws Exception{
        //given
        Order order = getOrder();
        //when
        int res = orderRepository.insert(order);
        //then
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void 주문selectByUser테스트() throws Exception{
        //given
        Order order = getOrder();
        Order order1 = getOrder();
        Order order2 = getOrder();
        order1.setContentId(2);
        order2.setContentId(3);
        //when
        orderRepository.insert(order);
        orderRepository.insert(order1);
        orderRepository.insert(order2);

        List<Order> orders = orderRepository.selectByUser(1);
        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    public void 주문selectByContent테스트() throws Exception{
        //given
        Order order = getOrder();
        Order order1 = getOrder();
        Order order2 = getOrder();
        order1.setUserId(2);
        order2.setUserId(3);
        //when
        orderRepository.insert(order);
        orderRepository.insert(order1);
        orderRepository.insert(order2);

        List<Order> orders = orderRepository.selectByContent(1);
        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    public void 주문delete테스트() throws Exception{
        //given
        Order order = getOrder();
        //when
        orderRepository.insert(order);
        int res = orderRepository.delete(order.getOrderId());
        List<Order> orders = orderRepository.selectByUser(order.getUserId());
        //then
        assertThat(res).isEqualTo(1);
        assertThat(orders.isEmpty()).isTrue();
    }

    @Test
    public void 판매테스트() throws Exception{
        List<Order> orders = orderRepository.selectSelling(122);
        for(Order order : orders)
            System.out.println("Order : " + order);
    }
}