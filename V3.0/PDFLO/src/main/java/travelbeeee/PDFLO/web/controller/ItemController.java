package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.dto.ItemDetailDto;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.service.ItemService;
import travelbeeee.PDFLO.web.form.CommentForm;
import travelbeeee.PDFLO.web.form.ItemForm;
import travelbeeee.PDFLO.web.form.RecommentForm;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/item/upload")
    public String itemUploadForm(HttpSession httpSession, Model model) throws PDFLOException {
        model.addAttribute("itemForm", new ItemForm());
        return "/item/uploadForm";
    }

    @PostMapping("/item/upload")
    public String itemUpload(HttpSession httpSession, @Valid ItemForm itemForm, BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException, IOException {
        if (bindingResult.hasErrors()) {
            if (itemForm.getThumbnailFile().isEmpty()) {
                bindingResult.addError(new FieldError("itemForm", "thumbnailFile", "썸네일은 필수입니다."));
            }
            if (itemForm.getPdfFile().isEmpty()) {
                bindingResult.addError(new FieldError("itemForm", "pdfFile", "PDF 파일은 필수입니다."));
            }
            return "/item/uploadForm";
        }
        Long memberId = (Long) httpSession.getAttribute("id");
        itemService.uploadItem(memberId, itemForm);

        return "redirect:/";
    }

    @GetMapping("/item/{itemId}")
    public String itemDetail(HttpSession httpSession, @PathVariable("itemId") Long itemId, Model model) throws PDFLOException {
        Item item = itemService.findWithMemberAndPdfAndThumbnailAndCommentAndRecommentById(itemId);
        Long memberId = (Long) httpSession.getAttribute("id");
        if (item.getMember().getId() == memberId) {
            model.addAttribute("SELLER", true);
        }else{
            model.addAttribute("CLIENT", true);
            if (itemService.checkBuyer(memberId, itemId)) {
                model.addAttribute("BUYER", true);
            }
        }

        ItemDetailDto itemDetailDto = new ItemDetailDto(item);
        model.addAttribute("item", itemDetailDto);
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("recommentForm", new RecommentForm());
        return "/item/detail";
    }

    @PostMapping("/item/delete/{itemId}")
    public String itemDelete(HttpSession httpSession, @PathVariable("itemId") Long itemId) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        itemService.deleteItem(memberId, itemId);

        return "redirect:/";
    }

    @GetMapping("/item/modify/{itemId}")
    public String itemModifyForm(HttpSession httpSession, @PathVariable("itemId") Long itemId,
                                 Model model) throws PDFLOException {
        model.addAttribute("itemForm", new ItemForm());
        model.addAttribute("itemId", itemId);

        return "/item/modifyForm";
    }

    @PostMapping("/item/modify/{itemId}")
    public String itemModify(HttpSession httpSession,  @Valid ItemForm itemForm, Model model,
                             @PathVariable("itemId") Long itemId, BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException, IOException {
        if (bindingResult.hasErrors() || itemForm.getThumbnailFile().isEmpty() || itemForm.getPdfFile().isEmpty()) {
            model.addAttribute("itemid", itemId);
            return "redirect:/item/modifyForm";
        }

        Long memberId = (Long) httpSession.getAttribute("id");
        itemService.modifyItem(memberId, itemId, itemForm);

        return "redirect:/";
    }

    @PostMapping("/item/reSell/{itemId}")
    public String itemReSell(HttpSession httpSession, @PathVariable("itemId") Long itemId) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        itemService.reSell(memberId, itemId);
        return "redirect:/member/myItem";
    }

    @PostMapping("/item/download/{itemId}")
    public void itemDownload(HttpSession httpSession, @PathVariable("itemId") Long itemId, HttpServletResponse response) throws PDFLOException, IOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        byte[] pdfFile = itemService.downloadItem(memberId, itemId);
        response.addHeader("Content-Disposition", "attachment; fileName=content.pdf");
        response.getOutputStream().write(pdfFile);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
