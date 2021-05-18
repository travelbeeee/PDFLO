package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.domain.Order;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.repository.OrderRepository;
import travelbeeee.PDFLOpjt.service.ContentService;
import travelbeeee.PDFLOpjt.service.OrderService;
import travelbeeee.PDFLOpjt.service.UserAccountService;

import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ContentService contentService;
    private final UserAccountService userAccountService;

    /**
     *
     * @param userId 구매자
     * @param contentId 구매를원하는상품
     * 1) 구매자랑 상품 판매자랑 다른지 확인! 같다면 구매 불가능
     * 2) 구매자가 이미 구매한 상품인지 확인! 이미 구매했다면 구매 불가능
     * 3) 구매자가 상품 구매가 가능한 잔고인지 확인!
     */
    @Override
    public ReturnCode order(int userId, int contentId) throws PDFLOException {
        Content content = contentService.selectById(contentId);
        if(userId == content.getUserId()) throw new PDFLOException(ReturnCode.SELLER_CANT_BUY);

        Order findOrder = orderRepository.selectByContentUser(contentId, userId);
        if(findOrder != null)  throw new PDFLOException(ReturnCode.CONTENT_ALREADY_BOUGHT); // 이미 구매한 적 있음.

        userAccountService.withdraw(userId, content.getPrice()); // 구매자 잔고 -
        userAccountService.deposit(content.getUserId(), content.getPrice()); // 판매자 잔고 +

        Order order = new Order();
        order.setUserId(userId);
        order.setContentId(contentId);
        order.setLocaldate(LocalDate.now());
        orderRepository.insert(order);
        
        return ReturnCode.SUCCESS;
    }

    @Override
    public List<Order> selectOrderByUser(int userId) {
        List<Order> orders = orderRepository.selectByUser(userId);

        return orders;
    }

    @Override
    public List<Order> selectSelling(int userId) {
        List<Order> orders = orderRepository.selectSelling(userId);

        return orders;
    }

    @Override
    public Order selectByContentUser(int contentId, int userId) {
        return orderRepository.selectByContentUser(contentId, userId);
    }


}
