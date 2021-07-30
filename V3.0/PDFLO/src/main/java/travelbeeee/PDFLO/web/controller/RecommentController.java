package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.service.RecommentService;
import travelbeeee.PDFLO.web.form.RecommentForm;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RecommentController {

    private final RecommentService recommentService;

    @PostMapping("/recomment/{commentId}")
    @ResponseBody
    public ReturnCode uploadRecomment(@PathVariable("commentId") Long commentId, HttpSession httpSession,
                                      @Validated @ModelAttribute RecommentForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ReturnCode.RECOMMENT_INPUT_INVALID;
        }
        Long memberId = (Long) httpSession.getAttribute("id");
        return recommentService.uploadRecomment(memberId, commentId, form.getComment());
    }

    @DeleteMapping("/recomment/{recommentId}")
    @ResponseBody
    public ReturnCode deleteRecomment(@PathVariable("recommentId") Long recommentId, HttpSession httpSession) {
        Long memberId = (Long) httpSession.getAttribute("id");
        return recommentService.deleteRecomment(memberId, recommentId);
    }
}
