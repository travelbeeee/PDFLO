package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO_V20.domain.dto.ItemDetailDto;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.form.ItemForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.ItemService;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/item/upload")
    public String itemUploadForm(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        model.addAttribute("itemForm", new ItemForm());
        return "/item/uploadForm";
    }

    @PostMapping("/item/upload")
    public String itemUpload(HttpSession httpSession, @Valid ItemForm itemForm, BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException, IOException {
        if (bindingResult.hasErrors() || itemForm.getThumbnailFile().isEmpty() || itemForm.getPdfFile().isEmpty()) {
            return "/item/uploadForm";
        }
        Long memberId = (Long) httpSession.getAttribute("id");
        itemService.uploadItem(memberId, itemForm);

        return "redirect:/";
    }

    @GetMapping("/item/{itemId}")
    public String itemDetail(HttpSession httpSession, @PathVariable("itemId") Long itemId, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Item item = itemService.findWithMemberAndPdfAndThumbnailAndCommentById(itemId);
        ItemDetailDto itemDetailDto = new ItemDetailDto(item);
        model.addAttribute("item", itemDetailDto);
        return "/item/detail";
    }

}
