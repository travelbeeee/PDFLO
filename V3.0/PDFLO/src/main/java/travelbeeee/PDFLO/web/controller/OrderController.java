package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.service.OrderService;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public String order(HttpSession httpSession, @RequestParam("orderList") List<String> orderList) throws PDFLOException {
        Long memberId = (Long)httpSession.getAttribute("id");
        List<Long> itemIds = orderList.stream().map(s -> Long.parseLong(s))
                .collect(Collectors.toList());

        orderService.putOrder(memberId, itemIds);
        return "redirect:/";
    }
}