package travelbeeee.PDFLO.web.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class TestController {

    @GetMapping("/test")
    public String testForm(Model model) {
        model.addAttribute("testForm", new TestForm());
        log.info("GET test");
        return "/test/test";
    }

    @PostMapping("/test")
    public String test() {
        log.info("POST test");
        return "redirect:/";
    }

    @PostMapping("/test/test2")
    public ResponseEntity<?> test(@RequestParam String email) throws IOException {
        log.info("email: " + email);
        return ResponseEntity.ok().body("Hello");
    }

    @GetMapping("/test/summernote")
    public String summerNoteForm(){
        return "/test/summerNote";
    }

    @PostMapping("/test/summernote")
    public String summerNote(@RequestParam("summernote") String summernote) {
        log.info("summerNote input : {}", summernote);
        return "/test/summerNote";
    }
}