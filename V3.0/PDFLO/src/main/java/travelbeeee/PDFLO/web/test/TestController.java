package travelbeeee.PDFLO.web.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class TestController {

    @GetMapping("/test")
    public String testForm(Model model) {
        model.addAttribute("testForm", new TestForm());
        return "/test/test";
    }

    @ResponseBody
    @PostMapping("/test")
    public TestForm test(@ModelAttribute TestForm testForm) {
        log.info("testForm : {}", testForm);
        return testForm;
    }
}
