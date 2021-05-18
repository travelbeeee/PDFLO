package travelbeeee.PDFLOpjt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLOpjt.domain.Comment;
import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.domain.Order;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.inputform.CommentForm;
import travelbeeee.PDFLOpjt.service.CommentService;
import travelbeeee.PDFLOpjt.service.ContentService;
import travelbeeee.PDFLOpjt.service.OrderService;
import travelbeeee.PDFLOpjt.utility.PermissionChecker;
import travelbeeee.PDFLOpjt.utility.ReturnCode;

import javax.servlet.http.HttpSession;

@Controller @RequiredArgsConstructor
public class CommentController {

    private final ContentService contentService;
    private final OrderService orderService;
    private final CommentService commentService;

    /**
     * contentId에 userId가 댓글 남기기
     * 1) 댓글 Input이 제대로 들어왔는지 ReturnCode.COMMENT_INPUT_INVALID
     * 2) content 구매자인지 ReturnCode.COMMENT_NO_PERMISSSION_BUYING
     * 3) 기존에 댓글을 쓴적이없는지
     */
    @PostMapping("/comment/write/{contentId}")
    public String commentWriting(@PathVariable("contentId") int contentId, HttpSession httpSession, CommentForm commentForm,
                                 BindingResult bindingResult) throws PDFLOException {

        if(bindingResult.hasErrors()) throw new PDFLOException(ReturnCode.COMMENT_INPUT_INVALID);
        PermissionChecker.checkPermission(httpSession);
        System.out.println("Score = " + commentForm.getScore());

        int userId = (int) httpSession.getAttribute("userId");
        commentService.writing(userId, contentId, commentForm);

        return "redirect:/content/" + contentId;
    }

    @GetMapping("/comment/modify/{commentId}")
    public String commentModifyForm(@PathVariable("commentId") int commentId, HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Comment comment = commentService.selectByComment(commentId);
        if(comment.getUserId() != (int)httpSession.getAttribute("userId"))
            throw new PDFLOException(ReturnCode.COMMENT_NO_PERMISSION_WRITER);
        model.addAttribute("comment", comment);

        return "/comment/modifyForm";
    }

    @PostMapping("/comment/modify/{commentId}")
    public String commentModify(@PathVariable("commentId") int commentId, HttpSession httpSession,
                                CommentForm commentForm, BindingResult bindingResult) throws PDFLOException {
        // 로그인했는지 체크
        PermissionChecker.checkPermission(httpSession);
        // 잘못된 입력
        if(bindingResult.hasErrors()) throw new PDFLOException(ReturnCode.COMMENT_INPUT_INVALID);

        int userId = (int)httpSession.getAttribute("userId");
        commentService.update(userId, commentId, commentForm);
        Comment comment = commentService.selectByComment(commentId);
        return "redirect:/content/" + comment.getContentId();
    }

    @PostMapping("/comment/delete/{commentId}")
    public String commentDelete(@PathVariable("commentId") int commentId, HttpSession httpSession) throws PDFLOException {
        // 로그인했는지 체크
        PermissionChecker.checkPermission(httpSession);

        int userId = (int)httpSession.getAttribute("userId");
        Comment comment = commentService.selectByComment(commentId);
        commentService.delete(userId, commentId);
        return "redirect:/content/" + comment.getContentId();
    }
}
