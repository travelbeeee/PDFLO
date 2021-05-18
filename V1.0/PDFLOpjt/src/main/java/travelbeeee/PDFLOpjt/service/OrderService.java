package travelbeeee.PDFLOpjt.service;

import travelbeeee.PDFLOpjt.domain.Order;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.utility.ReturnCode;

import java.util.List;

public interface OrderService {
    ReturnCode order(int userId, int contentId) throws PDFLOException; // user가 content를 구매한다.
    List<Order> selectOrderByUser(int userId);
    List<Order> selectSelling(int userId);
    Order selectByContentUser(int contentId, int userId);
}