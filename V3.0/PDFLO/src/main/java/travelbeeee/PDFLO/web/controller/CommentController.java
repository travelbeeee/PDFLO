package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.dto.CommentItemDto;
import travelbeeee.PDFLO.domain.model.dto.CommentModifyDto;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.service.CommentService;
import travelbeeee.PDFLO.web.form.CommentForm;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/comment/modify/{itemId}/{commentId}")
    public String modifyCommentForm(HttpSession httpSession, @PathVariable("commentId") Long commentId,
                                    @PathVariable("itemId") Long itemId,  Model model) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        Comment findComment = commentService.findByIdAndMember(commentId, memberId);

        CommentModifyDto comment = new CommentModifyDto(findComment);

        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("comment", comment);
        model.addAttribute("itemId", itemId);

        return "/comment/modifyForm";
    }

    @PostMapping("/comment/modify/{itemId}/{commentId}")
    public String modifyComment(HttpSession httpSession, @Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId) throws PDFLOException {
        if (bindingResult.hasErrors()) {
            return "redirect:/comment/modify/" + itemId + "/" + commentId;
        }

        Long memberId = (Long) httpSession.getAttribute("id");
        commentService.modifyComment(memberId, commentId, commentForm);

        return "redirect:/item/" + itemId;
    }

    @GetMapping("/comment/member")
    public String myComment(HttpSession httpSession, Model model) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        List<Comment> comments = commentService.findAllWithItemByMember(memberId);
        List<CommentItemDto> commentList = comments.stream()
                .map(c -> new CommentItemDto(c))
                .collect(Collectors.toList());

        model.addAttribute("commentList", commentList);

        return "/member/mycomment";
    }
}
