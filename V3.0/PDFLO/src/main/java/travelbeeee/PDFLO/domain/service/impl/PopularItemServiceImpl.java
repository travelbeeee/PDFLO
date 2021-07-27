package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;
import travelbeeee.PDFLO.domain.repository.*;
import travelbeeee.PDFLO.domain.service.PopularItemService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularItemServiceImpl implements PopularItemService {

    private static final Double MAX_COMMENT_SCORE = 5.0;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final OrderItemRepository orderItemRepository;
    private final PopularItemRepository popularItemRepository;

    /**
     * 매일 0시에 실행되는 인기 게시물 갱신 메소드
     * [ 가산점 기준 ]
     * ~7일 이내 5점
     * 8 ~ 30일 이내 4점
     * 31 ~ 60일 이내 3점
     * 61 ~ 90일 이내 2점
     * 91일 ~ 1점
     * --> Sum(기간 별 가산점 * 구매) + Sum(기간 별 가산점 * 후기 * (평점 / 5.0))
     */
    @Transactional
//    @Scheduled(cron = "0 0 00 * * ?") // 매일 0시에 실행
    @Scheduled(fixedDelay = 100000) // 매일 0시에 실행
    @Override
    public void updatePopularScore() {
        log.info("updatePopularScore 실행");
        LocalDateTime curTime = LocalDateTime.now();
        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            Double orderScore = 0.0;
            Double commentScore = 0.0;
            Double commentAvg = 0.0;
            List<Comment> comments = commentRepository.findAllByItem(item.getId());
            for (Comment c : comments) {
                commentAvg += c.getScore();
                long betweenDays = ChronoUnit.DAYS.between(curTime, c.getCreatedDate());
                commentScore += calculateExtraPoints(betweenDays);
            }
            if (!comments.isEmpty()) {
                commentAvg /= comments.size();
                commentScore *= (commentAvg / MAX_COMMENT_SCORE);
            }
            List<OrderItem> orders = orderItemRepository.findAllByItem(item.getId());
            for (OrderItem o : orders) {
                long betweenDays = ChronoUnit.DAYS.between(curTime, o.getCreatedDate());
                orderScore += calculateExtraPoints(betweenDays);
            }
            log.info("itemId : {}", item.getId());
            log.info("commentAvg : {}, commentScore : {}, orderScore : {}", commentAvg, commentScore, orderScore);
            popularItemRepository.updatePopular(item.getId(), orderScore + commentScore, commentAvg, comments.size(), orders.size());
        }
    }

    private Double calculateExtraPoints(long betweenDays) {
        if (betweenDays <= 7) return 5.0;
        else if (betweenDays <= 30) return 4.0;
        else if (betweenDays <= 60) return 3.0;
        else if (betweenDays <= 90) return 2.0;
        return 1.0;
    }
}
