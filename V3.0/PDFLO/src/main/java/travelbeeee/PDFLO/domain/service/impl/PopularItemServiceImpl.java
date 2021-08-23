package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLO.domain.model.entity.*;
import travelbeeee.PDFLO.domain.repository.*;
import travelbeeee.PDFLO.domain.service.PopularItemService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
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
@Transactional
public class PopularItemServiceImpl implements PopularItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final OrderItemRepository orderItemRepository;
    private final PopularItemRepository popularItemRepository;

    /**
     * 인기 게시물 갱신 메소드
     *
     * 매일 0시에 실행되고, 2000개씩 업데이트를 진행한다.
     *
     * [ 가산점 기준 ]
     * ~7일 이내 5점
     * 8 ~ 30일 이내 4점
     * 31 ~ 60일 이내 3점
     * 61 ~ 90일 이내 2점
     * 91일 ~ 1점
     * --> Sum(기간 별 가산점 * 구매 수) + Sum(기간 별 가산점 * 후기 수 * 상대평점)
     */
    //TODO : 인기도 계산 Batch하기
    @Scheduled(cron = "0 0 00 * * ?") // 매일 0시에 실행
    @Override
    public void updatePopularScore() {
        LocalDateTime curTime = LocalDateTime.now();

        final Double MAX_COMMENT_SCORE = 5.0;
        final Integer BATCH_SIZE = 2000
                ;
        Integer pageNum = 0;
        List<Long> itemIds = new ArrayList<>();

        while (pageNum == 0 || itemIds.size() == BATCH_SIZE) {
            log.info("{} : {} 번 째 배치작업진행중", LocalDateTime.now(), pageNum);
            PageRequest pageRequest = PageRequest.of(pageNum, BATCH_SIZE, Sort.by(Sort.Direction.ASC, "id"));
            itemIds = itemRepository.findAllId(pageRequest);
            List<PopularItem> popularItems = popularItemRepository.findAllByIds(itemIds);
            Map<Long, ArrayList<OrderItem>> orderItemMap = makeOrderItemMap(orderItemRepository.findAllByItems(itemIds));
            Map<Long, ArrayList<Comment>> commentMap = makeCommentMap(commentRepository.findAllByItems(itemIds));
            for (PopularItem popularItem : popularItems) {
                Long itemId = popularItem.getItem().getId();
                Double orderScore = 0.0;
                Integer orderCnt = 0;
                Double commentScore = 0.0;
                Double commentAvg = 0.0;
                Integer commentCnt = 0;
                if(commentMap.containsKey(itemId)){
                    ArrayList<Comment> comments = commentMap.get(itemId);
                    commentCnt = comments.size();
                    for (Comment c : comments) {
                        long betweenDays = ChronoUnit.DAYS.between(curTime, c.getCreatedDate());
                        commentAvg += c.getScore();
                        commentScore += calculateExtraPoints(betweenDays);
                    }
                    commentAvg /= commentCnt;
                    commentScore *= (commentAvg / MAX_COMMENT_SCORE);
                }
                if(orderItemMap.containsKey(itemId)){
                    ArrayList<OrderItem> orderItems = orderItemMap.get(itemId);
                    for (OrderItem oi : orderItems) {
                        long betweenDays = ChronoUnit.DAYS.between(curTime, oi.getCreatedDate());
                        orderScore += calculateExtraPoints(betweenDays);
                    }
                    orderCnt = orderItems.size();
                }
                popularItem.updatePopularity(orderScore + commentScore, commentAvg, commentCnt, orderCnt);
//                popularItemRepository.updatePopular(itemId, orderScore + commentScore, commentAvg, commentCnt, orderCnt);
            }
            if(pageNum == 0 && itemIds.size() == 0){ // 초기 아무것도 없는 상태
                break;
            }
            pageNum++;
        }
    }

    private Map<Long, ArrayList<OrderItem>> makeOrderItemMap(List<OrderItem> orderItems) {
        Map<Long, ArrayList<OrderItem>> res = new HashMap<>();
        for (OrderItem order : orderItems) {
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

    private Map<Long, ArrayList<Comment>> makeCommentMap(List<Comment> comments) {
        Map<Long, ArrayList<Comment>> res = new HashMap<>();
        for (Comment comment : comments) {
            Long itemId = comment.getItem().getId();
            if(res.containsKey(itemId)){
                ArrayList<Comment> values = res.get(itemId);
                values.add(comment);
            }else{
                ArrayList<Comment> value = new ArrayList<>();
                value.add(comment);
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
