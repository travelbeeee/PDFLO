package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO_V20.domain.form.CommentForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.CommentService;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/write/{itemId}")
    public String uploadComment(HttpSession httpSession, @PathVariable("itemId") Long itemId,
                                @Valid CommentForm commentForm, BindingResult bindingResult) throws PDFLOException {
        if (bindingResult.hasErrors()) {
            return "redirect:/item/detail/" + itemId;
        }

        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long) httpSession.getAttribute("id");

        commentService.uploadComment(memberId, itemId, commentForm);

        return "redirect:/item/" + itemId;
    }

    @PostMapping("/comment/delete/{itemId}/{commentId}")
    public String deleteComment(HttpSession httpSession, @PathVariable("commentId") Long commentId,
                                @PathVariable("itemId") Long itemId) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        commentService.deleteComment(memberId, commentId);

        return "redirect:/item/" + itemId;
    }
}
