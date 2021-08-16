package travelbeeee.PDFLO.domain.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;


@Component
@Slf4j
public class PageMaker {

    /**
     * 현재 totalPage 수와 pageSize 를 기반으로
     * startPageNum, endPageNum, prevPageNum, nextPageNumn을 계산해 Model에 추가해준다.
     */
    public static void calcPageNum(Integer totalPages, Integer pageSize, Integer pageNum, Model model) {
        log.info("PageMaker - makePage");
        if(totalPages == 0) return;

        Integer startPageNum = (pageNum / pageSize) * pageSize;
        Integer endPageNum = (pageNum / pageSize) * pageSize + (pageSize - 1);
        Integer prevPageNum = startPageNum - pageSize;
        Integer nextPageNum = endPageNum + 1;

        if(prevPageNum >= 0){
            log.info("prevPageNum : {}", prevPageNum);
            model.addAttribute("prevPageNum", prevPageNum);
        }
        if (nextPageNum < totalPages) {
            log.info("nextPageNum : {}", nextPageNum);
            model.addAttribute("nextPageNum", nextPageNum);
        }
        if(endPageNum >= totalPages){
            endPageNum = totalPages - 1;
        }
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("endPageNum", endPageNum);
        log.info("startPageNum : {}", startPageNum);
        log.info("endPageNum : {}", endPageNum);

        return;
    }
}
