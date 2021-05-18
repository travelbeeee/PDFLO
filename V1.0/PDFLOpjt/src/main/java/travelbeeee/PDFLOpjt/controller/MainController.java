package travelbeeee.PDFLOpjt.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.domain.Thumbnail;
import travelbeeee.PDFLOpjt.service.ContentService;
import travelbeeee.PDFLOpjt.service.ThumbnailService;
import travelbeeee.PDFLOpjt.service.UserService;
import travelbeeee.PDFLOpjt.view.ContentView;

import java.util.ArrayList;
import java.util.List;

@Controller @RequiredArgsConstructor
public class MainController {
    private final ContentService contentService;
    private final UserService userService;
    private final ThumbnailService thumbnailService;

    Logger logger = LoggerFactory.getLogger(MainController.class);
    /**
     * 현재 등록된 글과 해당 글의 썸네일 이미지를 넘겨준다.
     * @return Main페이지
     */
    @GetMapping("/")
    public String main(Model model){
        List<Content> contents = contentService.selectAll();
        List<ContentView> contentViews = new ArrayList<>();
        for(Content content : contents){
            ContentView contentView = new ContentView();
            contentView.setContentId(content.getContentId());
            contentView.setLocaldate(content.getLocaldate());
            contentView.setTitle(content.getTitle());

            Thumbnail thumbnail = thumbnailService.selectById(content.getThumbnailId());
            contentView.setThumbnailLocation(thumbnail.getLocation()  +  "/t-" + thumbnail.getSaltedFileName());
            contentView.setUsername(userService.selectById(content.getUserId()).getUsername());
            contentViews.add(contentView);
        }
        model.addAttribute("contents", contentViews);
        return "main";
    }
}
