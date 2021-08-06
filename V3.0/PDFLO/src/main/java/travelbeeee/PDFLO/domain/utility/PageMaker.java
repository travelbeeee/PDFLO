package travelbeeee.PDFLO.domain.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * 현재 데이터의 totalPage 수와 pageSize 를 기반으로
 * startPageNum, endPageNum, prevPageNum, nextPageNumn을 계산해 Model에 추가해준다.
 */
@Component
@Slf4j
public class PageMaker {

    public static void makePage(Integer totalPages, Integer pageSize, Integer pageNum, Model model) {
        log.info("PageMaker - makePage");
        Integer startPageNum = (pageNum / pageSize) * pageSize + 1;
        Integer endPageNum = (pageNum / pageSize) * pageSize + pageSize;
        Integer prevPageNum = (pageNum / pageSize) * pageSize + 1 - pageSize;
        Integer nextPageNum = (pageNum / pageSize) * pageSize + pageSize + 1;

        if(totalPages == 0) return;

        if(prevPageNum >= 1){
            log.info("prevPageNum : {}", prevPageNum);
            model.addAttribute("prevPageNum", prevPageNum);
        }
        if (nextPageNum <= totalPages) {
            log.info("nextPageNum : {}", nextPageNum);
            model.addAttribute("nextPageNum", nextPageNum);
        }
        if(endPageNum >= totalPages){
            endPageNum = totalPages;
        }
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("endPageNum", endPageNum);
        log.info("startPageNum : {}", startPageNum);
        log.info("endPageNum : {}", endPageNum);

        return;
    }
}
