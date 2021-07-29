package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Order;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;
import travelbeeee.PDFLO.domain.repository.*;
import travelbeeee.PDFLO.domain.service.PopularItemService;

import javax.transaction.Transactional;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    @Scheduled(fixedDelay = 100000) // 매일 0시에 실행
    @Override
    public void updatePopularScore() {
        LocalDateTime curTime = LocalDateTime.now();
        log.info("updatePopularScore 실행");
        log.info("현재 시간 : {}", curTime);

        List<Item> items = itemRepository.findAllWithComments();
        List<OrderItem> orders = orderItemRepository.findAllWithItem();
        Map<Long, ArrayList<OrderItem>> itemOrderMap = makeItemOrderMap(orders);
        // 나는 아이템별로 구매내역이 필요함! --> Map , ArrayList 2개를 이용하자
        for (Item item : items) {
//            log.info("Item : {}", item.getId());
            // 상품후기들을 순회하며 점수계산
            Double commentScore = 0.0;
            Double commentAvg = 0.0;
            List<Comment> comments = item.getComments();
            for (Comment comment : comments) {
//                log.info("Item의 Comment : {}", comment.getId());
                long betweenDays = ChronoUnit.DAYS.between(curTime, comment.getCreatedDate());
                commentScore += calculateExtraPoints(betweenDays);
                commentAvg += comment.getScore();
            }
//            log.info("pure CommentScore : {}", commentScore);
            if(!comments.isEmpty())
                commentAvg /= (comments.size());
            commentScore *= (commentAvg / (5.0));

            // 상품구매내역을 순회하며 점수 계산
            Integer orderCnt = 0;
            Double orderScore = 0.0;
            if (itemOrderMap.containsKey(item.getId())) {
                ArrayList<OrderItem> orderItems = itemOrderMap.get(item.getId());
                for (OrderItem orderItem : orderItems) {
//                    log.info("Item의 구매내역 : {}",orderItem.getId());
                    long betweenDays = ChronoUnit.DAYS.between(curTime, orderItem.getCreatedDate());
                    orderScore += calculateExtraPoints(betweenDays);
                    orderCnt++;
                }
            }
//            log.info("commentScore : {}, orderScore : {}", commentScore, orderScore);
            popularItemRepository.updatePopular(item.getId(), orderScore + commentScore, commentAvg, comments.size(), orderCnt);
        }
    }

    private Map<Long, ArrayList<OrderItem>> makeItemOrderMap(List<OrderItem> orders) {
        Map<Long, ArrayList<OrderItem>> res = new HashMap<>();
        for (OrderItem order : orders) {
            Long itemId = order.getItem().getId();
            if(res.containsKey(itemId)){
                ArrayList<OrderItem> values = res.get(itemId);
                values.add(order);
            }else{
                ArrayList<OrderItem> value = new ArrayList<>();
                value.add(order);
                res.put(itemId, value);
            }
        }
        return res;
    }

    private Double calculateExtraPoints(long betweenDays) {
        if (betweenDays <= 7) return 5.0;
        else if (betweenDays <= 30) return 4.0;
        else if (betweenDays <= 60) return 3.0;
        else if (betweenDays <= 90) return 2.0;
        return 1.0;
    }
}
