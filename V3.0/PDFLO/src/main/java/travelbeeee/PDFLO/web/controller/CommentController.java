package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.dto.CommentItemDto;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.service.CommentService;
import travelbeeee.PDFLO.web.form.CommentForm;
import travelbeeee.PDFLO.web.validator.CommentFormValidator;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final CommentFormValidator commentFormValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(commentFormValidator);
    }

    /**
     * 후기 남기기
     */
    @ResponseBody
    @PostMapping("/comment/write/{itemId}")
    public ReturnCode uploadComment(HttpSession httpSession, @PathVariable("itemId") Long itemId,
                                    @Validated @ModelAttribute CommentForm commentForm, BindingResult bindingResult) throws PDFLOException {
        log.info("UploadComment 메소드");
        if (bindingResult.hasErrors()) {
            return ReturnCode.COMMENT_INPUT_INVALID;
        }
        Long memberId = (Long) httpSession.getAttribute("id");
        ReturnCode resultReturnCode = commentService.uploadComment(memberId, itemId, commentForm);

        return resultReturnCode;
    }

    @PostMapping("/comment/delete/{itemId}/{commentId}")
    @ResponseBody
    public ReturnCode deleteComment(HttpSession httpSession, @PathVariable("commentId") Long commentId,
                                @PathVariable("itemId") Long itemId) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        return commentService.deleteComment(memberId, commentId);
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
