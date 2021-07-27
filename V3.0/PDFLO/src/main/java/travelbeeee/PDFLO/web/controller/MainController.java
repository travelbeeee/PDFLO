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
    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        List<ItemViewDto> itemViewDtos = itemService.findAllOrderByPopular();
        log.info("itemViewDtos : {}", itemViewDtos);
        model.addAttribute("items", itemViewDtos);
        return "index";
    }

    @ResponseBody
    @GetMapping("/images/{location}/{year}/{month}/{day}/{fileName}")
    public Resource downloadImage(@PathVariable String location, @PathVariable String year, @PathVariable String month,
                                  @PathVariable String day, @PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + filePath + location + fileSeperator + year +
                fileSeperator + month + fileSeperator + day + fileSeperator + fileName);
    }
}
