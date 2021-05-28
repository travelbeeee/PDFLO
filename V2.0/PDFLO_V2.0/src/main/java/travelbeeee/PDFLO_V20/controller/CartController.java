package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.CartService;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @ResponseBody
    @PostMapping("/cart/{itemId}")
    public String putItemOnCart(HttpSession httpSession, @PathVariable("itemId") Long itemId) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long) httpSession.getAttribute("id");
        cartService.putItemOnCart(memberId, itemId);
        return "성공!!";
    }
}
