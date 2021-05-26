package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import travelbeeee.PDFLO_V20.domain.dto.ItemMainDto;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @Value("${file.absolute_location}")
    private String rootLocation;

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model){
        List<Item> items = itemService.findAllWithMember();
        List<ItemMainDto> itemMainDtos = new ArrayList<>();
        for (Item item : items) {
            itemMainDtos.add(new ItemMainDto(item, rootLocation));
        }
        model.addAttribute("items", itemMainDtos);
        return "main";
    }
}
