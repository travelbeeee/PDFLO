package travelbeeee.PDFLO.domain.utility;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * 현재 데이터의 totalPage 수와 pageSize 를 기반으로
 * startPageNum, endPageNum, prevPageNum, nextPageNumn을 계산해 Model에 추가해준다.
 */
@Component
public class PageMaker {

    public static void makePage(Integer totalPages, Integer pageSize, Integer pageNum, Model model) {
        Integer startPageNum = (pageNum / pageSize) * pageSize + 1;
        Integer endPageNum = (pageNum / pageSize) * pageSize + pageSize;
        Integer prevPageNum = (pageNum / pageSize) * pageSize + 1 - pageSize;
        Integer nextPageNum = (pageNum / pageSize) * pageSize + pageSize + 1;

        if(totalPages == 0) return;

        if(prevPageNum >= 1){
            model.addAttribute("prevPageNum", prevPageNum);
        }
        if (nextPageNum <= totalPages) {
            model.addAttribute("nextPageNum", nextPageNum);
        }
        if(endPageNum >= totalPages){
            endPageNum = totalPages;
        }
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("endPageNum", endPageNum);

        return;
    }
}
