package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import travelbeeee.PDFLO.domain.model.dto.ItemDto;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;
import travelbeeee.PDFLO.domain.service.ItemService;
import travelbeeee.PDFLO.domain.utility.PageMaker;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @Value("${file.dir}")
    private String filePath;
    private String fileSeperator = "/";
    private Integer itemSizePerPage = 6;
    private Integer pageSize = 10;

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        PageRequest popularRequest = PageRequest.of(0, itemSizePerPage, Sort.by(Sort.Direction.DESC, "score").and(Sort.by(Sort.Direction.DESC, "createdDate")));
        PageRequest recentRequest = PageRequest.of(0, itemSizePerPage, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PopularItem> pagePopularItems = itemService.findSellItemsWithItemAndThumbnailByPaging(popularRequest);
        Page<PopularItem> pageRecentItems = itemService.findSellItemsWithItemAndThumbnailByPaging(recentRequest);

        List<PopularItem> popularItems = pagePopularItems.getContent();
        List<PopularItem> recentItems = pageRecentItems.getContent();

        List<ItemDto> popularItemDto = popularItems.stream().map(pi -> new ItemDto(pi))
                .collect(Collectors.toList());
        List<ItemDto> recentItemDto = recentItems.stream().map(pi -> new ItemDto(pi))
                .collect(Collectors.toList());

        log.info("popularItem : {}", pagePopularItems.getTotalElements());
        log.info("recentItem : {}", pageRecentItems.getTotalElements());

        model.addAttribute("popularItems", popularItemDto);
        model.addAttribute("popularHasNext", pagePopularItems.hasNext());
        model.addAttribute("recentItems", recentItemDto);
        model.addAttribute("recentHasNext", pageRecentItems.hasNext());

        return "index";
    }

    @GetMapping("/popular/{pageNum}")
    public String popularItems(@PathVariable("pageNum") Integer pageNum, Model model) {
        log.info("현재 pageNum : {}", pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, itemSizePerPage, Sort.by(Sort.Direction.DESC, "score").and(Sort.by(Sort.Direction.DESC, "createdDate")));
        Page<PopularItem> pagePopularItems = itemService.findSellItemsWithItemAndThumbnailByPaging(pageRequest);

        PageMaker.calcPageNum(pagePopularItems.getTotalPages(), pageSize, pageNum, model);

        List<ItemDto> itemDtos = pagePopularItems.getContent().stream().map(pi -> new ItemDto(pi))
                .collect(Collectors.toList());

        model.addAttribute("items", itemDtos);
        model.addAttribute("curPageNum", pageNum);
        model.addAttribute("popular", true);
        model.addAttribute("recent", false);
        return "/item/list";
    }

    @GetMapping("/recent/{pageNum}")
    public String recentItems(@PathVariable("pageNum") Integer pageNum, Model model) {
        log.info("현재 pageNum : {}", pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, itemSizePerPage, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<PopularItem> pageRecentItems = itemService.findSellItemsWithItemAndThumbnailByPaging(pageRequest);

        PageMaker.calcPageNum(pageRecentItems.getTotalPages(), pageSize, pageNum, model);

        List<ItemDto> itemDtos = pageRecentItems.getContent().stream().map(pi -> new ItemDto(pi))
                .collect(Collectors.toList());

        model.addAttribute("items", itemDtos);
        model.addAttribute("curPageNum", pageNum);
        model.addAttribute("popular", false);
        model.addAttribute("recent", true);
        return "/item/list";
    }

    @ResponseBody
    @GetMapping("/images/{location}/{year}/{month}/{day}/{fileName}")
    public Resource downloadImage(@PathVariable String location, @PathVariable String year, @PathVariable String month,
                                  @PathVariable String day, @PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + filePath + location + fileSeperator + year +
                fileSeperator + month + fileSeperator + day + fileSeperator + fileName);
    }
}
