package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.Order;

import java.util.List;

@Mapper
public interface OrderRepository { // 주문을 수정되면 안된다!
    int insert(Order order);
    List<Order> selectByContent(int contentId); // 판매자가 판매내역 가져올 때
    List<Order> selectByUser(int userId); // 유저가 자기 구매 내역 가져올 때
    List<Order> selectSelling(int userId); // 유저가 자기 판매 내역 가져올 때
    Order selectByContentUser(int contentId, int userId); // 유저 contentId 를 구매했는지
    int delete(int orderId);
    int deleteAll();
}
