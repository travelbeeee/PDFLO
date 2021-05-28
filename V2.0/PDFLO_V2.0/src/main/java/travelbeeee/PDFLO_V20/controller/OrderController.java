package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.OrderService;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public String order(HttpSession httpSession, List<Long> itemIds) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long)httpSession.getAttribute("id");

        orderService.putOrder(memberId, itemIds);
        return "redirect:/";
    }
}