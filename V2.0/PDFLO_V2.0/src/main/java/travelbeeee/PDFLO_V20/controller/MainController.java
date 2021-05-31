package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import travelbeeee.PDFLO_V20.domain.dto.ItemViewDto;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @Value("${file.absolute_location}")
    private String rootLocation;

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model){
        List<Item> items = itemService.findAllWithMemberAndThumbnail();
        List<ItemViewDto> itemMainDtos = new ArrayList<>();
        itemMainDtos = items.stream()
                .map(i -> new ItemViewDto(i, rootLocation))
                .collect(Collectors.toList());

        model.addAttribute("items", itemMainDtos);
        return "main";
    }
}
