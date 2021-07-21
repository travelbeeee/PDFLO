package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import travelbeeee.PDFLO.domain.model.dto.ItemViewDto;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @Value("${file.dir}")
    private String fileDir;

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        List<Item> items = itemService.findSellItemWithMemberAndThumbnail();
        List<ItemViewDto> itemMainDtos = new ArrayList<>();
        itemMainDtos = items.stream()
                .map(i -> new ItemViewDto(i))
                .collect(Collectors.toList());
        log.info("itemMainDtos : {}", itemMainDtos);
        model.addAttribute("rootLocation", fileDir);
        model.addAttribute("items", itemMainDtos);
        return "index";
    }

}
