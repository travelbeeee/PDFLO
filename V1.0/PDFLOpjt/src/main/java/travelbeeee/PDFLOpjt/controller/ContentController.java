package travelbeeee.PDFLOpjt.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLOpjt.domain.Comment;
import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.domain.Thumbnail;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.inputform.ContentForm;
import travelbeeee.PDFLOpjt.service.*;
import travelbeeee.PDFLOpjt.utility.PermissionChecker;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.view.CommentView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller @RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final CommentService commentService;
    private final UserService userService;
    private final ThumbnailService thumbnailService;
    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(ContentController.class);

    @GetMapping("/content/upload")
    public String contentUploadForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        return "/content/uploadForm";
    }

    @PostMapping("/content/upload")
    public String contentUpload(HttpSession httpSession, @Valid ContentForm contentForm, BindingResult bindingResult, HttpServletRequest request) throws PDFLOException, IOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);

        if(bindingResult.hasErrors()) throw new PDFLOException(ReturnCode.CONTENT_UPLOAD_ERROR);

        contentService.upload(contentForm, (int) httpSession.getAttribute("userId"));

        return "redirect:/";
    }

    @GetMapping("/content/{contentId}")
    public String contentDetail(@PathVariable int contentId, Model model, HttpSession httpSession) throws PDFLOException {
        Content content = contentService.selectById(contentId);
        Thumbnail thumbnail = thumbnailService.selectById(content.getThumbnailId());
        List<Comment> comments = commentService.selectAllByContent(contentId);
        List<CommentView> commentViews = new ArrayList<>();
        for (Comment comment : comments) {
            CommentView commentView = new CommentView();

            commentView.setComments(comment.getComments());
            commentView.setScore(comment.getScore());
            commentView.setLocaldate(comment.getLocaldate());
            commentView.setUsername(userService.selectById(comment.getUserId()).getUsername());
            commentView.setCommentId(comment.getCommentId());
            commentViews.add(commentView);
        }
        model.addAttribute("title", content.getTitle());
        model.addAttribute("content", content.getContent());
        model.addAttribute("contentId", content.getContentId());
        model.addAttribute("price", content.getPrice());
        model.addAttribute("thumbnailLocation", "/files/THUMBNAIL/" + thumbnail.getSaltedFileName());
        model.addAttribute("comments", commentViews);

        if(httpSession.getAttribute("userId") == null)
            model.addAttribute("isSeller", false);
        else
            model.addAttribute("isSeller", (int)httpSession.getAttribute("userId") == content.getUserId());

        return "/content/detail";
    }

    @PostMapping("/content/buy/{contentId}")
    public String contentBuy(@PathVariable("contentId") int contentId, HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        int userId = (Integer) httpSession.getAttribute("userId");

        orderService.order(userId, contentId);

        return "redirect:/";
    }

    @PostMapping("/content/delete/{contentId}")
    public String contentDelete(@PathVariable("contentId") int contentId, HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        contentService.delete(contentId, (int) httpSession.getAttribute("userId"));

        return "redirect:/";
    }

    @GetMapping("/content/modify/{contentId}")
    public String contentModifyForm(@PathVariable("contentId") int contentId, HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        Content content = contentService.selectById(contentId);
        if(content.getUserId() != (int)httpSession.getAttribute("userId"))
            throw new PDFLOException(ReturnCode.USER_NOT_SELLER);
        model.addAttribute("contentId", contentId);
        model.addAttribute("title", content.getTitle());
        model.addAttribute("content", content.getContent());
        model.addAttribute("price", content.getPrice());

        return "/content/modifyForm";
    }

    @PostMapping("/content/modify/{contentId}")
    public String contentModify(@PathVariable("contentId") int contentId, HttpSession httpSession, @Valid ContentForm contentForm, BindingResult bindingResult) throws PDFLOException, IOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);
        if(bindingResult.hasErrors()) throw new PDFLOException(ReturnCode.CONTENT_UPLOAD_ERROR);

        contentService.modify(contentId, (int)httpSession.getAttribute("userId"), contentForm);

        return "redirect:/";
    }

    @PostMapping("/content/download/{contentId}")
    public void contentDownload(@PathVariable("contentId") int contentId, HttpSession httpSession, HttpServletResponse response) throws PDFLOException, IOException {
        PermissionChecker.checkPermission(httpSession);

        int userId = (int)httpSession.getAttribute("userId");
        byte[] downloadFile = contentService.download(userId, contentId);
        response.addHeader("Content-Disposition", "attachment; fileName=content.pdf");
        response.getOutputStream().write(downloadFile);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
