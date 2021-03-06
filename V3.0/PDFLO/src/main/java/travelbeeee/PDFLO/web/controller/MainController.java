package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import travelbeeee.PDFLO.domain.model.dto.ItemViewDto;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.service.ItemService;
import travelbeeee.PDFLO.domain.utility.FileManager;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @Value("${file.dir}")
    private String filePath;

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        log.info("home");
        List<Item> items = itemService.findSellItemWithMemberAndThumbnail();
        List<ItemViewDto> itemViewDtos = items.stream()
                .map(i -> new ItemViewDto(i))
                .collect(Collectors.toList());
        log.info("itemViewDtos : {}", itemViewDtos);
        model.addAttribute("items", itemViewDtos);
        return "index";
    }

    @ResponseBody
    @GetMapping("/images/{file}")
    public Resource downloadImage(@PathVariable String file) throws MalformedURLException {
        log.info("downloadImage");
        return new UrlResource("file:" + filePath + file);
    }
}
